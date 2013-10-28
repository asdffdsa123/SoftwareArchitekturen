/*
 * Created on 31.03.2009
 *
 */
package de.hswt.swa.seq;

import java.util.Collection;
import java.util.Vector;


public interface VectorObserver {
	
	public void update(VectorObservable o,Collection arg);

}
