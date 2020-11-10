
public class Test {

	public static void main(String[] args) {
		System.out.println(test("aabbb"));
		System.out.println(test("ba"));
		System.out.println(test("aaa"));
		System.out.println(test("b"));
		System.out.println(test("abba"));
		System.out.println(test(""));
		System.out.println(test("a"));
		System.out.println(test(null));
	}
	
	public static boolean test(String s) {
		if(s == null || s.length() == 0) {
			return false;
		}
		boolean isBOccured = false;
		for(int i=0; i< s.length(); i++) {
			if(s.charAt(i) == 'a' && isBOccured) {
				return false;
			}else if(s.charAt(i) == 'b') {
				isBOccured = true;
			}
		}		
		return true;
	}
}
