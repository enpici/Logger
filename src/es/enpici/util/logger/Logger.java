/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.enpici.util.logger;

import java.util.ArrayList;

/**
 *
 * @author Enrique
 */
public class Logger {
    
     private final String clase;
    /**
     * Constructor de la clase Logger.
     * @param clase Parametro para especificar sobre que clase trabaja el Logger
     */
    public Logger(String clase) {
        this.clase=clase;
    }
    /**
     * Activa el Log
     */
    public void On() {
        Log.LogOn();
    }
    /**
     * Desactiva el Log
     */
    public void Off() {
        Log.LogOff();
    }
    /**
     * Muestra el estado del Log
     * @return boolean (Activo/Desactivo)
     */
    public boolean State() {
        return Log.LogState();
    }
    /**
     * Genera un Log de tipo info.
     * @param text 
     */
    public void info(String text){
        this.writeLog(text, Severity.INFO);
    }
    /**
     * Genera un Log de tipo warning.
     * @param text 
     */
    public void warning(String text){
         this.writeLog(text, Severity.WARNING);
    }
    /**
     * Genera un Log de tipo severe.
     * @param text 
     */
    public void severe(String text){
         this.writeLog(text, Severity.SEVERE);
    }
    /**
     * Genera un Log del tipo especifico.
     * @param text
     * @param level Tipo de Log
     */
    public void writeLog(String text,Severity level) {
        Log.writeLog(text,level,clase);
    }
    /**
     * Genera un Log del tipo especifico.
     * @param arraytext Array con textos a mostrar del mismo tipo
     * @param level Tipo de Log
     */
    public void writeLog(String[] arraytext,Severity level) {
        Log.writeLog(arraytext,level,clase);
    }
    /**
     * Genera un Log del tipo especifico.
     * @param arraytext ArrayList con textos a mostrar del mismo tipo
     * @param level Tipo de Log
     */
    public  void writeLog(ArrayList<String> arraytext,Severity level) {
        Log.writeLog(arraytext,level,clase);
    }
    /**
     * Devuelve el log convertido en texto.
     * @return String con el contenido del log
     */
    public  String toText() {
       
         return Log.toText();
    }
    /**
     * Reinicia el log
     */
    public  void Restart() {
        Log.Restart();
    }
    /**
     * Escribe el fichero Log.
     */
    public  void WriteFile() {
        Log.WriteFile();
    }
    /**
     * como la funcion writeLog.
     * @param level
     * @param text 
     */
    public void log(Severity level, String text ) {
      this.writeLog(text, level);
    }

    

   
   
}
