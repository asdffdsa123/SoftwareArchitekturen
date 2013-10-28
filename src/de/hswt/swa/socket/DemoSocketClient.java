/*
 * DemoSocketClient.java
 */

package de.hswt.swa.socket;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.Socket;

public class DemoSocketClient {

	// **********************
	// Hauptfunktion
	// **********************

	/**
	 * @args: Kommandozeilen-Parameter: erster Parameter ist Server-Rechner
	 */
	public static void main(String args[]) {
		try {
			// Den Server kontaktieren...
			Socket socket = new Socket("localhost", 2200);		

			System.out.println("Server kontaktiert...");

			// Streams oeffnen:
			OutputStream os = socket.getOutputStream();
			InputStream is = socket.getInputStream();
			
			BufferedReader rin = new BufferedReader(new InputStreamReader(is));
			PrintWriter pout  = new PrintWriter(os, true);
			

			// Kommunikationsszenario abarbeiten:

			// 1.) Schicke Begruessung an den Server...
			pout.println("Hallo Server");
			System.out.println("Begrüssung geschickt");
			
			// 2.) Empfange Server-Antwort...
			String s = rin.readLine();
			System.out.println("Server antwortet: " + s);

			// 3.) Frage Server nach der Uhrzeit...
			pout.println("uhrzeit");

			// 4.) Empfange Uhrzeit...
			s = rin.readLine();
			System.out.println("aktuelle Uhrzeit: " + s);
			
			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
			// Fehler:
			System.out.println("Fehler: " + e);
		}
	}

}
