package de.hswt.swa.jni;

import java.io.File;

public class JNITest {
	
	public static native int addiere(int x, int y);
	
	public static native int strlen(String str);
	
	public static native String getDate();
	
	public static native long getMemorySize();
	
	public static void main(String[] args){
		System.load(new File("libnative.so").getAbsolutePath());
		System.out.println(addiere(5, 4));
		System.out.println(strlen("Blabla"));
		System.out.println(getDate());
		System.out.println(String.format("Total memory: %d", getMemorySize()));
		byte[] bla = new byte[1024 * 1024];
		System.out.println(String.format("Total memory: %d", getMemorySize()));
		System.out.println(bla);
	}

}
