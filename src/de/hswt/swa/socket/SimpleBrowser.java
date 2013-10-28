package de.hswt.swa.socket;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

public class SimpleBrowser extends JFrame implements ActionListener  {

		private JTextField urlField, patternField;
		private JTextPane htmlDisplay;
		
		private JButton exit, search;
		private String pattern = new String("Bioinformatik");
		private URL Url;
		
		public SimpleBrowser(int h, int b) {
			this.setSize(h,b);
			construct();
		}
		
		public SimpleBrowser(int h, int b, String url) {
			this(h,b);
			urlField.setText(url);
			
			try {
				this.Url = new URL(url);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		
		private void construct() {

			//auf dem Frame liegt ein Panel mit 3 Unterpanels
			JPanel mainPanel = new JPanel();
			mainPanel.setLayout(new BorderLayout());
			this.add(mainPanel, BorderLayout.CENTER);

			// das erste Unterpanel enthält eine Komponente für die Texteingabe
			JPanel textP = new JPanel();
			urlField = new JTextField(30);
			urlField.setEditable(true);
			urlField.addActionListener(this);
			TitledBorder title = BorderFactory.createTitledBorder("URL:");
			urlField.setBorder(title);
			textP.add(urlField);
			

			htmlDisplay = new JTextPane();
			htmlDisplay.setSize(300,500);
			htmlDisplay.setPreferredSize(new Dimension(500,500));
			
			JScrollPane scrollPane = new JScrollPane(htmlDisplay);
			htmlDisplay.setEditable(false);
			TitledBorder title2 = BorderFactory.createTitledBorder("Web Browser:");
			htmlDisplay.setBorder(title2);
			mainPanel.add(scrollPane, BorderLayout.CENTER);
			mainPanel.add(textP, BorderLayout.NORTH);

			JPanel bottomP = new JPanel();
			bottomP.setLayout(new BorderLayout());
			
			patternField = new JTextField(20);
			patternField.setEditable(true);
			patternField.addActionListener(this);
			TitledBorder title3 = BorderFactory.createTitledBorder("Suchmuster:");
			patternField.setBorder(title3);
			bottomP.add(patternField, BorderLayout.CENTER);
			
			exit = new JButton("Exit");
			exit.addActionListener(this);
			
			search  = new JButton("Suche");
			search.addActionListener(this);
			
			JPanel buttonP = new JPanel();
			buttonP.setLayout(new FlowLayout());
			buttonP.add(search);
			buttonP.add(exit);
			
			bottomP.add(buttonP, BorderLayout.SOUTH);
			
			mainPanel.add(bottomP, BorderLayout.SOUTH);
			
			this.setVisible(true);
			
		}
		
		public void actionPerformed(ActionEvent arg) {
			
			if (arg.getSource() == exit) {
				System.exit(0);
			}
			if (arg.getSource() == urlField) {
				
					//... anzeigen
				try {
					htmlDisplay.setText(" ");
					htmlDisplay.setPage(new URL(urlField.getText()));
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				urlField.setText(" ");
				validate();
			}
			if (arg.getSource() == patternField) {
				pattern = patternField.getText();
			}
			if (arg.getSource() == search) {
				pattern = patternField.getText();
				// highlighten
			}

		}
		


}
