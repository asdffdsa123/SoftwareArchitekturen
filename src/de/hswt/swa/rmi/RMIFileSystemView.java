package de.hswt.swa.rmi;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

public class RMIFileSystemView extends FileSystemView{

	private FileSystemService service;
	
	public RMIFileSystemView(String servicePath) throws MalformedURLException, RemoteException, NotBoundException{
		service = (FileSystemService) Naming.lookup(servicePath);
	}
	
	@Override
	public File createNewFolder(File arg0) throws IOException {
		return service.createNewFolder(arg0);
	}

	public File[] getFiles(File dir, boolean useFileHiding) {
		try {
			return service.getFiles(dir, useFileHiding);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}

	}
	
	public File getDefaultDirectory() {
		return getHomeDirectory();
	}

	public File getHomeDirectory() {
		return new File("/home/bp4553/7sem/SoftwareArchitekturen");
	}

	public String getSystemDisplayName(File f) {
		return f.getName();
	}

	@Override
	public File getChild(File parent, String fileName) {
		try {
			return service.getChild(parent, fileName);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Icon getSystemIcon(File f) {
		try {
			return service.getSystemIcon(f);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getSystemTypeDescription(File f) {
		try {
			return service.getSystemTypeDescription(f);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isComputerNode(File dir) {
		try {
			return service.isComputerNode(dir);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isDrive(File dir) {
		try {
			return service.isDrive(dir);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isFileSystem(File f) {
		try {
			return service.isFileSystem(f);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isFileSystemRoot(File dir) {
		try {
			return service.isFileSystemRoot(dir);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isFloppyDrive(File dir) {
		try {
			return service.isFloppyDrive(dir);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isHiddenFile(File f) {
		try {
			return service.isHiddenFile(f);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isParent(File folder, File file) {
		try {
			return service.isParent(folder, file);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	public Boolean isTraversable(File f) {
		try {
			return service.isTraversable(f);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isRoot(File f) {
		try {
			return service.isRoot(f);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	public File createFileObject(String path) {
		try {
			return service.createFileObject(path);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	public File createFileObject(File dir, String filename) {
		try {
			return service.createFileObject(dir, filename);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public File getParentDirectory(File dir) {
		try {
			return service.getParentDirectory(dir);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	public File[] getRoots() {
		try {
			return service.getRoots();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}
	
	

}
