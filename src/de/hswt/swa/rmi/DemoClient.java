/*
 * DemoClient.java
 */

package de.hswt.swa.rmi;

import java.rmi.Naming;
import java.util.Vector;

import de.hswt.swa.tools.StatisticsCalculatorService;

public class DemoClient {

	public static void main(String args[]) {
		try {
			w("Suche Server unter '" + DemoServer.URI + "': ");

			// 1. Schritt: Server suchen:
			DemoServerInterface server =
				(DemoServerInterface) Naming.lookup(DemoServer.URI);

			wln("erfolgreich!");

			// 2. Schritt: Methoden ganz normal aufrufen:

			w("Uhrzeit: ");
			String zeit = server.aktuelleUhrzeit();
			wln(zeit);

			w("Verzeichnis: ");
			String[] vrz = server.listeVerzeichnis("/home");
			wln(vrz.length + " Eintr�ge...");

			for (int i = 0; i < vrz.length; i++)
				wln("[" + i + "]\t" + vrz[i]);

			StatisticsCalculatorService serverCalc =
					(StatisticsCalculatorService) Naming.lookup(DemoServer.URI);
			
			Vector<Double> v = new Vector<>();
			v.add(10.0);
			v.add(15.0);
			v.add(20.0);
			
			serverCalc.setValues(v);
			System.out.println(serverCalc.getStandardDeviation());
			
		} catch (Exception e) {
			e.printStackTrace();
			wln("Fehler: " + e );
		}
	}

	//--------------------------------------> Hilfsroutinen:
	private static void wln(String s) {
		System.out.println(s);
	}

	private static void w(String s) {
		System.out.print(s);
	}

}
