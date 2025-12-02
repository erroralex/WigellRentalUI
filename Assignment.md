# 🏕️ Wigell Rental UI - Medlemsklubb & Uthyrningssystem

***

## Bakgrund

Du har fått en provanställning på Wigellkoncernen och som andra uppdrag ska du bygga en Java-applikation med grafiskt gränssnitt som modellerar en medlemsklubb med uthyrning av utrustning (t.ex. fordon, verktyg, sportprylar eller liknande beroende på ditt val av applikation).

***

## ⚙️ Tekniska Krav

### Klassdesign

Klasser som ska finnas i applikationen:

* **Member** (id, namn, status/level, historik)
* **Item** (abstract) $\rightarrow$ Subklasser som ex. Vehicle, Tool, SportGear eller liknande beroende på ditt val av applikation. Var och en av klasserna ska innehålla sina unika attribut och metoder.
* **Rental** (kopplar member och item för en viss tidsperiod)
* **PricePolicy** (interface) + konkreta strategier, ex. standard, student, premium.
* **Inventory** och **MemberRegistry**
* **RentalService** och **MembershipService** ska innehålla affärslogiken

### User Interface (JavaFX)

Applikationen ska ha ett grafiskt gränssnitt i JavaFX. Användaren ska kunna:

* Lägga till, söka och ändra medlemmar.
* Lägga till och ändra items.
* Lista och filtrera items.
* Boka och avsluta uthyrningar.
* Se summeringar (ex intäkter eller antal aktiva uthyrningar).
* Data ska presenteras i t.ex. `TableView`, `ListView` eller liknande komponenter.
* Fel och meddelanden till användaren ska visas på ett begripligt sätt.

**Obs:** Det är helt ok att utgå från ditt föregående projekt, men du förväntas refaktorisera och bygga vidare.

***

## 🌟 Bedömningskriterier

### Graden Godkänd (G)

| Kriterium | Beskrivning |
| :--- | :--- |
| **Körbarhet** | Körbar applikation som körs tills användaren väljer att avsluta. |
| **Klassstruktur** | Korrekt uppförda klasser samt användande av objekt och metoder. |
| **Arv & Polymorfism** | `Item` som basklass + minst två konkreta subklasser. `PricePolicy` som interface + minst två implementationer. |
| **Inkapsling** | Privata attribut samt nödvändiga getters/setters. |
| **Ansvarsdelning** | Enkel ansvarsdelning mellan klasser/logik samt huvudprogrammet. |
| **Collections/Streams** | `Collections` och `streams` ska användas där det är lämpligt. |
| **Persistens (Läs)** | Vid start ska `inventory` och `memberRegistry` läsas in från fil och populera aktuell tabell. |

### Graden Väl Godkänd (VG)

Samtliga krav på G är uppfyllda. Dessutom gäller:

| Kriterium | Beskrivning |
| :--- | :--- |
| **Felhantering** | Robust felhantering på metoder där så är lämpligt. |
| **Kodkvalitet** | Hög kodkvalitet med väl namngivna klasser och metoder. |
| **Persistens (Spara/Läs)** | Applikationens data (Medlemmar och items) ska kunna sparas till fil och via ett knapptryck läsas in och populera önskad tabell. |
| **Multithreading** | En **separat tråd** ska skapas och användas för att köra minst en ytterligare funktion. Val av funktion är valfri, men exempel kan vara – en autosave som sparar data med jämna mellanrum i bakgrunden. – en timer som visar hur länge applikationen varit aktiv. Tråden ska startas och stängas på ett kontrollerat sätt. Dvs ingen evig tråd… |
| **Helhetsintryck** | Helheten ska ge intrycket av ett ”litet men seriöst system” snarare än ”bara en skolövning”. |
| **OOP-Tydlighet** | Koden ska vara tydligt objektorienterad. |
| **Deadline** | Uppgiften ska lämnas in innan deadline. |