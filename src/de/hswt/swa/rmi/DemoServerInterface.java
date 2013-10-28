/*
 * DemoServerInterface.java
 */

package de.hswt.swa.rmi;

import java.rmi.RemoteException;
import java.rmi.Remote;
import java.util.Vector;

import de.hswt.swa.tools.StatisticsCalculatorService;

public interface DemoServerInterface extends Remote {

	public String aktuelleUhrzeit() throws RemoteException;

	public String[] listeVerzeichnis(String verzeichnisname)
			throws RemoteException;

//	public void setValues(Vector values) throws RemoteException;
//
//	public double getStandardDeviation() throws RemoteException;
}
