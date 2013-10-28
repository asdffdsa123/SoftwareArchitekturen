/*
 * Created on 12.04.2007
 *
 */
package de.hswt.swa.rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class RPCClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] param1 = { "C:" };
		Object ergebnis;
		ergebnis = RPCClient.rufeAuf("aktuelleUhrzeit", new Object[0]);
		System.out.println("Ergebnis2: " + ergebnis.toString());

		String[] dateien = (String[]) RPCClient.rufeAuf("listeVerzeichnis",
				new String[] { "/home/lesske" });
		System.out.println("Dateien: ");
		for (int i = 0; i < dateien.length; i++) {
			System.out.println(dateien[i]);
		}
		RPCClient.rufeAuf("setVector", new Object[]{getValues()});
		System.out.println(RPCClient.rufeAuf("getSD", new Object[]{}));
	}
	
	private static Vector<Double> getValues(){
		return fromArray(new Double[]{1d, 1d, 2d, 3d});
	}
	
	private static <T> Vector<T> fromArray(T[] array){
		Vector<T> result = new Vector<>();
		for(T e : array){
			result.add(e);
		}
		return result;
	}

	public static Object rufeAuf(String methode, Object[] parameter) {
		try {
		    Socket server = new Socket( "bb-x-xterm.hswt.de", 3443 );
		     
		     System.out.println ("Server kontaktiert...");
		     
		     System.out.println(server.toString());
		     
		     // Streams �ffnen:
		     ObjectOutputStream out =  new ObjectOutputStream ( server.getOutputStream() );
		     //ObjectInputStream in = new ObjectInputStream(server.getInputStream());
		     
		     System.out.println("schicke Methodenname");
		     out.writeObject(methode);
		     out.flush();
		     out.writeObject(parameter);
		     
		     ObjectInputStream in = new ObjectInputStream(server.getInputStream());
		     Object returnValue = in.readObject();
		     return returnValue;
		            

		} catch (UnknownHostException e) {
			// TODO Automatisch erstellter Catch-Block
			e.printStackTrace(); return "Fehler";
		} catch (IOException e) {
			// TODO Automatisch erstellter Catch-Block
			e.printStackTrace(); return "Fehler";
		} catch (ClassNotFoundException e) {
			// TODO Automatisch erstellter Catch-Block
			e.printStackTrace(); return "Fehler";
		}

	}
}
