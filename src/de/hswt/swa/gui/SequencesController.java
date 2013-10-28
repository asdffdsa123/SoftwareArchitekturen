/*
 * Created on 30.03.2009
 *
 */
package de.hswt.swa.gui;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import de.hswt.swa.seq.SequenceModel;
import de.hswt.swa.seq.VectorObservable;
import de.hswt.swa.seq.VectorObserver;

public class SequencesController implements VectorObserver {
	
	Vector<VectorDisplayable> views = new Vector<VectorDisplayable>();

	//@Override
	public void update(VectorObservable o, Collection arg) {
			updateDisplays(arg);
	}
	
	public void registerDisplay(VectorDisplayable view) {
		views.add(view);
	}

	private void updateDisplays(Collection arg) {
		for (VectorDisplayable v : views) {
			v.display(arg);
		}
		
	}

}
