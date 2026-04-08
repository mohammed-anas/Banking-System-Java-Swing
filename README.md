# Banking System (ApexTrust Retail Banking — demo UI)

Desktop banking demo built with Java Swing, with an **enterprise-style console** (navy / gold theme, header banner, optional logo). You can add savings, current, and student accounts; deposit and withdraw; list accounts; and persist all data to a local file.


## Requirements

- **JDK 17 or newer** (the project was verified with OpenJDK 25). Install a JDK and ensure `javac` and `java` are on your `PATH`.

Check:

```bash
javac -version
java -version
```

**After `git clone`:** the repo has **source only** — run **`build.bat`** (Windows) or compile with the commands below, then run the app. The `bin/` folder and local `data` file are not committed; they appear after you build and run.

## Build (command line)

From the **project root** (`BankingSystem`), compile all sources into `bin/`.

`-encoding UTF-8` is required so the compiler reads source files correctly on **Windows** (default system encoding is often not UTF-8). It is safe on macOS and Linux as well.

**Windows (simplest):** double-click or run **`build.bat`** from Explorer or Command Prompt. It changes to the project folder, compiles with UTF-8, and writes classes under `bin\`.

**macOS / Linux:**

```bash
cd /path/to/BankingSystem
mkdir -p bin
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d bin -sourcepath src @sources.txt
rm sources.txt
```

**Windows (Command Prompt, from project root):**

```cmd
mkdir bin 2>nul
dir /s /b src\*.java > sources.txt
javac -encoding UTF-8 -d bin -sourcepath src @sources.txt
del sources.txt
```

## Run (end to end)

Always run from the **project root** so the data file `data` is created and loaded in this folder.

**Windows:** run **`run.bat`** (it sets the working directory to the project folder and uses `java -cp "bin;src" Application`). Or from a shell:

```cmd
cd C:\path\to\BankingSystem
java -cp "bin;src" Application
```

**macOS / Linux:**

```bash
cd /path/to/BankingSystem
java -cp bin:src Application
```

### Optional menu image

Optional branding images (classpath must include `src` so resources load — **`run.bat` already does this**):

- **`src/img/banking-logo.png`** — preferred logo in the header and menu (square PNG, ~200–300px works well).
- **`src/img/1.png`** — legacy decorative image; used if the banking logo is missing.

If you run without `src` on the classpath (`java -cp bin Application` only), images are skipped and a **vector shield logo** is drawn automatically in the header and menu.

## Using the app

1. **Login:** Enter **Username** `admin` and **Password** `admin`, then click **Sign in**.
2. **Main menu:** Choose **Add Account** (savings, current, or student), **Deposit To Account**, **Withdraw From Account**, **Update withdrawal limit** (savings/student per-transaction cap), **Display Account List**, or **Transaction report** (per-account history of opening balance, deposits, and withdrawals).
3. **Account numbers:** Each new account gets a random **5-digit Id** (digits only—this is what you type for deposit and withdraw). After you add an account, the success dialog shows the new Id. You can also open **Display Account List** (treat it as confidential).
4. **Save and log out:** Use **Save and Log out** on the main menu, or close the main window with **×**—both write the `data` file, close open tool windows, hide the menu, and return to the **Sign in** screen (no popup). Quit the app by closing the login window.

## Testing

See **[TESTING.md](TESTING.md)** for black-box scenarios (login, all account types, deposit/withdraw edge cases, privacy expectations, persistence).

## Security note (demo)

This is a **student / demo** app: credentials are fixed (`admin`/`admin`), the `data` file holds balances and names in plain serialized form, and receipts may show partial identity for clarity. **Do not use real customer data or real secrets.** Protect the `data` file like any file with account information.

## Business rules (implemented)

| Account type | Minimum opening balance | Notes |
|--------------|-------------------------|--------|
| Savings      | 2000                    | Withdrawals cannot exceed the per-account maximum withdraw limit. |
| Current      | 5000                    | Requires a trade licence number. |
| Student      | 100                     | Minimum balance after withdrawals is 100; uses a default high withdraw cap (same chain as savings). |

## Data file

- **Location:** `data` in the current working directory (usually the project root when you run as above).
- **Format:** Java serialized `Bank` object. Delete `data` to start with an empty bank (backup first if you care about existing accounts).

## Eclipse

Import as an **existing project** (the repo includes `.project`). Ensure the JDK is configured; set the default output folder to `bin` if needed, and run `Application` as the main class. On **Windows**, set the workspace or project **Text file encoding** to **UTF-8** (Project → Properties → Resource) so sources with symbols like `©` and `—` stay correct. Add **`src`** to the run configuration’s classpath if you want the PNG logo; otherwise the vector logo is used.

## Project layout

- `src/Application.java` — entry point  
- `src/Bank/` — domain model (`Bank`, `BankAccount`, account types)  
- `src/GUI/` — Swing screens; `UITheme` / `EnterpriseLogo` — enterprise styling  
- `src/Bank/TransactionRecord.java` — one log line per account movement (includes a **legacy baseline** row for accounts loaded from old `data` files with no stored history)  
- `src/Data/FileIO.java` — load/save `data`  
- `src/Exceptions/` — checked exceptions used by operations  
