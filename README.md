# 📚 Smart Study Planner

**Smart Study Planner** is a modern Android productivity app built with **Kotlin + Jetpack Compose** to help students organize subjects, manage tasks, track study sessions, set goals, and monitor their learning progress — all in one place.

### 🔗 Links

**[▶ Live Demo](https://appetize.io/app/b_pmpdty2sgvds7l25womwfnakoi)**   •   **[GitHub Repository](https://github.com/DevWithMaryam?tab=repositories)**


---

## ✨ Features

### 📚 Subjects & Topics

* Organize subjects and topics in a structured hierarchy.
* Manage study material by subject and topic.

### ✅ Task Management

* Create tasks with **priority, due date, and status**.
* Link tasks to subjects and optional topics.
* **Search, filter, and sort** tasks.
* Track pending and completed tasks.

### ⏱️ Study Sessions

* Start a **live study timer** for focused sessions.
* Record and store study time.
* View previous study sessions.
* Associate sessions with subjects and topics.

### 🎯 Study Goals

* Create **daily, weekly, or subject-specific goals**.
* Set target study time.
* Automatically calculate goal progress.

### 🏠 Home Dashboard

* Today's tasks
* Upcoming tasks
* Today's study time
* Goal progress
* Quick access to important study activities

### 📅 Calendar

* Monthly calendar view.
* View tasks and study sessions by date.

### 📊 Progress & Statistics

* Weekly study-time chart.
* Monthly study totals.
* Subject-wise study breakdown.
* Task completion statistics.

### 🔔 Notifications & Settings

* Reminders for tasks due today and overdue tasks.
* **Light / Dark / System** theme.
* Notification toggle.
* Daily goal preference using DataStore.

---

## 🛠️ Tech Stack

| Category                 | Technology                             |
| ------------------------ | -------------------------------------- |
| **Language**             | Kotlin                                 |
| **UI**                   | Jetpack Compose + Material 3           |
| **Architecture**         | MVVM + Repository Pattern              |
| **Database**             | Room                                   |
| **Preferences**          | Jetpack DataStore                      |
| **Async**                | Kotlin Coroutines + Flow / StateFlow   |
| **Dependency Injection** | Hilt                                   |
| **Navigation**           | Navigation Compose                     |
| **Background Work**      | WorkManager                            |
---

## 🏗️ Architecture

The app follows **MVVM + Repository Pattern**:

```text
Compose UI
    ↓
ViewModel
    ↓
Repository
    ↓
Room Database / DataStore
```

* **UI Layer** — Jetpack Compose screens and reusable components.
* **ViewModel Layer** — manages UI state using `StateFlow` and handles user actions.
* **Repository Layer** — centralizes data operations and separates the UI from data sources.
* **Data Layer** — Room Database for app data and DataStore for preferences.

---

## 🗄️ Database Design

The app uses **Room Database with 5 entities**:

```text
Subject
 ├── Topic
 ├── Task ────────────> Topic (optional)
 ├── StudySession ────> Topic (optional)
 └── StudyGoal (optional)
```

| Entity         | Purpose                                     |
| -------------- | ------------------------------------------- |
| `Subject`      | Stores subjects                             |
| `Topic`        | Stores topics under subjects                |
| `Task`         | Stores tasks, priorities, dates, and status |
| `StudySession` | Stores recorded study time                  |
| `StudyGoal`    | Stores target-based study goals             |

**Relationships:** Topics belong to Subjects, while Tasks and Study Sessions belong to a Subject and can optionally reference a Topic. Goals can optionally be associated with a Subject.

---

## 📂 Project Structure

```text
com.maryam.smartstudyplanner
│
├── data/
│   ├── local/
│   │   ├── dao/
│   │   ├── database/
│   │   └── entity/
│   └── repository/
│
├── di/
├── navigation/
│
├── ui/
│   ├── home/
│   ├── subjects/
│   ├── tasks/
│   ├── calendar/
│   ├── progress/
│   ├── study/
│   ├── goals/
│   ├── settings/
│   ├── theme/
│   └── components/
│
├── util/
├── worker/
└── MainActivity.kt
```

---

## 💡 Key Technical Highlights

* **Custom Progress Chart** — built with native Compose layouts instead of an external charting library.
* **Custom Calendar** — implemented using Compose layouts and date calculations without a third-party calendar library.
* **Hilt + WorkManager** — task reminders use `@HiltWorker` with dependency injection.
* **Room + DataStore** — Room handles application data while DataStore persists user preferences.
* **Concrete Repositories** — used because the app currently has a single data source, avoiding unnecessary abstraction.

---

## 🚀 Getting Started

```bash
**[Git clone](https://github.com/DevWithMaryam/Smart-Study-Planner)**
```

Open the project in **Android Studio**, allow Gradle to sync, and run it on an emulator or physical Android device.

**Minimum SDK:** 26 (Android 8.0+)

---

## 👩‍💻 Author

### Maryam Akram

**Android Developer**

---
