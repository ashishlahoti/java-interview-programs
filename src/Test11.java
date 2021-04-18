import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Test11 {

	public static void main(String[] args) {
		List<Integer> list = Arrays.asList(1, 3, 3, 5, 2, 1, 1);

		List<Integer> sortedList = list.stream().sorted().collect(Collectors.toList());

		int median = sortedList.get(Math.round(list.size() / 2));
		System.out.println("Median: " + median);

	}
}

final class ImmutableList<T extends Cloneable> {

	private final List<T> list;

	ImmutableList(List<T> list) {
		this.list = list.stream().collect(Collectors.toList());
		
	}
	
	public List<T> getList(){
		return list.stream().collect(Collectors.toList());
	}
	
	public T get(int index) {
		return list.get(index);
	}
	
	/**
	public void add(T value) {
		list.add(value);
	}
	*/
}
