/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.enpici.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Enrique
 */
public class Log {


    private static ArrayList<String> log;
    private static boolean traza = false;
    private static boolean continua = false;


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
     *
     * @return boolean (Activo/Desactivo)
     */
    public static boolean LogState() {
        return traza;
    }

    /**
     * Genera un Log del tipo especifico.
     *
     * @param text  String del log.
     * @param nivel Codigo del nivel.
     * @param clase Clase que lanza el log.
     */
    public static void writeLog(String text, es.enpici.logger.Severity nivel, String clase) {
        if (traza) {
            if (log == null) {
                log = new ArrayList<>(0);
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
            Date date = new Date();
            String ntext = String.format("[ %s ][%s]:#%s# %s", dateFormat.format(date), nivel, clase, text);
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
     *
     * @param arraytext Array de String del log.
     * @param level     Codigo del nivel.
     * @param clase     Clase que lanza el log.
     */
    public static void writeLog(String[] arraytext, Severity level, String clase) {
        if (traza) {
            for (String arraytext1 : arraytext) {
                writeLog(arraytext1, level, clase);
            }
        }
    }

    /**
     * Genera un Log del tipo especifico.
     *
     * @param arraytext ArrayList de String del log.
     * @param level     Codigo del nivel.
     * @param clase     Clase que lanza el log.
     */
    public static void writeLog(ArrayList<String> arraytext, Severity level, String clase) {
        if (traza) {
            arraytext.stream().forEach((arraytext1) -> {
                writeLog(arraytext1, level, clase);
            });
        }
    }

    /**
     * Devuelve el log convertido en texto.
     *
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

    public static void printStackTrace() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (int pos = stack.length - 1; pos > 1; pos--) {
            StackTraceElement elem = stack[pos];
            //se elimina el paquete del nombre de la clase
            String name = elem.getClassName().substring(
                    elem.getClassName().lastIndexOf(".") + 1);
            System.out.print(name + "." + elem.getMethodName() + ":"
                    + elem.getLineNumber());
            if (pos > 2) {
                System.out.print("->");
            }
        }
    }

    private static void WriteLog(String salida) {
        FileWriter fw;
        Object i;
        try {
            String logs = "Logs";
            File f = new File(logs);
            if (!f.exists()) {
                writeLog("OH! no tiene el directorio Log, vamos a crearlo", Severity.INFO, Log.class.getName());
                if (f.mkdir()) {
                    writeLog("Directorio de log creado con exito", Severity.INFO, Log.class.getName());
                    logs += "/";
                } else {
                    writeLog("Hemos tenido un problema al crear el directorio. Guardaremos los logs en la raiz", Severity.WARNING, Log.class.getName());
                    logs = "";
                }
            } else {
                logs += "/";
            }
            File fichero = new File(logs + "1Log.txt");
            int j = 2;
            while (fichero.exists()) {
                fichero = new File(logs + j + "Log.txt");
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

    private Log() {
    }

}
