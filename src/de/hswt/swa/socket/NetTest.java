package de.hswt.swa.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetTest {

	public static void main(String[] arg) throws UnknownHostException {
		System.out.println("Local Host: " + InetAddress.getLocalHost()); //lokale Adresse ausgeben
		dnsLookup("www.hswt.de");
		dnsLookup("bb-x-xterm.hswt.de");
		dnsLookup("bb-x-servlet.hswt.de");
		
		//printWebSeite("http://www.google.de:80");
		
		displayTime("time.nist.gov");
	}

	static void dnsLookup(String dnsName) {
		System.out.println("IP-Adresse für Host " + dnsName + " bestimmen...");
		// Bestimme Adresse und Name des Rechners
		try {
			InetAddress ia = InetAddress.getByName(dnsName);
			System.out.println("IP Adresse: " + ia.getHostAddress());
			System.out.println("IP Name: " + ia.getHostName());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	static void printWebSeite(String seite) {
		try {
			URL url = new URL(seite);
			System.out.println("Protokoll: " +  url.getProtocol());
			System.out.println("Port: " + url.getPort());
			
			InputStream in = url.openStream();
			SimpleBrowser browser = new SimpleBrowser(600, 800, url.toString());
			browser.setVisible(true);
			
			
			
		} catch (MalformedURLException e1) {
			System.out.println("falsche URL: " + seite);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	
		
	}
	
	private static void displayTime(String server) {
		try {
			Socket socket = new Socket(server, 13);
			System.out.println("Verbindung zu " + server +  " hergestellt!");
			InputStream is = socket.getInputStream();
			
			byte[] b = new byte[128];
			while (is.read(b) != -1) {
				System.out.println(new String(b));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
