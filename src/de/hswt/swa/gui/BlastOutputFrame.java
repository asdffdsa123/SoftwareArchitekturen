package de.hswt.swa.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class BlastOutputFrame extends JFrame {
	
	JPanel panel;
	JScrollPane scroller;
	JTextArea text;
	
	public BlastOutputFrame() {
		panel = new JPanel();
		
		text = new JTextArea();
		text.setSize(400, 400);
		
		//panel.add(text);
		scroller = new JScrollPane(text);
		this.getContentPane().add(scroller);
		
		this.setTitle("Blast Output");
		this.setSize(400,400);
	}
	
	public JTextArea getTextArea() {
		return text;
	}

}
