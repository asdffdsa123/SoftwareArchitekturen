/*
 * Created on 30.03.2009
 *
 */
package de.hswt.swa.gui;

import java.awt.Dimension;
import java.util.Collection;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;


public class SequencesViewPanel extends JPanel implements VectorDisplayable {
	
	SequencesController seqCon;
	JList list;
	JScrollPane listScroller;
	DefaultListModel model;
	
	
	public SequencesViewPanel(SequencesController sc) {
		seqCon = sc;
		seqCon.registerDisplay(this);
		initComponents();
	}

	private void initComponents() {
		model = new DefaultListModel();
		model.addElement("Sequences...");
		
		list = new JList(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1)	;
		
		listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(700, 400));
		
		this.add(listScroller);

	}

	//@Override
	public void display(Collection data) {
		model.removeAllElements();
		for (Object o : data)
			model.addElement(o);
		this.validate();
	}

}
