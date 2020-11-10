
public class Test2 {

	public static void main(String[] args) {
		System.out.println(test(268) + " " + test(-268));
		//System.out.println(test(670) + " " + test(-670));
		System.out.println(test(-0) + " " );
		//System.out.println(test(01) + " " + test(-01));
		//System.out.println(test(1) + " " + test(-1));
		//System.out.println(test(6) + " " + test(-6));
		//System.out.println(test(111) + " " + test(-111));
		//System.out.println(test(999) + " " + test(-999));
		//System.out.println(test(51) + " " + test(-51));
		//System.out.println(test(15) + " " + test(-15));
		//System.out.println(test(55) + " " + test(-55));
		//System.out.println(test(59) + " " + test(-59));
		//System.out.println(test(95) + " " + test(-95));
	}
	
	public static int test(int n) {
		String intStr = Integer.toString(n);
		String output = "";
		boolean isNegative = intStr.charAt(0) == '-' ? true: false;
		int i= isNegative ? 1 : 0;
		while(i < intStr.length()) {
			int num = intStr.charAt(i) - '0';
			if(num < 5) {
				if(isNegative) {
					output += "" + num;
				}else {
					output += "5" + num;
					break;
				}				
			}else {
				if(isNegative) {
					output += "5" + num;
					break;
				}else {
					output += num + "";
				}				
			}
			i++;
		}
		
		output += (i >= intStr.length()) ? "5" : intStr.substring(i+1);
		if(isNegative) {
			output = "-" + output;
		}
		return Integer.valueOf(output);
	}
}
