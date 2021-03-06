package de.hswt.swa.gui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.filechooser.FileSystemView;

public class RemoteFileSystemView extends FileSystemView {

	private File[] dirContent;
	private File directory;
	private ObjectInputStream is;
	private ObjectOutputStream os;

	public void setDirContent(File[] files) {
		dirContent = files;
	}

	public void setDir(File dir) {
		directory = dir;
	}

	public void setStreams(ObjectInputStream i, ObjectOutputStream o) {
		is = i;
		os = o;
	}

	@Override
	public File createNewFolder(File arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public File[] getFiles(File dir, boolean useFileHiding) {
		System.out.println("getfiles");
		return dirContent;

	}

	public File getDefaultDirectory() {
		System.out.println("getdirectory " + directory.getName());
		return directory;
	}

	public File getHomeDirectory() {
		System.out.println("gethomedirectory " + directory.getPath());
		return directory;
	}

	public String getSystemDisplayName(File f) {
		return f.getName();
	}

	public Boolean isTraversable(File f) {
		return false;
	}

	public boolean isRoot(File f) {
		return false;
	}

	public File createFileObject(String path) {
		System.out.println("createFO " + path);

		try {
			os.writeObject("createFO");
			os.flush();
			os.writeObject(path);
			os.flush();
			File f = (File) is.readObject();
			return f;
		} catch (Exception e) {
			return null;
		}
	}

	public File createFileObject(File dir, String filename) {
		System.out.println("createFO2 " + dir + " " + filename);

		try {
			os.writeObject("createFO2");
			os.flush();
			os.writeObject(dir);
			os.writeObject(filename);
			File f = (File) is.readObject();
			System.out.println(f.getPath());
			return f;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public File[] getRoots() {
		System.out.println("Roots ");
		try {
			os.writeObject("getRoots");
			os.flush();
			
			File[] f = (File[]) is.readObject();
			return f;
		} catch (Exception e) {
			e.printStackTrace();
			return null;			
		}
	}

	public File getParentDirectory(File dir) {
		/*if (dir != null) System.out.println("getParentDir " + dir.getPath());
		
		try {
			os.writeObject("getPD");
			os.flush();
			os.writeObject(dir);
			File f = (File) is.readObject();
			//System.out.println(f.getPath());
			return f;
		} catch (Exception e) {
			e.printStackTrace();
			return null;			
		}*/
		return dir;
	}

	public void close() {
		try {
			os.close();
		is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
