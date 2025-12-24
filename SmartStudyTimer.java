import java.util.Scanner;
import java.util.concurrent.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SmartStudyTimer {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final List<SessionRecord> sessionHistory = new ArrayList<>();
    private ScheduledFuture<?> timerTask;
    private ScheduledFuture<?> displayTask;
    private int remainingSeconds;
    private boolean isStudyPhase;
    private int completedSessions = 0;
    private LocalDateTime sessionStartTime;


    private int studyDuration = 25 * 60;
    private int breakDuration = 5 * 60;

    public static void main(String[] args) {
        SmartStudyTimer timer = new SmartStudyTimer();
        timer.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   Smart Study Session Timer (Pomodoro) ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        while (true) {
            displayMenu();
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    configureSession(scanner);
                    break;
                case "2":
                    startSession();
                    break;
                case "3":
                    pauseSession();
                    break;
                case "4":
                    stopSession();
                    break;
                case "5":
                    displaySummary();
                    break;
                case "6":
                    System.out.println("\n✓ Shutting down...");
                    shutdown();
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.\n");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("1. Configure Session");
        System.out.println("2. Start Session");
        System.out.println("3. Pause Session");
        System.out.println("4. Stop Session");
        System.out.println("5. View Session Summary");
        System.out.println("6. Exit");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    private void configureSession(Scanner scanner) {
        System.out.println("\nConfigure Session Duration");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        try {
            System.out.print("Study duration (minutes) [default: 25]: ");
            String studyInput = scanner.nextLine().trim();
            if (!studyInput.isEmpty()) {
                int minutes = Integer.parseInt(studyInput);
                if (minutes > 0 && minutes <= 120) {
                    studyDuration = minutes * 60;
                } else {
                    System.out.println("Invalid range. Using default (25 min).");
                }
            }

            System.out.print("Break duration (minutes) [default: 5]: ");
            String breakInput = scanner.nextLine().trim();
            if (!breakInput.isEmpty()) {
                int minutes = Integer.parseInt(breakInput);
                if (minutes > 0 && minutes <= 30) {
                    breakDuration = minutes * 60;
                } else {
                    System.out.println("Invalid range. Using default (5 min).");
                }
            }

            System.out.println("\n✓ Configuration saved!");
            System.out.println("  Study: " + (studyDuration / 60) + " minutes");
            System.out.println("  Break: " + (breakDuration / 60) + " minutes");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Configuration unchanged.");
        }
    }

    private void startSession() {
        if (timerTask != null && !timerTask.isDone()) {
            System.out.println("Session already running!");
            return;
        }

        isStudyPhase = true;
        remainingSeconds = studyDuration;
        sessionStartTime = LocalDateTime.now();

        System.out.println("\nStarting Study Session!");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("Duration: " + formatTime(remainingSeconds));
        System.out.println("Stay focused! \n");

        timerTask = scheduler.scheduleAtFixedRate(() -> {
            remainingSeconds--;

            if (remainingSeconds <= 0) {
                handlePhaseCompletion();
            }
        }, 1, 1, TimeUnit.SECONDS);


        displayTask = scheduler.scheduleAtFixedRate(() -> {
            displayProgress();
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void handlePhaseCompletion() {
        if (isStudyPhase) {

            timerTask.cancel(false);
            displayTask.cancel(false);
            completedSessions++;

            SessionRecord record = new SessionRecord(
                    sessionStartTime,
                    LocalDateTime.now(),
                    studyDuration / 60,
                    "Study"
            );
            sessionHistory.add(record);

            System.out.println("\n\n Study session completed! Great work!");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("Completed sessions: " + completedSessions);

            // Start break automatically
            startBreak();
        } else {
            // Break phase completed
            timerTask.cancel(false);
            displayTask.cancel(false);

            SessionRecord record = new SessionRecord(
                    sessionStartTime,
                    LocalDateTime.now(),
                    breakDuration / 60,
                    "Break"
            );
            sessionHistory.add(record);

            System.out.println("\n\n Break time over! Ready for another session?");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        }
    }

    private void startBreak() {
        isStudyPhase = false;
        remainingSeconds = breakDuration;
        sessionStartTime = LocalDateTime.now();

        System.out.println("\n Break Time!");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("Duration: " + formatTime(remainingSeconds));
        System.out.println("Relax and recharge!\n");

        timerTask = scheduler.scheduleAtFixedRate(() -> {
            remainingSeconds--;

            if (remainingSeconds <= 0) {
                handlePhaseCompletion();
            }
        }, 1, 1, TimeUnit.SECONDS);

        displayTask = scheduler.scheduleAtFixedRate(() -> {
            displayProgress();
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void pauseSession() {
        if (timerTask == null || timerTask.isDone()) {
            System.out.println("No active session to pause!");
            return;
        }

        timerTask.cancel(false);
        displayTask.cancel(false);
        System.out.println("\nSession paused at " + formatTime(remainingSeconds));
        System.out.println("Select option 2 to resume.\n");
    }

    private void stopSession() {
        if (timerTask == null || timerTask.isDone()) {
            System.out.println("No active session to stop!");
            return;
        }

        timerTask.cancel(false);
        displayTask.cancel(false);

        System.out.println("\n Session stopped!");
        System.out.println("Time remaining: " + formatTime(remainingSeconds) + "\n");

        remainingSeconds = 0;
    }

    private void displayProgress() {
        int totalSeconds = isStudyPhase ? studyDuration : breakDuration;
        int elapsed = totalSeconds - remainingSeconds;
        int progressPercent = (elapsed * 100) / totalSeconds;

        String phase = isStudyPhase ? "STUDY" : "BREAK";
        String bar = createProgressBar(progressPercent);

        System.out.print("\r" + phase + " | " + bar + " | " +
                formatTime(remainingSeconds) + " remaining ");
    }

    private String createProgressBar(int percent) {
        int filled = percent / 5; // 20 chars total
        int empty = 20 - filled;
        return "█".repeat(filled) + "░".repeat(empty) + " " + percent + "%";
    }

    private String formatTime(int seconds) {
        int mins = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }

    private void displaySummary() {
        System.out.println("\n Session Summary");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("Total completed sessions: " + completedSessions);
        System.out.println("Total sessions recorded: " + sessionHistory.size());

        int totalStudyMinutes = 0;
        int totalBreakMinutes = 0;

        for (SessionRecord record : sessionHistory) {
            if (record.type.equals("Study")) {
                totalStudyMinutes += record.durationMinutes;
            } else {
                totalBreakMinutes += record.durationMinutes;
            }
        }

        System.out.println("\nTotal study time: " + totalStudyMinutes + " minutes");
        System.out.println("Total break time: " + totalBreakMinutes + " minutes");

        if (!sessionHistory.isEmpty()) {
            System.out.println("\nRecent Sessions:");
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            int displayCount = Math.min(5, sessionHistory.size());

            for (int i = sessionHistory.size() - displayCount; i < sessionHistory.size(); i++) {
                SessionRecord record = sessionHistory.get(i);
                String icon = record.type.equals("Study") ? "Study" : "Break";
                System.out.printf("%s %s | %s - %s | %d min%n",
                        icon,
                        record.type,
                        record.startTime.format(formatter),
                        record.endTime.format(formatter),
                        record.durationMinutes
                );
            }
        }
        System.out.println();
    }

    private void shutdown() {
        if (timerTask != null) {
            timerTask.cancel(false);
        }
        if (displayTask != null) {
            displayTask.cancel(false);
        }
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(2, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }

        displaySummary();
        System.out.println("Thank you for using Smart Study Timer!\n");
    }

    private static class SessionRecord {
        LocalDateTime startTime;
        LocalDateTime endTime;
        int durationMinutes;
        String type;

        SessionRecord(LocalDateTime startTime, LocalDateTime endTime, int durationMinutes, String type) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.durationMinutes = durationMinutes;
            this.type = type;
        }
    }
}
