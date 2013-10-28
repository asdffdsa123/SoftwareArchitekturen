package de.hswt.swa.rmi;

import java.io.File;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.swing.Icon;

public interface FileSystemService extends Remote{

	File[] getFiles(File dir, boolean useFileHiding) throws RemoteException;

	File getHomeDirectory() throws RemoteException;

	File createFileObject(String path) throws RemoteException;

	File createFileObject(File dir, String filename) throws RemoteException;

	File[] getRoots() throws RemoteException;

	File createNewFolder(File arg0) throws IOException, RemoteException;

	File getParentDirectory(File dir) throws RemoteException;

	Boolean isTraversable(File f) throws RemoteException;

	boolean isRoot(File f) throws RemoteException;

	File getDefaultDirectory() throws RemoteException;

	Icon getSystemIcon(File f) throws RemoteException;

	String getSystemTypeDescription(File f) throws RemoteException;

	boolean isDrive(File dir) throws RemoteException;

	boolean isComputerNode(File dir) throws RemoteException;

	boolean isFileSystem(File f) throws RemoteException;

	boolean isFloppyDrive(File dir) throws RemoteException;

	boolean isHiddenFile(File f) throws RemoteException;

	boolean isParent(File folder, File file) throws RemoteException;

	boolean isFileSystemRoot(File dir) throws RemoteException;
	
	File getChild(File parent, String filename) throws RemoteException;

}
