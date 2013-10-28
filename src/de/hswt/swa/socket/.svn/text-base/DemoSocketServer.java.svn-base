/*
 * DemoSocketServer.java
 */

package de.hswt.swa.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DemoSocketServer {

	// der server socket
	private ServerSocket ss;
	private int counter = 0;

	/**
	 * erzeugt ein neues Server-Objekt
	 * 
	 * @portNummer: der Port, ueber den Server und Client kommunizieren
	 */
	public DemoSocketServer(int portNummer) throws IOException {
		// Port @portNummer belegen:
		ss = new ServerSocket(portNummer);
		System.out.println(ss.getLocalSocketAddress());

		wln("Server erfolgreich auf Port " + portNummer + " gestartet!");

		while (true) {
			// Auf Client-Anfragen warten:
			System.out.println("Server wartet auf Anfragen...");
			Socket client_Anfrage = ss.accept();
			counter++;
			System.out.println("Die " + counter + "-te Anfrage ist eingetroffen");
			
			UhrzeitThread ut = new UhrzeitThread(client_Anfrage, counter);
			ut.start();
			// Client-Anfrage bearbeiten:
			//bearbeiteAnfrage(client_Anfrage);
		}
	}

	/**
	 * @anfrage: Der Client-Socket
	 */
	private void bearbeiteAnfrage(Socket anfrage) throws IOException {
		// Streams initialisieren:
		BufferedReader in = new BufferedReader(new InputStreamReader(
				anfrage.getInputStream()));
		PrintWriter out = new PrintWriter(anfrage.getOutputStream());

		// Kommunikationsszenario abarbeiten:

		// Client schickt Begruessung
		String hallo = in.readLine();
		wln("Client sagt: " + hallo);

		// Schicke Antwort
		String antwort_1 = "Hallo Client!";
		wln("sende: " + antwort_1);
		out.println(antwort_1);
		out.flush();

		// Client fragt nach der Uhrzeit
		String frage = in.readLine();
		wln("Client fragt: " + frage);
		String antwort_2 = aktuelleUhrzeit();

		// Sende Uhrzeit
		wln("sende: " + antwort_2);
		out.println(antwort_2);
		out.flush();

		// Kommunikationskanal schliessen:
		in.close();
		out.close();
		anfrage.close();
	}

	/**
	 * @args: Kommandozeilen-Parameter
	 */
	public static void main(String args[]) {
		// Server starten:
		try {
			// Achtung: bitte Port-Nummer aendern
			new DemoSocketServer(2200);
		} catch (IOException e) {
			// Ein I/O-Fehler ist aufgetreten:
			System.out.println("Fehler: " + e);
		}
	}

	// --------------------------------------------------> Hilfsroutinen:
	private void wln(String s) {
		System.out.println(s);
	}

	private String aktuelleUhrzeit() {
		Date jetzt = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"HH:mm '-' dd.MM.yyyy");

		// verlangsamt die Antwort kuenstlich um 10 sek.
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		}

		// Rueckgabe:
		return formatter.format(jetzt);
	}

}
