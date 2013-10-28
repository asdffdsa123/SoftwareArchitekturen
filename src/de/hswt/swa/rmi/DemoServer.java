/*
 * DemoServer.java
 */

package de.hswt.swa.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PipedInputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;
import java.text.SimpleDateFormat;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import de.hswt.swa.seq.FastaReader;
import de.hswt.swa.seq.Sequence;
import de.hswt.swa.tools.StatisticsCalculator;
import de.hswt.swa.tools.StatisticsCalculatorService;

public class DemoServer extends UnicastRemoteObject implements
		DemoServerInterface, StatisticsCalculatorService, FileSystemService, FastaBlastService{

	//StatisticsCalculatorService statisticsImpl = new StatisticsCalculator();
	
	private static final int PORT = 61232;
	public static final String URI = "rmi://bb-x-xterm.hswt.de:"+PORT+"/demoService";
	
	private FileSystemView fileSystemView = FileSystemView.getFileSystemView();

	public DemoServer() throws Exception {
		/*
		 * Konstruktor muss in jedem Falle auftauchen, auch wenn nichts drin
		 * steht!
		 */
	}

	/*
	 * Methoden des Interfaces:(sie entsprechen denen der Klasse RPCBearbeitung
	 */
	public String aktuelleUhrzeit() throws RemoteException {
		w("-> aktuelleUhrzeit(): ");

		Date jetzt = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"HH:mm:ss '-' dd.MM.yyyy");

		wln(formatter.format(jetzt));

		// R�ckgabe:
		return formatter.format(jetzt);
	}

	public synchronized String[] listeVerzeichnis(String verzeichnisname)
			throws RemoteException {
		w("-> listeVerzeichnis( '" + verzeichnisname + "' ): ");

		// Nach Verzeichnis suchen:
		File dir = new File(verzeichnisname);
		if (dir.exists() && dir.isDirectory()) {
			/*
			 * Das Verzeichnis existiert und es handelt sich um ein Verzeichnis
			 */
			String[] namen = dir.list();
			wln(namen.length + " Verzeichnisse");

			return namen;
		} else {
			// existiert nicht:
			wln("unbekannt");

			return new String[] { "existiert nicht" };
		}
	}

	// -------------> Hauptfunktion

	public static void main(String args[]) {
		try {

			try {
				LocateRegistry.createRegistry(PORT);
			} catch (RemoteException e) {
				/* ... */
			}

			// 1. Schritt: Serverobjekt erzeugen:
			DemoServer server = new DemoServer();

			w("Anmeldung am Broker: ");

			// 2. Schritt: Registrierung am Broker als 'demoService':
			Naming.rebind(URI, server);

			// Fertig!
			wln("erfolgreich");

		} catch (Exception e) {
			e.printStackTrace();
			wln("Es ist folgender Fehler aufgetreten:");
			wln(e.toString());
		}
	}

	// -------------------------------------------------> Hilfsroutinen:
	private static void wln(String s) {
		System.out.println(s);
	}

	private static void w(String s) {
		System.out.print(s);
	}

	@Override
	public synchronized void setValues(Vector values) throws RemoteException {
		//statisticsImpl.setValues(values);
	}

	@Override
	public synchronized double getStandardDeviation() throws RemoteException {
		return 0;//statisticsImpl.getStandardDeviation();
	}

	@Override
	public File[] getFiles(File dir, boolean useFileHiding) {
		return fileSystemView.getFiles(dir, useFileHiding);
	}

	@Override
	public File getHomeDirectory() {
		return fileSystemView.getHomeDirectory();
	}

	@Override
	public File createFileObject(String path) {
		return fileSystemView.createFileObject(path);
	}

	@Override
	public File createFileObject(File dir, String filename) {
		return fileSystemView.createFileObject(dir, filename);
	}

	@Override
	public File[] getRoots() {
		return fileSystemView.getRoots();
	}

	@Override
	public File createNewFolder(File arg0) throws IOException {
		return fileSystemView.createNewFolder(arg0);
	}

	@Override
	public File getParentDirectory(File dir) throws RemoteException {
		return fileSystemView.getParentDirectory(dir);
	}

	@Override
	public Boolean isTraversable(File f) throws RemoteException {
		return fileSystemView.isTraversable(f);
	}

	@Override
	public boolean isRoot(File f) throws RemoteException {
		return fileSystemView.isRoot(f);
	}

	@Override
	public File getDefaultDirectory() throws RemoteException {
		return fileSystemView.getDefaultDirectory();
	}

	@Override
	public Icon getSystemIcon(File f) throws RemoteException {
		return fileSystemView.getSystemIcon(f);
	}

	@Override
	public String getSystemTypeDescription(File f) throws RemoteException {
		return fileSystemView.getSystemTypeDescription(f);
	}

	@Override
	public boolean isDrive(File dir) throws RemoteException {
		return fileSystemView.isDrive(dir);
	}

	@Override
	public boolean isComputerNode(File dir) throws RemoteException {
		return fileSystemView.isComputerNode(dir);
	}

	@Override
	public boolean isFileSystem(File f) throws RemoteException {
		return fileSystemView.isFileSystem(f);
	}

	@Override
	public boolean isFloppyDrive(File dir) throws RemoteException {
		return fileSystemView.isFloppyDrive(dir);
	}

	@Override
	public boolean isHiddenFile(File f) throws RemoteException {
		return fileSystemView.isHiddenFile(f);
	}

	@Override
	public boolean isParent(File folder, File file) throws RemoteException {
		return fileSystemView.isParent(folder, file);
	}

	@Override
	public boolean isFileSystemRoot(File dir) throws RemoteException {
		return fileSystemView.isFileSystemRoot(dir);
	}

	@Override
	public File getChild(File parent, String filename) throws RemoteException {
		return fileSystemView.getChild(parent, filename);
	}

	@Override
	public Collection<Sequence> readFasta(File file) throws IOException {
		try(PipedInputStream pin = new PipedInputStream()){
			Collection<Sequence> result = new ArrayList<>();
			FastaReader reader = new FastaReader(file.getAbsolutePath(), pin);
			reader.start();
			ObjectInputStream oin = new ObjectInputStream(pin);
			while (true) {
				try {
					Sequence seq = (Sequence)oin.readObject();
					result.add(seq);
					System.out.println(seq);
				} catch (EOFException e) {
					System.out.println("End of Input reached");
					try {
						oin.close();
					} catch (IOException e1) {}
					break;
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				} 
			}
			return result;
		}

		
	}

	@Override
	public String blastAll(File file) throws IOException {
		System.out.println("blastAll");
		StringBuilder builder = new StringBuilder();
		try(BufferedReader reader = runBlastProteomeAlignment(file.getAbsolutePath())){
			String str;
			while((str = reader.readLine()) != null) {
				builder.append(str).append(System.lineSeparator());
				System.out.println(str);
			}
		}
		System.out.println("blastAll end");
		return builder.toString();
	}
	
	public BufferedReader runBlastProteomeAlignment(String filename) {
		String tmpDirName = "tmp";
		// erzeuge temporäre Dateien Datenbank
		initAlignmentFile(filename, tmpDirName);

		File fastaFile = new File(filename);
		String fn = fastaFile.getName();
		
		return runBlast(tmpDirName, fn);

		/*
		// starte formatdb
		ProcessBuilder fBuilder = new ProcessBuilder("formatdb", "-i", fn,
				"-p", "T", "-n", fn + "-db");
		fBuilder.directory(new File(tmpDirName).getAbsoluteFile());

		try {
			Process formatdb = fBuilder.start();
			formatdb.waitFor();

			ProcessBuilder bBuilder = new ProcessBuilder("blastall", "-p",
					"blastp", "-i", fn, "-d", fn + "-db", "-m", "0", "-e", "1");
			bBuilder.directory(new File(tmpDirName).getAbsoluteFile());

			Process blastP = bBuilder.start();
			return new BufferedReader(new InputStreamReader(
					blastP.getInputStream()));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		// temporäre Dateien löschen
		//deleteDir(new File(tmpDirName));
		//return null;
	}
	
	public static BufferedReader runBlast(String tmpDirName, String fn) {
		// starte formatdb
				ProcessBuilder fBuilder = new ProcessBuilder("formatdb", "-i", fn,
						"-p", "T", "-n", fn + "-db");
				fBuilder.directory(new File(tmpDirName).getAbsoluteFile());

				try {
					Process formatdb = fBuilder.start();
					formatdb.waitFor();

					ProcessBuilder bBuilder = new ProcessBuilder("blastall", "-p",
							"blastp", "-i", fn, "-d", fn + "-db", "-m", "0", "-e", "1");
					bBuilder.directory(new File(tmpDirName).getAbsoluteFile());

					Process blastP = bBuilder.start();
					return new BufferedReader(new InputStreamReader(
							blastP.getInputStream()));

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
	}

	private void initAlignmentFile(String fileName, String tempDirName) {
		File tmpDir = new File(tempDirName);

		if (tmpDir.exists() && tmpDir.isDirectory()) {
			deleteDir(tmpDir);

		}

		tmpDir.mkdir();

		File inFile = new File(fileName);

		System.out.println(fileName);
		System.out
				.println(tmpDir.getPath() + File.separator + inFile.getName());

		if (!inFile.exists()) {
			System.out.println("File " + fileName + " does not exist!");
		} else {
			copyFile(inFile, new File(tmpDir.getPath() + File.separator
					+ inFile.getName()));
		}

	}

	private  void deleteDir(File dir) {
		// vorher leeren
		if (!dir.isDirectory())
			return;

		for (File f : dir.listFiles()) {
			f.delete();
		}
		dir.delete();

	}

	private void copyFile(File source, File dest) {
		try {
			dest.createNewFile();
			FileOutputStream fos = new FileOutputStream(dest);
			FileInputStream fis = new FileInputStream(source);

			FileChannel fco = fos.getChannel();
			FileChannel fci = fis.getChannel();
			fci.transferTo(0, fci.size(), fco);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
