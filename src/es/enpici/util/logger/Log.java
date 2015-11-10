/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.enpici.util.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Enrique
 */
public class Log {

    /**
     * OFF is a special level that can be used to turn off logging. This level
     * is initialized to <CODE>Integer.MAX_VALUE</CODE>.
     */
    public static final Integer OFF = Integer.MAX_VALUE;

    /**
     * SEVERE is a message level indicating a serious failure.
     * <p>
     * In general SEVERE messages should describe events that are of
     * considerable importance and which will prevent normal program execution.
     * They should be reasonably intelligible to end users and to system
     * administrators. This level is initialized to <CODE>1000</CODE>.
     */
    public static final Integer SEVERE = 1000;

    /**
     * WARNING is a message level indicating a potential problem.
     * <p>
     * In general WARNING messages should describe events that will be of
     * interest to end users or system managers, or which indicate potential
     * problems. This level is initialized to <CODE>900</CODE>.
     */
    public static final Integer WARNING = 900;

    /**
     * INFO is a message level for informational messages.
     * <p>
     * Typically INFO messages will be written to the console or its equivalent.
     * So the INFO level should only be used for reasonably significant messages
     * that will make sense to end users and system administrators. This level
     * is initialized to <CODE>800</CODE>.
     */
    public static final Integer INFO = 800;

    /**
     * CONFIG is a message level for static configuration messages.
     * <p>
     * CONFIG messages are intended to provide a variety of static configuration
     * information, to assist in debugging problems that may be associated with
     * particular configurations. For example, CONFIG message might include the
     * CPU type, the graphics depth, the GUI look-and-feel, etc. This level is
     * initialized to <CODE>700</CODE>.
     */
    public static final Integer CONFIG = 700;

    /**
     * FINE is a message level providing tracing information.
     * <p>
     * All of FINE, FINER, and FINEST are intended for relatively detailed
     * tracing. The exact meaning of the three levels will vary between
     * subsystems, but in general, FINEST should be used for the most voluminous
     * detailed output, FINER for somewhat less detailed output, and FINE for
     * the lowest volume (and most important) messages.
     * <p>
     * In general the FINE level should be used for information that will be
     * broadly interesting to developers who do not have a specialized interest
     * in the specific subsystem.
     * <p>
     * FINE messages might include things like minor (recoverable) failures.
     * Issues indicating potential performance problems are also worth logging
     * as FINE. This level is initialized to <CODE>500</CODE>.
     */
    public static final Integer FINE = 500;

    /**
     * FINER indicates a fairly detailed tracing message. By default logging
     * calls for entering, returning, or throwing an exception are traced at
     * this level. This level is initialized to <CODE>400</CODE>.
     */
    public static final Integer FINER = 400;

    /**
     * FINEST indicates a highly detailed tracing message. This level is
     * initialized to <CODE>300</CODE>.
     */
    public static final Integer FINEST = 300;

    /**
     * ALL indicates that all messages should be logged. This level is
     * initialized to <CODE>Integer.MIN_VALUE</CODE>.
     */
    public static final Integer ALL = Integer.MIN_VALUE;

    private static ArrayList<String> log;
    private static boolean traza = false;
    private static boolean continua = false;
    /**
     * Devuelve el texto segun el codigo de nivel.
     * @param level
     * @return String con el texto del nivel
     */
    public static String level(Integer level) {
        String o;
        switch (level) {
            case 1000:
                o = "SEVERE";
                break;
            case 900:
                o = "WARNING";
                break;
            case 800:
                o = "INFO";
                break;
            case 700:
                o = "CONFIG";
                break;
            default:
                o = "MSG";
                break;

        }
        return o;

    }
    /**
     * Activa el log
     */
    public static void LogOn() {
        traza = true;
    }
    /**
     * Desactiva el log
     */
    public static void LogOff() {
        traza = false;
        continua = false;
    }
    /**
     * Muestra el estado del Log
     * @return boolean (Activo/Desactivo)
     */
    public static boolean LogState() {
        return traza;
    }
    /**
     * Genera un Log del tipo especifico.
     * @param text String del log.
     * @param nivel Codigo del nivel.
     * @param clase Clase que lanza el log.
     */
    public static void writeLog(String text, Integer nivel,String clase) {
        if (traza) {
            if (log == null) {
                log = new ArrayList<>(0);
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
            Date date = new Date();
            String ntext = String.format("[ %s ][%s]:#%s# %s", dateFormat.format(date),level(nivel),clase, text);
            log.add(ntext);
            System.out.println(log.size() + ": " + ntext);
            if (log.size() > 1000) {
                continua = true;
                WriteFile();
            }
        }
    }
/**
     * Genera un Log del tipo especifico.
     * @param arraytext Array de String del log.
     * @param level Codigo del nivel.
     * @param clase Clase que lanza el log.
     */
    public static void writeLog(String[] arraytext, Integer level,String clase) {
        if (traza) {
            for (String arraytext1 : arraytext) {
                writeLog(arraytext1, level,clase);
            }
        }
    }
/**
     * Genera un Log del tipo especifico.
     * @param arraytext ArrayList de String del log.
     * @param level Codigo del nivel.
     * @param clase Clase que lanza el log.
     */
    public static void writeLog(ArrayList<String> arraytext, Integer level,String clase) {
        if (traza) {
            arraytext.stream().forEach((arraytext1) -> {
                writeLog(arraytext1, level,clase);
            });
        }
    }
    /**
     * Devuelve el log convertido en texto.
     * @return String con el contenido del log
     */
    public static String toText() {
        String salida = "";
        salida = log.stream().map((v) -> v + "\r\n").reduce(salida, String::concat);
        return salida;
    }
    /**
     * Reinicia el log
     */
    public static void Restart() {
        log.clear();
    }
    /**
     * Escribe el fichero Log.
     */
    public static void WriteFile() {
        if (traza) {
            WriteLog(toText());
        }
    }

    private static void WriteLog(String salida) {
        FileWriter fw;
        Object i;
        try {
            String logs="Logs";
            File f= new File(logs);
            if (!f.exists()) {
                writeLog("OH! no tiene el directorio Log, vamos a crearlo",INFO,Log.class.getName());
		if (f.mkdir()) {
			writeLog("Directorio de log creado con exito",INFO,Log.class.getName());
                        logs+="/";
		} else {
			writeLog("Hemos tenido un problema al crear el directorio. Guardaremos los logs en la raiz",WARNING,Log.class.getName());
                        logs="";
                }
            }else{
                logs+="/";
            }
            File fichero = new File(logs+"1Log.txt");
            int j = 2;
            while (fichero.exists()) {
                fichero = new File(logs+j + "Log.txt");
                j++;
            }
            fw = new FileWriter(fichero, continua);
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(salida);
            } catch (Exception ex) {
                System.out.println("Un error inexperado ha ocurrido y no se ha podido almacenar el Log, pero nose preocupe se lo mostraremos por pantalla");
                System.out.println(salida);
            } finally {
                fw.close();
                Restart();
            }
        } catch (IOException ex) {
            System.out.println("Un error inexperado ha ocurrido y no se ha podido almacenar el Log, pero nose preocupe se lo mostraremos por pantalla");
            System.out.println(ex.getMessage());
            System.out.println(salida);
        }
    }
}
