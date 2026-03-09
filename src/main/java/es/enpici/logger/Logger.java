package es.enpici.logger;

import java.util.List;

public class Logger {

    private final String clase;
    private final Log logService;

    public Logger(String clase) {
        this(clase, new Log());
    }

    public Logger(String clase, Log logService) {
        this.clase = clase;
        this.logService = logService;
    }

    public void on() {
        logService.on();
    }

    public void off() {
        logService.off();
    }

    public boolean state() {
        return logService.state();
    }

    public void info(String text) {
        writeLog(text, Severity.INFO);
    }

    public void warning(String text) {
        writeLog(text, Severity.WARNING);
    }

    public void severe(String text) {
        writeLog(text, Severity.SEVERE);
    }

    public void writeLog(String text, Severity level) {
        logService.writeLog(text, level, clase);
    }

    public void writeLog(String[] arrayText, Severity level) {
        logService.writeLog(arrayText, level, clase);
    }

    public void writeLog(List<String> arrayText, Severity level) {
        logService.writeLog(arrayText, level, clase);
    }

    public String toText() {
        return logService.toText();
    }

    public void restart() {
        logService.restart();
    }

    public void writeFile() {
        logService.writeFile();
    }

    public void log(Severity level, String text) {
        writeLog(text, level);
    }
}
