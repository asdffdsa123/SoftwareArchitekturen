/*
 * Anfragebearbeitung.java
 */

package de.hswt.swa.rpc;

import java.net.Socket;
//<-------------

public abstract class AnfrageBearbeitung extends Thread {
    
    protected Socket anfrage = null;
    private AbstractServer server = null;
            
    public AnfrageBearbeitung( AbstractServer server, Socket anfrage ) {
        this.anfrage = anfrage;
        this.server = server;
    }
    
    public void run () {
        // Starte Kommunikation:
        kommuniziere ();        
        // Fertig mit Kommunikation, sage dem server bescheid:
        server.ichBinFertig ( this );
    }
        
    // muss von erbender Klasse überschrieben werden:    
    protected abstract void kommuniziere ();
    

    //-------------------------------------------------> Hilfsroutinen:
    protected void wln (String s) {
        System.out.println ( s );
    }
    
    protected void w (String s) {
        System.out.print ( s );
    }
}
