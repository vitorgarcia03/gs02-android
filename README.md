# CidadeSegura — Global Solution 2026.2 | CitizenTech

## Integrantes
- **Caíque De Lacerda Braga — RM 555532**
- **Vitor Garcia Correia — RM 558516**

## 1. Visão geral
Aplicativo Android em **Kotlin + Jetpack Compose** que facilita a consulta da previsão de chuva para cidades brasileiras e apresenta boas práticas de prevenção. O desafio CitizenTech propõe o uso de tecnologia para melhorar a vida das pessoas, com foco em segurança e acesso à informação.

**Problema:** muitas pessoas precisam consultar dados meteorológicos e orientações de prevenção antes de se deslocar em dias chuvosos.

**Solução:** acesso simplificado a previsões, lista de cidades favoritas e recomendações educativas. O aplicativo **não afirma prever enchentes nem emitir alertas oficiais**.

## 2. Tema e impacto
Global Solution 2026.2 — CitizenTech. O MVP se relaciona aos **ODS 11 (Cidades e comunidades sustentáveis)** e **ODS 13 (Ação climática)** por favorecer o acesso a dados para prevenção e mobilidade mais consciente.

## 3. Telas e fluxo
```text
Splash
  ├── primeira execução / introdução não dispensada → Onboarding (3 etapas)
  │      └── Começar → Início
  └── introdução já dispensada → Início
Início → Detalhe da cidade → Previsão de 5 dias
Barra inferior → Início | Previsão | Dicas | Salvos | Sobre
Início → favoritar cidade → Salvos → abrir cidade
Previsão → selecionar cidade → atualizar previsão
```
**Telas principais:** Início, Previsão, Dicas, Salvos e Sobre. Telas adicionais: Splash, Introdução e Detalhe.

## 4. Funcionalidades
- Splash com logo e redirecionamento automático usando `SharedPreferences`.
- Onboarding com ícone, título, descrição, voltar/avançar e opção de não repetir.
- 5 telas principais, com `Navigation Compose`.
- Busca local entre cidades cadastradas, seleção de cidade, detalhes e favoritos persistentes.
- Consumo HTTP da API REST Open-Meteo com Retrofit + Gson.
- Estados **inicial, carregamento, sucesso e erro** gerenciados por `CityViewModel` e `StateFlow`.
- Interface usando `Scaffold`, `TopAppBar`, `Column`, `Row`, `Card`, `LazyColumn`, botões, ícones e imagem.
- Componentes reutilizáveis (`CityCard`, `ForecastCard`, `MainLayout`, `SectionTitle`).

## 5. API utilizada
**Open-Meteo — Weather Forecast API**
- Documentação: https://open-meteo.com/en/docs
- Base URL: `https://api.open-meteo.com/`
- Endpoint: `GET /v1/forecast`
- Parâmetros: `latitude`, `longitude`, `daily=precipitation_sum,precipitation_probability_max,temperature_2m_max,weather_code`, `timezone=America/Sao_Paulo`, `forecast_days=5`.
- Retorno: acumulado diário de precipitação (mm), probabilidade máxima de precipitação (%), temperatura máxima (°C) e código meteorológico.
- Não requer token para uso não comercial, sujeito às regras da API.

**Observação:** as recomendações exibidas ao lado da previsão são orientações ilustrativas baseadas em limites simples no aplicativo; **não** constituem avisos técnicos oficiais.

## 6. Arquitetura
```text
MainActivity / Navigation Compose
          ↓
    Screens (Compose)  ← observa StateFlow
          ↓
    CityViewModel (AndroidViewModel)
          ↓
    CityRepository ── Retrofit ── Open-Meteo REST API
    PreferencesRepository ── SharedPreferences (onboarding / favoritos)
```
- `data/`: DTOs, API, catálogo de cidades e preferências.
- `ui/`: ViewModel, telas, componentes reutilizáveis e tema.
- `MainActivity.kt`: inicialização e rotas de navegação.
- UI sem chamadas HTTP diretas; consulta realizada no ViewModel/repositório.

## 7. Como executar
1. Abra **Android Studio** com JDK 17 e Android SDK 35.
2. **File > Open** → selecione a pasta `CidadeSegura` (esta pasta do ZIP).
3. Aguarde o Gradle sincronizar e baixar dependências com a internet conectada. Se o Android Studio solicitar um Gradle local, selecione **Gradle 8.9** (o `gradle-wrapper.properties` está incluído).
4. Selecione um emulador Android com API 26 ou superior (por exemplo Pixel com API 35).
5. Clique em **Run 'app'**.
6. Conceda acesso à internet ao emulador. A API pública é consultada quando o app abre a previsão.

> **Importante sobre o wrapper:** este ZIP inclui a configuração do Gradle Wrapper, mas **não** o arquivo binário `gradle-wrapper.jar`, pois não foi possível obtê-lo neste ambiente. O Android Studio pode importar o projeto e usar uma instalação de Gradle 8.9; alternativamente, gere o wrapper com `gradle wrapper --gradle-version 8.9` em uma máquina que tenha Gradle instalado.

## 8. Testes manuais recomendados
- Abrir pela primeira vez: Splash → introdução → Início.
- Marcar "Não mostrar novamente", fechar/reabrir: Splash → Início.
- Desmarcar a opção: introdução aparece na abertura seguinte.
- Pesquisar "Santos" na lista e abrir os detalhes.
- Favoritar e desfavoritar cidade; fechar/reabrir e confirmar persistência.
- Abrir Previsão e trocar cidade; verificar carregamento e dados recebidos.
- Desligar internet e clicar em atualizar; verificar erro e botão de nova tentativa.
- Navegar entre as 5 telas principais e verificar a barra inferior.

## 9. Prints das telas
**A inserir após rodar o aplicativo no emulador:**
- `docs/prints/01-splash.png`
- `docs/prints/02-onboarding.png`
- `docs/prints/03-inicio.png`
- `docs/prints/04-previsao.png`
- `docs/prints/05-dicas.png`
- `docs/prints/06-salvos.png`
- `docs/prints/07-sobre.png`
- `docs/prints/08-detalhe.png`

Não foram inventadas capturas de execução. Os prints acima devem ser produzidos no Android Studio antes da entrega.

## 10. Links de entrega (preencher pela equipe)
- **GitHub:** [INSERIR LINK PÚBLICO DO REPOSITÓRIO]
- **Vídeo pitch (YouTube público; até 5 minutos):** [INSERIR LINK]

O vídeo deve apresentar integrantes, problema, proposta, arquitetura e demonstração do aplicativo em funcionamento, dedicando ao menos metade do tempo ao software e infraestrutura, conforme orientações da GS.

## 11. Limitações
- O MVP exibe cinco cidades predefinidas; não realiza geolocalização.
- Consulta depende de conexão com internet e disponibilidade da API.
- Não envia notificações push, não detecta enchentes e não substitui orientações da Defesa Civil.
- O projeto foi estruturado para Android Studio, mas **não foi compilado nem executado neste ambiente** por ausência de Android SDK/Gradle; valide o build no Android Studio antes de submeter.
