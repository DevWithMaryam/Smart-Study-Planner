# Smart Study Planner

A polished, portfolio-quality Android application built entirely in **Kotlin** with **Jetpack Compose**, designed to help students organize subjects, manage tasks, track study sessions, set goals, and monitor their learning progress.

## Overview

Smart Study Planner is a real-world productivity app for students and self-learners. It goes beyond a basic to-do list by combining subject/topic organization, task management with priorities and deadlines, live study session tracking, goal setting with progress calculation, a calendar view, and detailed statistics — all backed by a local Room database and built with modern Android architecture.

## Problem Statement

Students often juggle multiple study tools — a to-do app for tasks, a separate timer for study sessions, and no clear way to see progress toward their goals. Smart Study Planner consolidates all of this into a single, cohesive app.

## Features

- **Subjects & Topics** — organize your study material hierarchically
- **Tasks** — add tasks with priority, due date, and status; search, filter, and sort
- **Study Sessions** — live timer to record focused study time, with session history
- **Study Goals** — set daily, weekly, or subject-specific goals with automatic progress tracking
- **Home Dashboard** — at-a-glance view of today's tasks, upcoming tasks, study time, and goal progress
- **Calendar** — month view showing tasks and study sessions by date
- **Progress & Statistics** — weekly study chart, monthly totals, subject-wise breakdown, task stats
- **Notifications** — daily reminders for tasks due today and overdue tasks
- **Settings** — Light/Dark/System theme, notification toggle, daily goal preference (persisted via DataStore)
- **Material 3 Design** — consistent theming, dark mode support, empty states, and smooth list animations

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM + Repository Pattern |
| Local Database | Room |
| Preferences | Jetpack DataStore |
| Async | Kotlin Coroutines + Flow/StateFlow |
| Dependency Injection | Hilt |
| Navigation | Navigation Compose |
| Background Work | WorkManager (Hilt-integrated) |
| Testing | JUnit, MockK, Turbine, Coroutines Test |

## Architecture

The app follows a clean **MVVM + Repository** architecture:

Presentation (Compose UI) → ViewModel → Repository → Room / DataStore

- **UI Layer** — Jetpack Compose screens, stateless where possible, observing `StateFlow` from ViewModels
- **ViewModel Layer** — holds UI state, exposes `StateFlow`, delegates all data operations to Repositories
- **Repository Layer** — single source of truth abstraction over Room DAOs and DataStore
- **Data Layer** — Room database (5 entities with foreign key relationships) and Preferences DataStore

## Database Design

Five Room entities with the following relationships:

Subject ──< Topic
Subject ──< Task >── Topic (optional)
Subject ──< StudySession >── Topic (optional)
Subject ──< StudyGoal (optional)


- `subjects` — subject records
- `topics` — belong to a subject (CASCADE delete)
- `tasks` — belong to a subject, optionally linked to a topic (SET NULL on topic delete)
- `study_sessions` — recorded study time, linked to subject/topic
- `study_goals` — target-based goals with a date range, optionally scoped to a subject
## Project Structure
com.maryam.smartstudyplanner
├── data/
│ ├── local/
│ │ ├── dao/
│ │ ├── database/
│ │ └── entity/
│ └── repository/
├── di/
├── navigation/
├── ui/
│ ├── home/
│ ├── subjects/
│ ├── tasks/
│ ├── calendar/
│ ├── progress/
│ ├── study/
│ ├── goals/
│ ├── settings/
│ ├── theme/
│ └── components/
├── util/
├── worker/
└── MainActivity.kt




## Key Technical Decisions

- **No external charting library** — the weekly progress chart is built with plain Compose layouts (`fillMaxHeight(fraction)`), avoiding an unnecessary dependency for a simple bar chart.
- **No custom calendar library** — the month-view calendar grid is hand-built using nested `Row`/`Column` composables and `Calendar` date math.
- **Hilt + WorkManager** — task reminders use `@HiltWorker` so the background worker can access the Repository layer directly, following the same DI pattern as the rest of the app.
- **Repository without interfaces** — since the app currently has a single data source (Room/DataStore), repositories are concrete classes rather than interface + impl pairs, avoiding premature abstraction.

## Setup Instructions

1. Clone the repository:
```bash
   git clone https://github.com/DevWithMaryam/SmartStudyPlanner.git
```
2. Open the project in **Android Studio** (Ladybug or newer recommended).
3. Let Gradle sync automatically (all dependencies are managed via version catalog in `gradle/libs.versions.toml`).
4. Run the app on an emulator or physical device (minSdk 26 / Android 8.0+).

## How to Run

- Open `MainActivity.kt` and click **Run** ▶️, or use `Shift+F10`.
- On first launch, grant the notification permission when prompted (Android 13+).

## Future Improvements

- Move all UI strings to `strings.xml` for localization support
- Add Room database migrations and schema export for production readiness
- Add subject-specific study time filtering for goals (currently goals track total study time within their date range)
- Add cloud backup/sync as an optional feature
- Expand test coverage to additional ViewModels and UI tests with Compose Testing

## Author

**Maryam Akram**
GitHub: [@DevWithMaryam](https://github.com/DevWithMaryam)
LinkedIn: [linkedin.com/in/maryam-akram-2b183b338](https://linkedin.com/in/maryam-akram-2b183b338)
