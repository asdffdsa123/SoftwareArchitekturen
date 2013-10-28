package de.hswt.swa.gui;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.channels.FileChannel;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.*;

import de.hswt.swa.rmi.DemoServer;
import de.hswt.swa.rmi.RMIFileSystemView;
import de.hswt.swa.seq.BusinessLogic;

public class MainFrame extends JFrame implements ActionListener {

	private String host = "bb-x-xterm.hswt.de";
	private int port = 1280;

	JMenuItem openItem;
	JMenuItem importItem;
	JMenuItem saveItem;
	JMenuItem exit;
	JMenuItem genbank;
	JMenuItem blast;
	JMenuItem remoteBlast;
	JMenuItem remoteImport;

	JTextArea textArea;
	JTabbedPane tabPane;
	
	JDialog wartenDialog;

	BusinessLogic bLogic = new BusinessLogic();

	public MainFrame() throws HeadlessException {
		super();
		setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("HSWT Biotool");
		initFrame();
	}

	private void initFrame() {
		JMenuBar mbar = new JMenuBar();
		this.setJMenuBar(mbar);
		JMenu fileMenu = new JMenu("File");
		mbar.add(fileMenu);
		openItem = new JMenuItem("Open");
		fileMenu.add(openItem);
		openItem.addActionListener(this);

		JMenu importM = new JMenu("Import");
		fileMenu.add(importM);

		importItem = new JMenuItem("Local Import");
		importM.add(importItem);
		importItem.addActionListener(this);

		remoteImport = new JMenuItem("Remote Import");
		importM.add(remoteImport);
		remoteImport.addActionListener(this);

		saveItem = new JMenuItem("Save");
		fileMenu.add(saveItem);
		saveItem.addActionListener(this);

		JMenu blasts = new JMenu("Blast");
		fileMenu.add(blasts);

		blast = new JMenuItem("Local BlastAll");
		blasts.add(blast);
		blast.addActionListener(this);

		remoteBlast = new JMenuItem("Remote BlastAll");
		blasts.add(remoteBlast);
		remoteBlast.addActionListener(this);

		exit = new JMenuItem("Exit");
		fileMenu.add(exit);
		exit.addActionListener(this);
		JMenu browse = new JMenu("Browse");
		mbar.add(browse);
		genbank = new JMenuItem("Genbank");
		browse.add(genbank);
		genbank.addActionListener(this);
		JMenu helpMenu = new JMenu("Help");
		mbar.add(helpMenu);

		/*
		 * textArea = new JTextArea(20, 20);
		 * 
		 * add(new JScrollPane(textArea));
		 */

		tabPane = new JTabbedPane();
		this.getContentPane().add(tabPane);

		SequencesController scon = new SequencesController();
		bLogic.registerController(scon);
		SequencesViewPanel svpanel = new SequencesViewPanel(scon);
		tabPane.addTab("Sequences", svpanel);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == exit)
			System.exit(0);

		if (e.getSource() == importItem) {
			String filename = " ";
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"*.fasta", "fasta");
			fileChooser.setFileFilter(filter);
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				filename = fileChooser.getSelectedFile().getAbsolutePath();
				bLogic.readFasta(filename);
			}
		}

		if (e.getSource() == remoteImport) {
//			RemoteFileSystemView fview = new RemoteFileSystemView();
//			bLogic.getRemoteDir(fview);
			RMIFileSystemView fview;
			try {
				fview = new RMIFileSystemView(DemoServer.URI);
			} catch (Exception e1) {
				throw new RuntimeException(e1);
			}
			
			//fview.setDirContent(dir);
			// System.out.println(fview.toString());
			JFileChooser fileChooser = new JFileChooser(fview);
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"*.fasta", "fasta");
			fileChooser.setFileFilter(filter);
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File toOpen = fileChooser.getSelectedFile();
				RemoteImporter ri = new RemoteImporter(toOpen);
				ri.start();
				disableMenus();
				bLogic.readRemoteFasta(toOpen);
			}

		}

		if (e.getSource() == saveItem) {
			String filename = " ";
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"*.bseq", "bseq");
			fileChooser.setFileFilter(filter);
			int returnVal = fileChooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				filename = fileChooser.getSelectedFile().getAbsolutePath();
				System.out.println(filename);
				bLogic.saveSequence(filename);
			}
		}

		if (e.getSource() == openItem) {
			String filename = " ";
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"*.bseq", "bseq");
			fileChooser.setFileFilter(filter);
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				filename = fileChooser.getSelectedFile().getAbsolutePath();
				System.out.println(filename);
				bLogic.readSequences(filename);
			}
		}

		if (e.getSource() == blast) {
			String filename = " ";
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"*.fasta", "fasta");
			fileChooser.setFileFilter(filter);
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {

				filename = fileChooser.getSelectedFile().getAbsolutePath();
				textArea = new JTextArea(40, 40);
				tabPane.addTab("Blast output", new JScrollPane(textArea));
				tabPane.setSelectedIndex(tabPane.getTabCount()-1);
				this.validate();
				
				RunBlast rb = new RunBlast(filename);
				rb.start();
				disableMenus();

			}
		}
		
		if (e.getSource() == remoteBlast) {
			File file = chooseRemote();
			if (file != null) {

				textArea = new JTextArea(40, 40);
				tabPane.addTab("Remote Blast output", new JScrollPane(textArea));
				tabPane.setSelectedIndex(tabPane.getTabCount()-1);
				this.validate();
				
				RunRemoteBlast rb = new RunRemoteBlast(file, textArea);
				rb.start();
				System.out.println("disable menus");
				disableMenus();

				wartenDialog = new JDialog();
				// Titel wird gesetzt
				wartenDialog.setTitle("Bitte warten...");

				// H�he und Breite des Fensters werden
				// auf 200 Pixel gesetzt
				wartenDialog.setSize(400, 100);
				// Dialog wird auf modal gesetzt
				wartenDialog.setModal(true);
				// Hinzuf�gen einer Komponente,
				// in diesem Fall ein JLabel
				wartenDialog
						.add(new JLabel(
								"Blast wird ausgef�hrt, bitte haben Sie etwas Geduld..."));
				// Wir lassen unseren JDialog anzeigen
				wartenDialog.setVisible(true);
			}
		}
	}
	
	private File chooseRemote(){
		RMIFileSystemView fview;
		try {
			fview = new RMIFileSystemView(DemoServer.URI);
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}
		
		//fview.setDirContent(dir);
		// System.out.println(fview.toString());
		JFileChooser fileChooser = new JFileChooser(fview);
//		FileNameExtensionFilter filter = new FileNameExtensionFilter(
//				"*.fasta", "fasta");
//		fileChooser.setFileFilter(filter);
		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}
	
	public void disableMenus() {
		blast.setEnabled(false);
		remoteBlast.setEnabled(false);
		remoteImport.setEnabled(false);
		importItem.setEnabled(false);
	}
	
	public void enableMenus() {
		blast.setEnabled(true);
		remoteBlast.setEnabled(true);
		remoteImport.setEnabled(true);
		importItem.setEnabled(true);
	}
	
	public void closeDialog() {
		wartenDialog.setVisible(false);
	}
	
	class RunRemoteBlast extends Thread {
		
		private File filename;
		private JTextArea textArea;
		
		public RunRemoteBlast(File file, JTextArea ta) {
			filename = file;
			textArea = ta;
		}
		
		public void run() {
			bLogic.runRemoteBlast(filename, textArea);
			System.out.println("enable menus");
			closeDialog();
			enableMenus();
		}
		
	}
	
	class RunBlast extends Thread {
		
		private String filename;
		
		public RunBlast(String fn) {
			filename = fn;
		}
		
		public void run() {
			BufferedReader breader = bLogic
					.runBlastProteomeAlignment(filename);

			String line = "";
			try {
				while ((line = breader.readLine()) != null) {
					textArea.append(line + "\n");
					textArea.validate();
					System.out.println(line);
				}
				breader.close();
			} catch (Exception ex) {
			}
			enableMenus();

		}
		
	}
	
	class RemoteImporter extends Thread {
		
		private File file;
		
		public RemoteImporter(File f) {
			file = f;
		}
		
		public void run() {
			bLogic.readRemoteFasta(file);
			enableMenus();
		}
	}
}
