package de.hswt.swa.rmi;
import java.io.File;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

import de.hswt.swa.seq.Sequence;


public interface FastaBlastService extends Remote{
	
	public Collection<Sequence> readFasta(File file) throws RemoteException, IOException;

	public String blastAll(File file) throws RemoteException, IOException;
}
