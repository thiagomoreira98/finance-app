# Guia de Configuração e Uso - App de Gestão Financeira KMP + Google Apps Script

## Architecture Overview

- **Mobile App**: Kotlin Multiplatform (Compose Multiplatform para Android / iOS)
- **Backend / DB**: Google Apps Script + Planilha do Google Sheets

---

## 1. Configurando a Planilha e o Google Apps Script (Servidor / Banco de Dados)

1. Acesse o [Google Sheets](https://sheets.google.com) e crie uma nova planilha (ex: `Gestão Financeira`).
2. No menu superior, clique em **Extensões** -> **Apps Script**.
3. Copie o conteúdo do arquivo [`Code.gs`](file:///Users/thiagomoreira/workspace/personal/finance_app/google_app_scripts/Code.gs) e cole no editor do Apps Script.
4. Execute a função `setupSheets()` uma vez no console do Apps Script para criar automaticamente as 3 abas principais com seus respectivos cabeçalhos:
   - **Cartão de Crédito**: `id`, `nome`, `data`, `valorTotal`, `parcelas`, `cartao`
   - **Custos Fixos**: `id`, `nome`, `valor`, `diaVencimento`, `categoria`
   - **Rendas**: `id`, `origem`, `valor`, `diaRecebimento`
5. No canto superior direito, clique em **Implantar (Deploy)** -> **Nova implantação**.
6. Escolha o tipo **Web app (App da Web)**:
   - **Executar como**: *Sua conta (Me)*
   - **Quem tem acesso**: *Qualquer pessoa (Anyone)*
7. Copie a **URL da Web app gerada** (ex: `https://script.google.com/macros/s/.../exec`).

---

## 2. Estrutura do App Mobile KMP

A estrutura completa do projeto Kotlin Multiplatform está localizada em:
- [`finance_app`](file:///Users/thiagomoreira/workspace/personal/finance_app)

### Componentes principais criados:
1. **Modelos de Dados** (`Models.kt`):
   - `CreditCardItem`: `id`, `nome`, `data`, `valorTotal`, `parcelas`, `cartao`
   - `FixedCostItem`: `id`, `nome`, `valor`, `diaVencimento`, `categoria`
   - `IncomeItem`: `id`, `origem`, `valor`, `diaRecebimento`
   - `FinancialSummary`: Projeção de totais e saldo mensal.
2. **Navegação & UI**:
   - `FloatingBottomNavBar.kt`: Barra de navegação flutuante inferior para alternar entre as 5 telas.
   - `Screen.kt`: Enums de controle das telas:
     - **Home**
     - **Cartão de Crédito** (Adicionar/Editar/Remover compras)
     - **Custos Fixos** (Adicionar/Editar/Remover despesas fixas)
     - **Rendas** (Adicionar/Editar/Remover fontes de receita)
     - **Resumo Diário** (Acompanhamento da projeção de gastos do mês)

---

## 3. Próximos Passos recomendados

1. **Instalar o JDK 17+ e Android Studio** na sua máquina para compilação e testes no emulador/dispositivo físico.
2. **Conectar a Web App URL**: Inserir a URL do Web App gerada no cliente HTTP Ktor do aplicativo.
3. Abrir a pasta `finance_app` no Android Studio e rodar a aplicação.
