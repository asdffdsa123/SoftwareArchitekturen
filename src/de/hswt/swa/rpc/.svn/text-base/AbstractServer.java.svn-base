/*
 * Server.java
 */

package de.hswt.swa.rpc;

//----> Imports:
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
//<-------------

public abstract class AbstractServer extends Thread {
   
    private ServerSocket ss = null;
    
    public AbstractServer ( int portNummer ) throws IOException {
        // @portNummer belegen:
        ss = new ServerSocket ( portNummer );        

        wln ("[Server belegt Port: " + portNummer + "]");        
    }
    
    public void run () {
        // endlose Schleife:
        while (true) try {
            // Auf Client-Anfragen warten:
            Socket client_Anfrage = ss.accept();
            
            /* Ein neuer Client hat sich angemeldet und muss
             * bedient werden. Es wird deshalb die Methode
             * bearbeite_Anfrage aufgerufen um einen neuen
             * Thread zu erzeugen: */            
            bearbeite_Anfrage ( client_Anfrage );
        } catch (Exception e) {
            // Ein Fehler ist aufgetreten:
            wln ("Fehler@run: " + e);
        }
    }        
    
    protected void bearbeite_Anfrage ( Socket anfrage ) {
        /* Diese Methode muss in einer abgeleiteten Klasse
         * nicht unbedingt überschrieben werden, da sie die
         * abstrakte Methode neue_Anfrage aufruft, die dort
         * auf jeden Fall implementiert werden muss */
        AnfrageBearbeitung bedienung = neueAnfrage ( anfrage );
        
        /* Da die Klasse Anfragebearbeitung ein Thread ist
         * muss an dieser Stelle die start()-Methode aufgerufen
         * werden, damit das Kommunikationsschema von dieser
         * Anfragebearbeitung abgearbeitet werden kann */
        bedienung.start();
    }
    
    protected abstract AnfrageBearbeitung neueAnfrage ( Socket anfrage );
    
    protected void ichBinFertig ( AnfrageBearbeitung client_thread ) {
        /* Ein Kommunikationsszenario mit einem Client
         * ist beendet und dieser sagt nun dem zugehörigen
         * Server bescheid */
        client_thread = null;
    }

    //-------------------------------------------------> Hilfsroutinen:
    protected void wln (String s) {
        System.out.println ( s );
    }
    
    protected void w (String s) {
        System.out.print ( s );
    }

}