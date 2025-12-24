# 📚 Smart Study Session Timer (Pomodoro-Lite)

A console-based productivity tool built in Java that implements the Pomodoro Technique to help you stay focused and productive during study sessions.

## 🎯 Overview

This lightweight application helps you manage your study time effectively by breaking work into focused intervals (typically 25 minutes) separated by short breaks. It's designed to demonstrate core Java concurrency concepts while providing a practical productivity tool.

## ✨ Features

- **Configurable Sessions**: Customize study and break durations to fit your needs
- **Live Countdown Timer**: Real-time progress display with visual progress bars
- **Automatic Break Reminders**: Seamlessly transitions from study to break phases
- **Session Tracking**: Maintains history of all completed sessions
- **Pause/Resume Capability**: Flexibility to pause and resume sessions as needed
- **Detailed Summary**: View statistics including total study time and session history
- **Clean Console UI**: Intuitive menu-driven interface with visual feedback

## 🛠️ Technical Concepts Demonstrated

### Concurrency & Threading
- **ScheduledExecutorService**: Manages scheduled tasks with a thread pool
- **Multiple Thread Coordination**: Separate threads for countdown logic and UI updates
- **Thread-Safe Operations**: Proper handling of shared state across threads
- **Graceful Shutdown**: Clean resource management and thread termination

### Design Patterns
- **Single Responsibility**: Clear separation of concerns
- **Encapsulation**: Session data managed through inner classes
- **Resource Management**: Proper lifecycle handling of executor services

## 📋 Requirements

- Java 8 or higher
- IntelliJ IDEA (or any Java IDE)
- No external dependencies required

## 🚀 Getting Started

### Installation

1. Clone or download the repository
2. Open IntelliJ IDEA
3. Create a new Java project or use an existing one
4. Copy `SmartStudyTimer.java` into your source folder

### Running the Application

1. Open `SmartStudyTimer.java` in IntelliJ IDEA
2. Right-click on the file and select "Run 'SmartStudyTimer.main()'"
3. Or press `Shift + F10` (Windows/Linux) or `Control + R` (Mac)

### Usage

Once the application starts, you'll see a menu with the following options:

```
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
1. Configure Session
2. Start Session
3. Pause Session
4. Stop Session
5. View Session Summary
6. Exit
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
```

#### Quick Start Guide

1. **Configure Session** (Optional)
   - Set your preferred study duration (default: 25 minutes)
   - Set your preferred break duration (default: 5 minutes)

2. **Start Session**
   - Begins a study session with live countdown
   - Automatically starts break when study time completes

3. **Pause Session**
   - Temporarily pause the timer
   - Resume by selecting "Start Session" again

4. **Stop Session**
   - Completely stop the current session
   - Time remaining is displayed

5. **View Session Summary**
   - See total completed sessions
   - View total study and break time
   - Review recent session history

6. **Exit**
   - Displays final summary
   - Gracefully shuts down all threads

## 💡 Example Session

```
📚 STUDY | ████████████████░░░░ 80% | 05:00 remaining
```

The progress bar updates in real-time, showing:
- Current phase (Study 📚 or Break ☕)
- Visual progress bar
- Remaining time in MM:SS format

## 🏗️ Project Structure

```
SmartStudyTimer.java
├── main()                    # Entry point
├── run()                     # Main application loop
├── configureSession()        # Session configuration
├── startSession()            # Study session management
├── startBreak()              # Break session management
├── handlePhaseCompletion()   # Phase transition logic
├── pauseSession()            # Pause functionality
├── stopSession()             # Stop functionality
├── displayProgress()         # Real-time UI updates
├── displaySummary()          # Statistics display
├── shutdown()                # Clean resource management
└── SessionRecord             # Inner class for session data
```

## 🎓 Learning Outcomes

This project helps you understand:

- Thread scheduling and management with `ScheduledExecutorService`
- Concurrent task execution and coordination
- Proper resource cleanup and thread termination
- Timer-based operations in Java
- Console-based user interface design
- State management in multi-threaded applications

## ⚙️ Configuration

Default settings:
- Study Duration: 25 minutes
- Break Duration: 5 minutes
- Thread Pool Size: 2 threads
- Display Update Interval: 1 second

All durations can be customized through the application menu.

## 🔧 Customization

You can easily modify the application by adjusting these constants in the code:

```java
private int studyDuration = 25 * 60;  
private int breakDuration = 5 * 60;  
```

## 📊 Session Tracking

The application maintains a complete history of your sessions, including:
- Start and end times
- Session duration
- Session type (Study or Break)
- Total statistics

## 🤝 Contributing

This is a learning project, but suggestions and improvements are welcome! Feel free to:
- Report bugs
- Suggest new features
- Improve documentation
- Optimize code

## 📝 License

This project is open source and available for educational purposes.


## 🙏 Acknowledgments

- Inspired by the Pomodoro Technique® developed by Francesco Cirillo
- Built for learning Java threading and concurrent programming

---

## ⏱️ Development Time

**Estimated**: 45-75 minutes

**Actual**: Perfect for a focused learning session!

---

**Happy Studying! 📖✨**

*Stay focused, take breaks, and track your progress!*
