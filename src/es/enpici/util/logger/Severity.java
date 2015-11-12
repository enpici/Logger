/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.enpici.util.logger;

import java.util.Comparator;

/**
 *
 * @author Enrique
 */
public enum Severity implements Comparator<Severity>{
    OFF("OFF",Integer.MAX_VALUE), SEVERE("SEVERE",1000), WARNING("WARNING",900), INFO("INFO",800), CONFIG("CONFIG",700), FINE("FINE",500), FINER("FINER",400), FINEST("FINEST",300), ALL("ALL",Integer.MIN_VALUE);
    public final Integer value;
    private final String val;
    Severity(String v,Integer value) {
        this.value = value;
        this.val=v;
    }
    @Override
    public String toString(){
     return this.val;
    }
    @Override
    public int compare(Severity o1, Severity o2) {
        return o1.value.compareTo(o2.value);
    }
    
}
