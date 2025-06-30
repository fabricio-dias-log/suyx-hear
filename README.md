# 📱 Aplicativo Mobile Suyx Hear
## Sobre o Projeto

Suyx Hear é um aplicativo para a plataforma Android. O objetivo principal é fornecer uma ferramenta acessível para o monitoramento de níveis de ruído em tempo real, visando a manutenção da paz sonora e a convivência harmoniosa em ambientes residenciais, como condomínios.

O aplicativo foi pensado com foco em acessibilidade e usabilidade, permitindo que qualquer usuário possa facilmente medir os níveis de som ao seu redor, definir limites e receber alertas quando esses limites forem ultrapassados.
### ✨ Funcionalidades Principais

- Monitoramento em Tempo Real: Mede o nível de ruído do ambiente em decibéis (dB) de forma constante e automática.

- Limite de Ruído Personalizado: Permite ao usuário definir um limite de decibéis e ser notificado quando este for ultrapassado.

- Notificações de Alerta: Envia notificações ao usuário quando o nível de ruído excede o limite configurado.

- Histórico de Medições: Mantém um registro persistente de todas as medições, exibindo os dados em um gráfico e listando os momentos em que o limite foi excedido.

- Persistência de Dados: Todas as configurações, incluindo o nome do usuário, limite de dB, tema e o histórico de ruídos, são salvas no dispositivo e mantidas entre as sessões.

- Sessão de Login Persistente: O usuário permanece logado no aplicativo após o primeiro acesso, precisando fazer logout apenas manualmente.

- Tema com Alto Contraste: Oferece um modo escuro para melhorar a acessibilidade e o conforto visual em ambientes com pouca luz.

### 🛠️ Tecnologias e Bibliotecas Utilizadas

O aplicativo foi desenvolvido de forma nativa para Android, utilizando as seguintes tecnologias:

- Linguagem: Kotlin

- Arquitetura: MVVM (Model-View-ViewModel) com SharedViewModel para comunicação entre as telas.

- Interface Gráfica: Android Views com ViewBinding.

- Navegação: Android Jetpack Navigation Component para gerenciar o fluxo entre as telas.

- Gráficos: MPAndroidChart para a exibição do histórico de ruídos.

- Persistência de Dados:

  - SharedPreferences para salvar configurações do usuário e o estado do login.

  - Gson para serializar a lista de histórico em formato JSON para ser salva nas SharedPreferences.

  - Componentes de UI: Material Design Components para um visual moderno e consistente.

### 🚀 Como Executar o Projeto

Para compilar e executar este projeto, você precisará do Android Studio.

- Clone o Repositório:
``` Bash
git clone https://github.com/seu-usuario/suyx-hear.git
```

- Abra no Android Studio:

- Inicie o Android Studio.

- Selecione "Open an Existing Project".

- Navegue até a pasta onde você clonou o projeto e selecione-a.

- Sincronize as Dependências:

    - O Android Studio irá sincronizar automaticamente as dependências listadas no arquivo build.gradle.kts.

- Execute o Aplicativo:

    - Conecte um dispositivo Android ou inicie um emulador.

    - Clique no botão "Run 'app'" (ícone de play verde) na barra de ferramentas superior.

### 🔮 Trabalhos Futuros

Conforme descrito na documentação original do projeto, algumas possibilidades para evolução incluem:

- Expansão para iOS: Desenvolver uma versão para a plataforma iOS para alcançar um público maior.

- Integração com Sistemas de Condomínios: Permitir que administradores tenham uma visão geral (anonimizada) dos níveis de ruído nas áreas comuns.

- Machine Learning: Implementar funcionalidades de aprendizado de máquina para identificar padrões de ruído e sugerir ações proativas aos usuários.

### ✒️ Autor

Fabricio Dias Nascimento