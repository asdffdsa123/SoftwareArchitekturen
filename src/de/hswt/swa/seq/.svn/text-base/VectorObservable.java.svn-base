/*
 * Created on 31.03.2009
 *
 */
package de.hswt.swa.seq;

import java.util.Collection;
import java.util.Vector;

public class VectorObservable {
	
	Vector<VectorObserver> observers = new Vector<VectorObserver>();
	
	public void addObserver(VectorObserver vo) {
		observers.add(vo);
	}
	
	public void removeObserver(VectorObserver vo) {
		observers.remove(vo);
	}
	
	public void fireUpdate(VectorObservable model, Collection v) {
		for (VectorObserver o : observers) {
			o.update(model,v);
		}
	}

}
