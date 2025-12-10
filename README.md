# 🏕️ Wigell Camping Admin Portal

**Wigell Camping Admin Portal** is a JavaFX application for managing a camping membership club. It handles inventory (vehicles & gear), rental tracking, member management, and financial reporting.

The application is built using **Pure JavaFX** (No FXML) with a focus on Object-Oriented Design principles and layered architecture.

---

## ✨ Key Features

### 1. Inventory & Rental Management
* **Inventory:** Manages a catalog of rentable items, handling both `Gear` (Tents, Backpacks) and `Vehicles` (RVs, Caravans) via a common interface.
* **Availability:** Items are automatically filtered based on their current rental status.
* **Rentals:** functionality to create new rentals, calculate costs based on days/membership, and process returns.

### 2. Pricing Strategies
The system uses the **Strategy Pattern** to calculate rental costs based on the member's tier:
* **Standard:** Regular daily rates.
* **Student:** 20% discount on all rentals.
* **Premium:** 20% surcharge (for extra services).

### 3. Membership Management
* **CRUD:** Add, edit, and remove members.
* **History:** View past rental history for each member.
* **Data:** Single source of truth for membership levels to prevent data conflicts.

### 4. Financial Tracking
* **Profits View:** Displays income for the current day and total historical revenue.
* **Chart:** A bar chart visualizes daily rental income over the last 14 days.

### 5. User Interface
* **JavaFX:** UI built entirely in Java code.
* **Themes:** Toggle between **Dark Mode** and **Light Mode**.
* **Localization:** Switch between **English** and **Swedish** text instantly.
* **Session Timer:** Tracks active session time in the window title bar.

---

## 🛠️ Technical Architecture

The project follows a **Model-View-Service** architecture.

### Design Patterns
* **Strategy Pattern:** Decouples pricing logic (`IPricePolicy`) from the rental service.
* **Singleton Pattern:** Used for data registries (`Inventory`, `MemberRegistry`) and the `LanguageManager`.
* **Observer Pattern:** Updates UI components (Charts, Tables) when data changes.

### Technology Stack
* **Language:** Java 8
* **GUI Framework:** JavaFX
* **Build Tool:** Maven
* **Persistence:** JSON (Jackson Library)
* **Icons:** Ikonli (FontAwesome)
  
---

## 🔑 Login Credentials

The system includes a login gatekeeper. Use the following credentials to access the dashboard:

* **Username:** `admin` (or any name you prefer)
* **Password:** `0000`

---

## 📂 Project Structure

```
src/
  └── com.nilsson.camping
      ├── app/            # Application entry, Session & Language management
      │   ├── MainApp.java
      │   ├── UserSession.java
      │   └── LanguageManager.java
      │
      ├── data/           # JSON Persistence & File handling
      │   ├── DataHandler.java
      │   └── ProfitsHandler.java
      │
      ├── model/          # Data entities
      │   ├── items/      # Inheritance: Item -> Vehicle/Gear
      │   ├── policies/   # Strategy Pattern: IPricePolicy implementations
      │   ├── registries/ # Singleton data stores (Inventory, Members)
      │   └── Member.java, Rental.java, DailyProfit.java
      │
      ├── service/        # Business Logic Layer
      │   ├── InventoryService.java
      │   ├── MembershipService.java
      │   ├── ProfitsService.java
      │   ├── RentalService.java
      │   └── SessionTimerService.java
      │
      └── ui/             # JavaFX User Interface
          ├── dialogs/    # Pop-up windows (Add/Edit forms)
          ├── views/      # Main content screens (Tabs)
          ├── CustomTitleBar.java
          ├── RootLayout.java
          ├── SideNavigation.java
          └── UIUtil.java

resources/
  ├── data/json/          # Database files (.json)
  │   ├── gear.json
  │   ├── members.json
  │   ├── profits.json
  │   ├── rentals.json
  │   └── vehicles.json
  │
  ├── i18n/               # Internationalization bundles
  │   ├── messages_en.properties
  │   └── messages_sv.properties
  │
  └── *.css, *.png        # Theme stylesheets & Images
```
---
