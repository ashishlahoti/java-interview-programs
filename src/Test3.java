import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Test3 {

	public static void main(String[] args) {
		System.out.println(processLogs(
				Arrays.asList("30 99 sign-in", 
						"30 105 sign-out", "12 100 sign-in", "20 80 sign-in", "12 120 sign-out", "20 101 sign-out", "21 110 sign-in"), 20));
		
		System.out.println(processLogs(
				Arrays.asList("60 12 sign-in", 
						"80 20 sign-out", "10 20 sign-in", "60 20 sign-out"), 100));
		
		System.out.println(processLogs(
				Arrays.asList("1 2 sign-out", 
						"1 1 sign-in"), 1));

	}

	public static List<String> processLogs(List<String> logs, int maxSpan) {
		List<String> users = new ArrayList<>();

		Map<String, Integer> usersByLoggingTime = new HashMap<String, Integer>();
		for (String log : logs) {
			String[] logStr = log.split(" ");
			String userId = logStr[0];
			int timestamp = Integer.parseInt(logStr[1]);
			String action = logStr[2];
			if(usersByLoggingTime.containsKey(userId)) {
				int totalLoginTime = Math.abs(usersByLoggingTime.get(userId)-timestamp);
				if(totalLoginTime <=maxSpan) {
					users.add(userId);
				}
			}else {
				usersByLoggingTime.putIfAbsent(userId, timestamp);
			}
		}
		Collections.sort(users, (s1, s2) -> Integer.parseInt(s1) - Integer.parseInt(s2));
		return users;

	}

}
