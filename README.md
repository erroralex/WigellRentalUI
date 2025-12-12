# Camping Club Rental Management System

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-Code--First-4285F4?style=for-the-badge&logo=java&logoColor=white)
![CSS](https://img.shields.io/badge/CSS-Styling-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![Jackson](https://img.shields.io/badge/Persistence-Jackson_JSON-2f2f2f?style=for-the-badge&logo=json&logoColor=white)

A comprehensive desktop application for managing rental inventories, membership data, and financial reporting for a camping club.

This project was built to demonstrate **Object-Oriented Design patterns**, **layered architecture**, and **programmatic UI development** (Pure JavaFX without FXML).

---

## 📸 Interface

| Dashboard & Themes | Data Management |
|:---:|:---:|
| <img src="src/main/java/com/nilsson/camping/screenshots/screenshot_profits.png" width="400" alt="Profits Dark Mode"> | <img src="src/main/java/com/nilsson/camping/screenshots/screenshot_ui.png" width="400" alt="Inventory Grid"> |
| *Dark Mode Dashboard with Charting* | *Inventory Management Table* |

<details>
<summary><b>View More Screenshots</b></summary>
<br>

| Login Screen | Light Mode |
|:---:|:---:|
| <img src="src/main/java/com/nilsson/camping/screenshots/screenshot_login.png" width="400" alt="Login"> | <img src="src/main/java/com/nilsson/camping/screenshots/screenshot_lightmode.png" width="400" alt="Light Mode"> |

</details>

---

## 🛠️ Technical Architecture

The application follows a **Model-View-Service** architecture to ensure separation of concerns. The UI is decoupled from business logic, and data persistence is handled via a dedicated layer.

### Design Patterns Implemented
* **Strategy Pattern:** Used in the `PricingPolicy` engine. Calculation logic (`Standard`, `Student`, `Premium`) is injected at runtime, allowing for flexible pricing updates without modifying the core rental service.
* **Singleton Pattern:** Ensures thread-safe, single-instance access to data registries (`Inventory`, `MemberRegistry`) and the `LanguageManager`.
* **Observer Pattern:** Utilized extensively in the UI to update charts and tables in real-time when underlying data models change.
* **Polymorphism:** The inventory system treats `Vehicle` and `Gear` as interchangeable `IRentable` items, simplifying the rental logic.

### Technology Stack
* **Core:** Java 17+, JavaFX (Programmatic DOM, No FXML)
* **Persistence:** Jackson (JSON Data Binding)
* **Build Tool:** Maven
* **UI Assets:** Ikonli (FontAwesome Pack), Custom CSS Theming

---

## ✨ Key Features

* **Inventory Management:** Unified system for tracking vehicles (RVs, Caravans) and gear (Tents, Backpacks).
* **Smart Pricing:** Automated cost calculation based on membership tiers (e.g., Student discounts, Premium surcharges).
* **Financial Reporting:** Live bar charts visualizing income trends over a 14-day rolling window.
* **Localization:** Instant runtime switching between English and Swedish `ResourceBundles`.
* **Dynamic Theming:** CSS-driven Dark/Light mode toggle.

---

## 🚀 Getting Started

To run the application locally:

1.  **Clone the repository**
2.  **Build with Maven:**
    ```bash
    mvn clean install
    ```
3.  **Run:**
    ```bash
    mvn javafx:run
    ```

---

## 🔒 Default Login
The system includes a simulated security gatekeeper. Use the following credentials to access the dashboard:

* **Username:** `admin`
* **Password:** `0000`

---

## 📂 Project Structure

```
src/main/java/com/nilsson/camping
├── app/            # Entry point, Session & Language management
├── data/           # JSON Persistence & File I/O
├── model/          # Entities & Strategy Interfaces (IPricePolicy)
├── service/        # Business Logic & Calculation Layer
└── ui/             # Pure JavaFX Views & Custom Components
    ├── dialogs/    # Modal forms
    └── views/      # Main application screens
```
---

## 📜 License

This project was developed for educational purposes as part of a System Development course. Distributed under the **MIT License**.

---
