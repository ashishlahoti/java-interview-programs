import java.util.HashSet;
import java.util.Set;

public class SubstringCalculator {
	
	public static void main(String[] args) {
		System.out.println(substringCalculator("abcde")); //15
		System.out.println(substringCalculator("kincenvizh")); //53
		// 499011
		System.out.println(substringCalculator("ghaqjdrmnegmrlrlfpjmnnngpwalzknsencuzwsnhfltwohdgbmvfuwtquosrnyerucntxxkfqehjqygcarxogvcfkljzbzutxphpyykapncjfclnhndzxghelyvzpylazhuutmcquusexzbhsfsmbnlvnlemzvfqbfzwquairhpylnbvyhiyamztlhfchhbwrqddmuzsprfdwuqqchcpeakkexackwwzihkfenwzwckynymgqydvjtovaoezkjjurylqcuonsujycziobnfnmuwnoxcdtahpituykvgpyyshvukrstcbmnsqtjseflwywnslmvnqrtnzkyaddkjamrezprqgoenzsdryygbkeahfiduozpwkrgmatszaxmwodsqiocvagbvxyqotpaujnqvqgjmfxnxhfbwqjpgodlxdrxpjpmzeabpgqrzpxomniknjkdiwtfgyvwvekrnoupwkcbtmpcfamzrghgrznuedkybmfwctdghcfawajlxfkzhdamuygjbcwnyglkjlfmpxfdtovkqbshhrfrnyjrgxgiozsuuncnwofkqzsypwgeikpfbhryhpszegdfajzvqlwwqlnvdtdiuckcvvosrdweohnmawqonjbxyjjhlccuteeshfrxxdhzgakwjqbymnaeudcmibsytyajsgdpfvrutcpglzxdevenevmkgalcrpknuvcrnkuboennhyzirfwvtozzijujsckbxqpocakzrbwgpqgjjmsrtwmvhwyraukbuxfvebeylfpipzwjdzlmgslbtwzataxgqpasrssnfwndldwkdutdqcmcpyanrbdsxrvcvpsywjambtbzlcrvzesuhvyvwwuwwdznigxjxknfajpknqutfvvqynkpvkzgypasevrpxofbymdzcitoqolwqegocuyqsexhumzmckzuuwkamolbltlifongpvkcnrnnuplftqbxpdnegdqlymftqyrxcnzmu")); //15
	}
	
	public static long substringCalculator(String s) {
		if (s == null || s.length() == 0)
			return 0;
		System.out.println(s);
		int size = s.length();
		long combi = size*(size+1)/2;
		
		Set<String> set = new HashSet<>();
		substringCalculator(s, set);
		return set.size();
	}
	
	/**
	public static long substringCalculator(String s) {
		if (s == null || s.length() == 0)
			return 0;
		System.out.println(s);
		Set<String> set = new HashSet<>();
		substringCalculator(s, set);
		return set.size();
	}
	*/

	public static void substringCalculator(String s, Set<String> set) {
		if (!set.add(s)) {
			return;
		}
		if (s.length() == 1) {
			return;
		}
		substringCalculator(s.substring(0, s.length() - 1), set);
		substringCalculator(s.substring(1, s.length()), set);
	}
}
