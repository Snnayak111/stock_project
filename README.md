# StockProject

StockProject is an Android application for managing and tracking stock wishlists. It uses modern Android development practices, including Kotlin, Jetpack libraries, Room database, and dependency injection with Hilt.

## Features
- Two main tabs: *Stocks* (Explore) and *Watchlist*
- *Explore Screen*: View Top Gainers and Losers in a grid of cards with stock/ETF information
- *Watchlist Screen*: Manage all your watchlists, with an empty state when no watchlists are present
- *Product (Details) Screen*: View detailed stock/ETF info, including a line graph of prices; add/remove stocks to/from watchlists with dynamic icon updates
- *Add to Watchlist Popup*: Add a new watchlist or select an existing one to add stocks; persistent storage of user watchlists
- *View All Screen*: Paginated list of all stocks in a section (e.g., all gainers/losers)
- *API Integration*: Fetch data from Alpha Vantage (Top Gainers/Losers, Company Overview, Ticker Search)
- *Loading, Error, and Empty States*: User-friendly handling for all network and data states
- *Caching*: API responses are cached with expiration for better performance
- *Modern UI*: Material Design components and responsive layouts
- *Well-structured Codebase*: Follows a modular and maintainable folder structure

## Project Structure
- app/ - Main Android application module
  - data/ - Database entities, DAOs, and data sources
  - domain/ - Repository interfaces and business logic
  - ui/ - User interface components
  - di/ - Dependency injection modules

## Getting Started

### Prerequisites
- Android Studio (latest version recommended)
- JDK 17 or higher
- Gradle (wrapper included)

### Setup
1. Clone the repository:
   sh
   git clone <your-repo-url>
   
2. Open the project in Android Studio.
3. Let Gradle sync and download dependencies.
4. Connect an Android device or start an emulator.
5. Click *Run* to build and launch the app.

## Build & Run
- Use the included gradlew or gradlew.bat scripts to build the project:
  sh
  ./gradlew assembleDebug
  
- To run tests:
  sh
  ./gradlew test
  

## Dependencies
- Kotlin
- AndroidX
- Room
- Hilt (Dagger)
- Jetpack Compose

## 🛠 Libraries & Techniques Used

- *Kotlin* – Main programming language for Android development.
- *Android Jetpack Libraries* – Architecture Components, Lifecycle, Navigation, and more for robust app structure.
- *Room* – Local database solution for storing and managing wishlist and stock data.
- *Hilt (Dagger)* – Dependency injection for modular, scalable, and testable code.
- *ViewModel & LiveData* – Lifecycle-aware data management and UI updates.
- *Coroutines* – Asynchronous programming for smooth network and database operations.
- *Retrofit* – For making API calls to fetch stock data (if used).
- *Material Design* – Modern UI components and design system.
- *Custom Error & Loading Handling* – User-friendly messages and progress indicators for all network/database states.
- *Modular Architecture* – Clear separation of concerns using data, domain, and UI layers.

**Example:**

## 📱 Example: Screenshots

| Explore Screen | Details Screen | Watchlist Screen | Search Screen |
|----------------|----------------|------------------|----------------|
| ![Explore Screen](StockProject/app/src/main/res/WhatsApp%20Image%202025-06-30%20at%2021.56.19_1d1af6c7.jpg) | ![Details Screen](StockProject/app/src/main/res/WhatsApp%20Image%202025-06-30%20at%2021.56.19_3509ba77.jpg) | ![Watchlist Screen](StockProject/app/src/main/res/WhatsApp%20Image%202025-06-30%20at%2021.56.19_dd95a5ee.jpg) | ![Search Screen](StockProject/app/src/main/res/WhatsApp%20Image%202025-06-30%20at%2021.56.18_91d03b46.jpg) |


### Demo Video

[App Walkthrough (Google Drive)](https://drive.google.com/file/d/15R7rIVcGtnH0qLOMwaj4sOyOtzbBtZkB/view?usp=drivesdk )

### APK Download

[Download APK (Google Drive)](https://drive.google.com/file/d/15OX4BCfcnmE1FBcQhKk-nVgzUSer57j2/view?usp=drivesdk)
