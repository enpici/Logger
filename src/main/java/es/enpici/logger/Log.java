package es.enpici.logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio de logging inyectable, sin estado global estático.
 */
public class Log {

    private static final int MAX_ENTRIES = 1000;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");

    private final ReentrantLock lock = new ReentrantLock(true);
    private final List<String> entries = new ArrayList<>();
    private final Path logDirectory;
    private final Clock clock;

    private boolean enabled;
    private boolean appendMode;

    public Log() {
        this(Path.of("Logs"), Clock.systemDefaultZone());
    }

    Log(Path logDirectory, Clock clock) {
        this.logDirectory = logDirectory;
        this.clock = clock;
    }

    public void on() {
        lock.lock();
        try {
            enabled = true;
        } finally {
            lock.unlock();
        }
    }

    public void off() {
        lock.lock();
        try {
            enabled = false;
            appendMode = false;
        } finally {
            lock.unlock();
        }
    }

    public boolean state() {
        lock.lock();
        try {
            return enabled;
        } finally {
            lock.unlock();
        }
    }

    public void writeLog(String text, Severity level, String clase) {
        lock.lock();
        try {
            if (!enabled) {
                return;
            }
            String timestamp = LocalDateTime.now(clock).format(FORMATTER);
            String entry = String.format("[ %s ][%s]:#%s# %s", timestamp, level, clase, text);
            entries.add(entry);
            System.out.println(entries.size() + ": " + entry);
            if (entries.size() > MAX_ENTRIES) {
                appendMode = true;
                writeFileInternal();
            }
        } finally {
            lock.unlock();
        }
    }

    public void writeLog(String[] arrayText, Severity level, String clase) {
        for (String text : arrayText) {
            writeLog(text, level, clase);
        }
    }

    public void writeLog(List<String> arrayText, Severity level, String clase) {
        for (String text : arrayText) {
            writeLog(text, level, clase);
        }
    }

    public String toText() {
        lock.lock();
        try {
            return String.join("\r\n", entries) + (entries.isEmpty() ? "" : "\r\n");
        } finally {
            lock.unlock();
        }
    }

    public void restart() {
        lock.lock();
        try {
            entries.clear();
        } finally {
            lock.unlock();
        }
    }

    public void writeFile() {
        lock.lock();
        try {
            if (!enabled) {
                return;
            }
            writeFileInternal();
        } finally {
            lock.unlock();
        }
    }

    private void writeFileInternal() {
        String output = String.join("\r\n", entries) + (entries.isEmpty() ? "" : "\r\n");
        try {
            Files.createDirectories(logDirectory);
            Path targetFile = nextAvailableFile();
            try (BufferedWriter writer = Files.newBufferedWriter(
                    targetFile,
                    StandardOpenOption.CREATE,
                    appendMode ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING)) {
                writer.write(output);
            }
            restart();
        } catch (IOException ex) {
            System.err.println("No se ha podido almacenar el Log. Se muestra por pantalla.");
            System.err.println(ex.getMessage());
            System.err.println(output);
        }
    }

    private Path nextAvailableFile() {
        int index = 1;
        Path file;
        do {
            file = logDirectory.resolve(index + "Log.txt");
            index++;
        } while (Files.exists(file));
        return file;
    }
}
