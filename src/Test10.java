import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Test10 {

	public static void main(String[] args) throws IOException {
		InputStreamReader reader = new InputStreamReader(System.in, StandardCharsets.UTF_8);
		BufferedReader in = new BufferedReader(reader);
		String line;
		// while ((line = in.readLine()) != null) {
		line = "28,54,812,438";
		List<Integer> list = Stream.of(line.split(",")).map(Integer::parseInt).collect(Collectors.toList());
		Double dampingFactor = list.stream().max(Comparator.comparing(Integer::valueOf)).map(max -> 100/ (double)max).get();	
		String output = list.stream().map(i -> Math.round(i * dampingFactor)).map(Object::toString).collect(Collectors.joining(",")).toString();
		System.out.println(output);
	}
}
