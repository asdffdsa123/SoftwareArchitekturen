package de.hswt.swa.socket;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PipedInputStream;
import java.net.*;
import java.util.Properties;
import java.io.*;
import javax.swing.filechooser.*;

import de.hswt.swa.seq.BusinessLogic;
import de.hswt.swa.seq.FastaReader;
import de.hswt.swa.seq.Sequence;

public class FastaBlastServer {

	private ServerSocket server;
	private Properties properties;

	public FastaBlastServer() throws Exception {

		properties = new Properties();
		BufferedInputStream stream = new BufferedInputStream(
				new FileInputStream("fastaBlast.properties"));
		properties.load(stream);
		stream.close();

		int port = Integer.parseInt(properties.getProperty("Port"));
		server = new ServerSocket(port);

		while (true) {
			Socket client = server.accept();
			System.out.println("neue Anfrage bekommen...");

			OutputStream os = client.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);

			InputStream is = client.getInputStream();
			BufferedReader rin = new BufferedReader(new InputStreamReader(is));

			String s = rin.readLine();
			System.out.println("Anfrage: " + s);
			if (s.equalsIgnoreCase("getdir")) {
				File f = new File(properties.getProperty("fastaDir"));
				System.out.println("Sending directory information from " + f);
				System.out.println(f.listFiles());
				oos.writeObject(f);
				oos.writeObject(f.listFiles());
				RemoteFileSystemConnection rfc = new RemoteFileSystemConnection(
						new ObjectInputStream(is), oos);
				rfc.start();
				rfc.join();
			}

			if (s.equalsIgnoreCase("readFasta")) {
				String filename = rin.readLine();
				filename = filename.replace("\\", "/");
				File f = new File(properties.getProperty("fastaDir")
						+ File.pathSeparator + filename);

				PipedInputStream pin = new PipedInputStream();
				// FastaReader wird in einem eigenen Thread aufgerufen
				FastaReader freader = new FastaReader(filename, pin);
				freader.start();

				try {
					ObjectInputStream oin = new ObjectInputStream(pin);

					// Kommunikation: verwende Pipe-Stream
					while (true) {
						try {
							Sequence seq = (Sequence) oin.readObject();
							oos.writeObject(seq);
							System.out.println("Verschickte Sequenz: " + seq);
						} catch (EOFException e) {
							System.out.println("End of Input reached");
							oin.close();
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} // end-if

			if (s.equalsIgnoreCase("blastall")) {
				try {
					String tmpDirName = properties.getProperty("tmpDir");
					File tmpDir = new File(tmpDirName);
					if (!tmpDir.isDirectory())
						tmpDir.mkdir();
					File tmpFasta = new File(tmpDir.getPath() + File.separator
							+ "tmp.fasta");

					FileOutputStream fileOs = new FileOutputStream(tmpFasta);
					PrintWriter filePw = new PrintWriter(fileOs);

					System.out.println("receive fasta file .. ");
					String line;
					while (!(line = rin.readLine())
							.equalsIgnoreCase("transferend")) {
						filePw.println(line);
					}
					filePw.close();

					System.out
							.println("fasta file received and saved, starting blast...");
					BufferedReader blastReader = BusinessLogic.runBlast(
							tmpDirName, "tmp.fasta");
					PrintWriter socketWriter = new PrintWriter(os);
					System.out.println("write blast output ...");
					while ((line = blastReader.readLine()) != null) {
						socketWriter.println(line);
					}
					blastReader.close();

				} catch (Exception e) {
				}
			}
			os.close();
			is.close();

		} // end-while
	}

	public static void main(String[] arg) throws Exception {
		FastaBlastServer fServer = new FastaBlastServer();
	}

	class RemoteFileSystemConnection extends Thread {
		private ObjectOutputStream os;
		private ObjectInputStream is;

		public RemoteFileSystemConnection(ObjectInputStream i,
				ObjectOutputStream o) {
			os = o;
			is = i;
		}

		public void run() {

			while (true) {
				System.out.println("in the loop ...");
				try {
					String s = (String) is.readObject();
					if (s.equalsIgnoreCase("createFO")) {
						System.out.println("createFileOBject called...");
						String path = (String) is.readObject();
						System.out.println("createFileOBject called for "
								+ path);
						FileSystemView view = FileSystemView
								.getFileSystemView();
						File f = view.createFileObject(path);
						os.writeObject(f);
						os.flush();
					}
					if (s.equalsIgnoreCase("createFO2")) {
						System.out.println("createFileOBject2 called...");
						File dir = (File) is.readObject();
						String filename = (String) is.readObject();
						System.out.println("createFileOBject called for "
								+ filename);
						FileSystemView view = FileSystemView
								.getFileSystemView();
						File f = view.createFileObject(dir, filename);
						os.writeObject(f);
						os.flush();
					}
					if (s.equalsIgnoreCase("getPD")) {
						System.out.println("getParentDir called...");
						File dir = (File) is.readObject();
						FileSystemView view = FileSystemView
								.getFileSystemView();
						File f = view.getParentDirectory(dir);
						os.writeObject(f);
						os.flush();
					}
					if (s.equalsIgnoreCase("getRoots")) {
						System.out.println("getRoots called...");
						FileSystemView view = FileSystemView
								.getFileSystemView();
						File[] files = view.getRoots();
						os.writeObject(files);
						os.flush();
					}
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
			try {
				os.close();
				is.close();
			} catch (Exception e) {
			}
		}
	}

}
