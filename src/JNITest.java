
public class JNITest {
	
	public static native int addiere(int x, int y);
	
	public static void main(String[] args){
		System.loadLibrary("nativelib.so");
		System.out.println(addiere(5, 4));
	}

}
