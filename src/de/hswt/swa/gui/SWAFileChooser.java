package de.hswt.swa.gui;

import javax.swing.*;
import java.io.*;

public class SWAFileChooser extends JFrame {
	
	private JList liste;
	
	public SWAFileChooser(File[] filelist) {
		JPanel listPanel = new JPanel();
		this.getContentPane().add(listPanel);
		
		liste = new JList(filelist);
		listPanel.add(liste);
	}

}
