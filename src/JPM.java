import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JPM {

	public static void main(String[] args) {
		List<String> list = Arrays.asList("A1" ,"A2", "A3", "A1", "B1", "B1", "B1", "C1", "C3");
		Map<String, Integer> map = new HashMap<>();
		list.stream().forEach((e) -> {
			map.put(e, map.getOrDefault(e, 0)+1);
		});
		
		System.out.println(map);
	}
}
