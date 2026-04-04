# Black-box test scenarios — Banking System

Use this as a checklist. **Expected** = what a user should see or what the system should do. No source code knowledge required.

**Environment:** Run from project root so the `data` file is used. Start from a known state when needed (backup/delete `data` for a clean bank).

---

## Security & privacy (non-functional)

| # | Scenario | Expected |
|---|-----------|----------|
| 1 | Password field | Characters are hidden (password field). |
| 2 | After login attempt | Password box is cleared; password array zeroed in memory after use. |
| 3 | Failed login | Message does **not** say whether username or password was wrong (reduces guessing). |
| 4 | Save failure | Console shows a **generic** message (no stack trace with file paths to the user). |
| 5 | Receipt / success dialogs | Short **privacy** reminder at the bottom; holder name shown in shortened form (e.g. `Jane D.`). |
| 6 | Trade licence (current account) | Confirm and success show **only tail** of reference (e.g. `…1234`), not necessarily full string. |
| 7 | Account list window | Banner warns list is **confidential**. |
| 8 | Wrong account Id on deposit/withdraw | Generic “no match” message; does **not** need to echo the typed Id (shoulder-surfing). |

---

## Login

| # | Scenario | Expected |
|---|-----------|----------|
| L1 | Empty username & password | Prompt to enter both. |
| L2 | Empty username only | Prompt for username. |
| L3 | Empty password only | Prompt for password. |
| L4 | `admin` / `admin` | Login window hides; main menu appears (no success popup). |
| L5 | Wrong username or password | Red status text (vague wording, not “wrong password” only); password cleared; user can retry. |
| L6 | Enter key in password field | Same as clicking Login. |

---

## Main menu

| # | Scenario | Expected |
|---|-----------|----------|
| M1 | Open Deposit while Deposit already open | Clear message: deposit window already open. |
| M2 | Open Withdraw while Withdraw already open | Message refers to **withdrawal** window. |
| M3 | Open Display list while already open | Message refers to **account list**. |
| M4 | Open Add Account while already open | Message refers to **add-account** window. |
| M5 | **Save and Log out** | Data saved; tool windows hidden; menu hides; login screen returns with **no** dialog. |
| M6 | Close main window (×) | Same as M5 (save, return to login). App stays running until login window is closed. |

---

## Add account (chooser)

| # | Scenario | Expected |
|---|-----------|----------|
| A0 | Open Savings / Current / Student when that form already open | Friendly “already open” message (from Add Account chooser). |

---

## Add savings account

| # | Scenario | Expected |
|---|-----------|----------|
| S1 | Empty name | Ask for customer name. |
| S2 | Name longer than 120 chars | Validation error. |
| S3 | Empty balance or empty max withdraw | Ask for both numbers. |
| S4 | Non-numeric balance or limit | Clear invalid-number message. |
| S5 | Balance &lt; 2000 | Minimum balance message. |
| S6 | Balance or limit ≤ 0 | Must be positive. |
| S7 | Balance or limit = infinity / huge | Demo cap message (`ClientSafeInput` limit). |
| S8 | Confirm **No** | No account created; form stays. |
| S9 | Confirm **Yes**, valid data | Success: new **5-digit Id**, masked name, privacy footer; list can refresh. |
| S10 | 100 accounts already in bank | “Maximum number of accounts” style message. |

---

## Add current account

| # | Scenario | Expected |
|---|-----------|----------|
| C1 | Missing name or licence | Ask for both. |
| C2 | Licence &gt; 80 chars | Too long message. |
| C3 | Empty balance | Balance required. |
| C4 | Balance &lt; 5000 | Minimum 5,000 message. |
| C5 | Non-numeric balance | Invalid input message. |
| C6 | Success | Id + masked name + licence **tail** only + privacy note. |

---

## Add student account

| # | Scenario | Expected |
|---|-----------|----------|
| T1 | Missing name or institution | Ask for both. |
| T2 | Institution &gt; 120 chars | Too long. |
| T3 | Balance &lt; 100 | Minimum 100 message. |
| T4 | Success | Id + masked name + privacy note (institution not repeated on success in full if long—confirm may truncate). |

---

## Transaction report (per account)

| # | Scenario | Expected |
|---|-----------|----------|
| TR1 | Empty Id + View | Prompt for Id. |
| TR2 | Invalid Id format (not 5 digits) | Format message. |
| TR3 | Valid format, unknown Id | Account not found. |
| TR4 | Valid account with activity after update | Table: #, date/time, type (Opening / Deposit / Withdrawal), amount, balance after; summary with masked name. |
| TR5 | Account from an old `data` file (no log) | First open report (or first deposit/withdraw after upgrade) adds **Balance on file (no earlier detail)**; real deposits/withdrawals append after it. |

---

## Display account list

| # | Scenario | Expected |
|---|-----------|----------|
| D1 | No accounts in bank | Empty-state message + hint to add account and reopen list. |
| D2 | One or more accounts | Rows with name, Id, balance, type; privacy banner visible. |

---

## Deposit

| # | Scenario | Expected |
|---|-----------|----------|
| P1 | Empty account Id | Account number required message. |
| P2 | Id not exactly 5 digits | Format help (e.g. `45231` only). |
| P3 | Id valid format but unknown | No account matches (generic). |
| P4 | Empty amount | Amount required. |
| P5 | Amount `0` or negative | Must be &gt; 0. |
| P6 | Amount not a number (`abc`) | Invalid number message. |
| P7 | Amount with commas `1,000.50` | Accepted (commas stripped). |
| P8 | Amount above demo max | Cap message with suggestion to split. |
| P9 | Confirm **No** | No deposit; fields unchanged. |
| P10 | Confirm **Yes**, valid | Success receipt: Id, masked name, amounts, balances, privacy footer. |
| P11 | Multiple deposits same account | Each time: new “before/after” balances correct. |

---

## Withdraw

| # | Scenario | Expected |
|---|-----------|----------|
| W1 | Same as deposit for empty / bad Id / bad amount | Matching validation style. |
| W2 | Withdraw more than balance | Clear message with numbers (existing `MaxBalance` text). |
| W3 | Withdraw breaking minimum balance | Message explains min balance and max allowed withdrawal. |
| W4 | Savings: single withdrawal &gt; per-tx limit | Per-transaction limit message. |
| W5 | Amount `0` or empty | Specific prompts. |
| W6 | Confirm **Yes**, valid | Success receipt with before/after + privacy footer. |
| W7 | Multiple withdrawals | Each receipt reflects updated balance. |

---

## Persistence (`data` file)

| # | Scenario | Expected |
|---|-----------|----------|
| R1 | Create accounts, Exit | `data` file exists in cwd. |
| R2 | Restart app | Previous accounts still there. |
| R3 | Missing `data` | App starts with empty bank (no crash). |
| R4 | Corrupt `data` (manual edit) | Safe fallback to new bank or error on read—**re-test** after simulating corruption. |

---

## Regression quick path (smoke)

1. Login → Add savings (valid) → note Id.  
2. Deposit small amount → receipt balances make sense.  
3. Withdraw small amount → receipt balances make sense.  
4. Display list → row matches.  
5. Exit → relaunch → data still there.

---

*Maintain this file when you add features (e.g. new account types or new limits).*
