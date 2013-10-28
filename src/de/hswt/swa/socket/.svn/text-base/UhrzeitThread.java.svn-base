package de.hswt.swa.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UhrzeitThread extends Thread {
	
	private Socket socket;
	private int nummer;
	
	public UhrzeitThread(Socket s, int i) {
		socket = s;
		nummer = i;
	}
	
	public void run() {
		try {
			System.out.println("Starte Bearbeitung von Anfrage " + nummer);
			bearbeiteAnfrage(socket);
			System.out.println("Bearbeitung von Anfrage " + nummer + " ist abgeschlossen");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
