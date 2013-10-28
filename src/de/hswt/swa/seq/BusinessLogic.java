package de.hswt.swa.seq;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;
import java.io.*;

import javax.swing.JTextArea;
import javax.swing.filechooser.*;

import de.hswt.swa.gui.RemoteFileSystemView;
import de.hswt.swa.gui.SequencesController;
import de.hswt.swa.rmi.DemoServer;
import de.hswt.swa.rmi.FastaBlastService;

public class BusinessLogic {

	private SequenceModel model = new SequenceModel();

	private Properties properties;

	private Socket fastaServer;
	
	public BusinessLogic(){
			try {
				properties = new Properties();

				BufferedInputStream stream = new BufferedInputStream(
						new FileInputStream("server.properties"));
				properties.load(stream);
				stream.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
	}

	private Socket getFastaBlastServer() {

		try {
			String host = properties.getProperty("fastaHost");
			int port = Integer.parseInt(properties.getProperty("fastaPort"));
			fastaServer = new Socket(host, port);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return fastaServer;
	}

	public boolean readSequences(String filename) {
		return model.readSequences(filename);
	}

	public boolean readFasta(String filename) {
		return model.readFastaFile(filename);
	}

	public boolean saveSequence(String filename) {
		return model.saveSequence(filename);
	}

	public void startGenbank() {
		Runtime rt = Runtime.getRuntime();
		try {
			Process p = rt.exec("firefox http://www.ncbi.nlm.nih.gov/Genbank/");
			// Process p =
			// rt.exec("rundll32 url.dll,FileProtocolHandler http://www.ncbi.nlm.nih.gov/Genbank/");
			p.waitFor();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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

	public void registerController(VectorObserver scon) {
		model.addObserver(scon);

	}

	public void getRemoteDir(RemoteFileSystemView view) {
		try {
			Socket socket = this.getFastaBlastServer();
			InputStream is = socket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			pw.println("getdir");
			pw.flush();
			view.setStreams(ois, new ObjectOutputStream(os));
			File dir = (File)ois.readObject();
			view.setDir(dir);
			File[] f = (File[]) ois.readObject();
			view.setDirContent(f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	private FastaBlastService getFastaBlastService(){
		try {
			return (FastaBlastService) Naming.lookup(DemoServer.URI);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			throw new RuntimeException(e);
		}
	}

	public void readRemoteFasta(File toOpen) {
		FastaBlastService service = getFastaBlastService();
		try {
			model.setSequences(service.readFasta(toOpen));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
//		try {
//			Socket socket = this.getFastaBlastServer();
//
//			OutputStream os = socket.getOutputStream();
//			PrintWriter pw = new PrintWriter(os);
//
//			InputStream is = socket.getInputStream();
//			ObjectInputStream ois = new ObjectInputStream(is);
//
//			pw.println("readFasta");
//			pw.flush();
//			String filepath =toOpen.getPath().replace("\\","/");
//			System.out.println(filepath);
//			pw.println(filepath);
//			pw.flush();
//
//			model.readSequences(ois);
//			is.close();
//			os.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}



	public void runRemoteBlast(File file, JTextArea area) {
		FastaBlastService service = getFastaBlastService();
		try {
			area.setText(service.blastAll(file));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
//		try {
//			Socket socket = this.getFastaBlastServer();
//			
//			OutputStream os = socket.getOutputStream();
//			PrintWriter pw = new PrintWriter(os);
//
//			InputStream is = socket.getInputStream();
//			//ObjectInputStream ois = new ObjectInputStream(is);
//
//			pw.println("blastall");
//			pw.flush();
//			
//			FileInputStream fin = new FileInputStream(new File(filename));
//			BufferedReader frin = new BufferedReader(new InputStreamReader(fin));
//			
//			System.out.println("transfer the fasta file ..");
//			String line;
//			while ((line = frin.readLine()) != null) {
//				pw.println(line);
//			}
//			pw.println("transferend");
//			pw.flush();
//			
//			System.out.println("receive blast results..");
//			area.setText("");
//			area.validate();
//			BufferedReader bris = new BufferedReader(new InputStreamReader(is));
//			while ((line = bris.readLine()) != null) {
//				
//				area.append(line+"\n");
//				area.validate();
//			}
//			
//		} catch (Exception e) {
//		}
//		
	}
}
