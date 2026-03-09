package es.enpici.logger;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LogTest {

    @Test
    void activationAndDeactivationWork() {
        Log service = new Log();
        Logger logger = new Logger("MyClass", service);

        assertFalse(logger.state());
        logger.on();
        assertTrue(logger.state());
        logger.off();
        assertFalse(logger.state());
    }

    @Test
    void messageFormatIncludesTimestampSeverityAndClass() {
        Clock fixedClock = Clock.fixed(Instant.parse("2024-01-02T03:04:05.006Z"), ZoneOffset.UTC);
        Log service = new Log(Path.of("Logs"), fixedClock);
        Logger logger = new Logger("ServiceA", service);

        logger.on();
        logger.info("hello");

        assertTrue(logger.toText().contains("[ 2024/01/02 03:04:05.006 ][INFO]:#ServiceA# hello"));
    }

    @Test
    void writesLogToFile(@TempDir Path tempDir) throws Exception {
        Log service = new Log(tempDir.resolve("Logs"), java.time.Clock.systemUTC());
        Logger logger = new Logger("Persist", service);

        logger.on();
        logger.warning("persist this");
        logger.writeFile();

        Path logFile = tempDir.resolve("Logs/1Log.txt");
        assertTrue(Files.exists(logFile));
        String content = Files.readString(logFile);
        assertTrue(content.contains("[WARNING]:#Persist# persist this"));
    }

    @Test
    void ioErrorsAreHandledWithoutThrowing(@TempDir Path tempDir) throws Exception {
        Path invalidLogPath = tempDir.resolve("existing-file");
        Files.writeString(invalidLogPath, "content");

        Log service = new Log(invalidLogPath, java.time.Clock.systemUTC());
        Logger logger = new Logger("Errors", service);
        logger.on();
        logger.severe("should remain in memory");

        logger.writeFile();

        assertTrue(logger.toText().contains("should remain in memory"));
    }
}
