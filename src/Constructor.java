
public class Constructor {

	static String str;
	
	public void Constructor() {
		System.out.println("Inside");
		str = "Hello world";
	}
	
	public static void main(String[] args) {
		Constructor c = new Constructor();
		System.out.println(str);
	}

}
