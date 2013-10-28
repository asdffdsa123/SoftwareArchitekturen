/*
 * Created on 07.10.2013
 *
 */
package de.hswt.swa.seq;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class FastaReader extends Thread  {
	
	private String filename;
	private PipedOutputStream pout;
	ObjectOutputStream out;
	
	public FastaReader(String fn, PipedInputStream pin) {
		filename = fn;
		try {
			pout = new PipedOutputStream(pin);
		} catch (IOException e) {
			// TODO Automatisch erstellter Catch-Block
			e.printStackTrace();
		}
	}
	
	public FastaReader(String fn) {
		filename = fn;
	}
	
	public void run() {
		try {
			out = new ObjectOutputStream(pout);
			readSeqData();
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Automatisch erstellter Catch-Block
			e.printStackTrace();
		}
	}
	
	private Collection<Sequence> readSeqData() {
		
		Collection<Sequence> result = new ArrayList<Sequence>();
		
		// erzeuge den richtigen Stream
		FileReader freader;
		try {
			freader = new FileReader(filename);
			LineNumberReader lineReader = new LineNumberReader(freader);
			
			// lese zeilenweise eine Sequenz
			String line;
			StringBuffer asSequence = new StringBuffer();
			Sequence seq = new Sequence();;
			
			while ((line = lineReader.readLine()) != null) {
				if (line.startsWith(">")) {
					if (! (asSequence.length() == 0)) {
						seq.setSequ(asSequence.toString());
						seq.setLength(asSequence.length());
						out.writeObject(seq); //über die pipe verschicken
					}
					seq = new Sequence();
					
					String[] header = line.split("\\|");
					seq.setFilename(filename);
					seq.setGi(header[1]);
					seq.setRef(header[3]);
					seq.setName(header[4]);
					seq.setDate(new Date());
					
					asSequence = new StringBuffer();
					result.add(seq);
					
				} else {
					asSequence.append(line);
				}
			}
			if (! (asSequence.length() == 0)) {
				seq.setSequ(asSequence.toString());
				seq.setLength(asSequence.length());
				out.writeObject(seq); //über die pipe verschicken
			}			
			
			
		} catch (FileNotFoundException e) {
			System.out.println("falscher Dateiname " + (new File(filename)).getAbsolutePath());
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static void main(String[] arg) {
		
		String fn = "mgProteome.fasta";
		FastaReader fastaReader = new FastaReader(fn);
		Collection<Sequence> seqs = fastaReader.readSeqData();
		for (Sequence s : seqs) {
			System.out.println(s.toString());
		}
	}

}
