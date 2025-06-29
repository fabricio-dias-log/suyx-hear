package com.example.suyxhear

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.suyxhear.data.NoiseRecord
import com.example.suyxhear.databinding.FragmentMonitorBinding
import com.example.suyxhear.util.NotificationHelper
import com.example.suyxhear.viewmodel.SharedViewModel
import java.io.IOException
import kotlin.math.log10

class MonitorFragment : Fragment() {

    private var _binding: FragmentMonitorBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var mediaRecorder: MediaRecorder? = null
    private var isMonitoring = false
    private val handler = Handler(Looper.getMainLooper())
    private var dbLimit = 55
    private lateinit var notificationHelper: NotificationHelper
    private var lastNotificationTime = 0L

    private val requestAudioPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) { checkNotificationPermissionAndStart() }
        }

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            // A monitorização começa independentemente da permissão de notificação
            startMonitoring()
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonitorBinding.inflate(inflater, container, false)
        notificationHelper = NotificationHelper(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadUserName()

        sharedViewModel.dbLimit.observe(viewLifecycleOwner) { limit ->
            dbLimit = limit
            if(isMonitoring) updateGauge(binding.textDecibels.text.toString().removeSuffix(" dB").toInt())
        }

        binding.buttonToggleMonitoring.setOnClickListener {
            if (isMonitoring) stopMonitoring() else checkAudioPermission()
        }
    }

    private fun loadUserName() {
        val sharedPref = activity?.getSharedPreferences("SuyxHearPrefs", Context.MODE_PRIVATE) ?: return
        val name = sharedPref.getString("USER_NAME", "Usuário")
        binding.textGreeting.text = "Olá, $name!"
    }

    private fun checkAudioPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED -> {
                checkNotificationPermissionAndStart()
            }
            else -> requestAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    private fun checkNotificationPermissionAndStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED -> {
                    startMonitoring()
                }
                else -> requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            startMonitoring()
        }
    }

    private fun startMonitoring() {
        if (isMonitoring) return
        val outputFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) "${requireContext().cacheDir.absolutePath}/recording.3gp" else "/dev/null"

        mediaRecorder = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) MediaRecorder(requireContext()) else @Suppress("DEPRECATION") MediaRecorder()).apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(outputFile)
            try {
                prepare()
                start()
                isMonitoring = true
                binding.buttonToggleMonitoring.text = "Parar Monitoramento"
                handler.post(updateDecibels)
            } catch (e: IOException) {
                Log.e("MediaRecorder", "prepare() failed", e)
            }
        }
    }

    private val updateDecibels = object : Runnable {
        override fun run() {
            if (isMonitoring) {
                // ... (cálculo de decibéis permanece o mesmo) ...
                val amplitude = mediaRecorder?.maxAmplitude ?: 0
                val db = if (amplitude > 0) (20 * log10(amplitude.toDouble()) + 20).coerceIn(20.0, 120.0) else 0.0
                val roundedDb = db.toInt()

                binding.textDecibels.text = "$roundedDb dB"
                updateGauge(roundedDb)
                sharedViewModel.addNoiseRecord(NoiseRecord(System.currentTimeMillis(), roundedDb))

                // Lógica de notificação
                if (roundedDb > dbLimit) {
                    val currentTime = System.currentTimeMillis()
                    // Envia notificação a cada 30 segundos no máximo
                    if (currentTime - lastNotificationTime > 30000) {
                        notificationHelper.sendNotification(roundedDb, dbLimit)
                        lastNotificationTime = currentTime
                    }
                }

                handler.postDelayed(this, 500)
            }
        }
    }

    private fun updateGauge(db: Int) {
        binding.progressGauge.progress = (db * 100 / 120).coerceIn(0, 100)
        when {
            db > dbLimit -> binding.progressGauge.setIndicatorColor(ContextCompat.getColor(requireContext(), R.color.red))
            db > dbLimit * 0.8 -> binding.progressGauge.setIndicatorColor(ContextCompat.getColor(requireContext(), R.color.yellow))
            else -> binding.progressGauge.setIndicatorColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
    }

    private fun stopMonitoring() {
        if (!isMonitoring) return
        handler.removeCallbacks(updateDecibels)
        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null
        isMonitoring = false
        binding.buttonToggleMonitoring.text = "Iniciar Monitoramento"
    }

    override fun onStop() {
        super.onStop()
        stopMonitoring()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}