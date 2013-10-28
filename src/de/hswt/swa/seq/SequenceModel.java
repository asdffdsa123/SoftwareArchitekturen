/*
 * Created on 07.10.2013
 *
 */
package de.hswt.swa.seq;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.util.ArrayList;
import java.util.Collection;

public class SequenceModel extends VectorObservable {

	private Collection<Sequence> sequences = new ArrayList<Sequence>();
	
	public void setSequences(Collection<Sequence> sequences){
		this.sequences = sequences;
		fireUpdate(this, sequences);
	}

	public boolean readFastaFile(String filename) {

		PipedInputStream pin = new PipedInputStream();
		// FastaReader wird in einem eigenen Thread aufgerufen
		FastaReader freader = new FastaReader(filename, pin);
		freader.start();

		try {
			ObjectInputStream oin = new ObjectInputStream(pin);

			// Kommunikation: verwende Pipe-Stream
			while (true) {
				try {
					Sequence seq = (Sequence)oin.readObject();
					sequences.add(seq);
					System.out.println(seq);
				} catch (EOFException e) {
					System.out.println("End of Input reached");
					oin.close();
					break;
				}
			}
		} catch (Exception e) {
			// TODO Automatisch erstellter Catch-Block
			e.printStackTrace();
			return false;
		}

		this.fireUpdate(this, sequences);
		return true;
	}

	public boolean saveSequence(String filename) {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			
			os.writeObject(sequences);
			
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public void readSequences(ObjectInputStream ois) {
		while (true) {
			try {
				sequences.add((Sequence)ois.readObject());
				fireUpdate(this, sequences);
			} catch (EOFException e) {
				System.out.println("End of Input reached");
				break;
			} catch (Exception e1 ) {
				System.out.println("Exception " + e1.toString() + " beim Einlesen einer Sequenz");
				break;
			}
		}
	}

	public boolean readSequences(String filename) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			sequences = (ArrayList)ois.readObject();
			
			ois.close();
			
			fireUpdate(this, sequences);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return false;
	}

}
