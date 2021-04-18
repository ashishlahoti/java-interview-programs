
public class Test4 {

	
	public static void main(String[] args) {
		System.out.println(getTime("BZA"));
		System.out.println(getTime("AZGB"));
	}
	
	public static long getTime(String s) {
	   char startChar = 'A';
	   long count = 0;
	   for(int i=0; i<s.length(); i++) {
		   char c = s.charAt(i);
		   int diff = Math.abs(c-startChar);
		   diff = Math.min(diff, 26-diff);
		   startChar = c;
		   //System.out.println(diff);
		   count += diff;
	   }

	   return count;
	}
}
