
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
		System.out.println(Integer.MIN_VALUE);
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
	
	public char findTheDifference(String s, String t) {
        int charInt = 0;
        for(int i=0; i<s.length(); i++){
            charInt += (int) t.charAt(i);
            charInt -= (int) s.charAt(i);
            
        }
        charInt += t.charAt(t.length()-1);
        return (char) charInt;
    }
}
