import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;

public class Solution {
	public static void main(String[] args) throws IOException {
		SetAssociativeCacheRunner.parseInput(System.in);
	}

	/**
	 * Parses Test Case input to instantiate and invoke a SetAssociativeCache
	 *
	 * NOTE: You can typically ignore anything in here. Feel free to collapse...
	 */
	static class SetAssociativeCacheRunner {
		public static void parseInput(InputStream inputStream) throws IOException {
			InputStreamReader inputReader = new InputStreamReader(inputStream);
			BufferedReader reader = new BufferedReader(inputReader);

			String line;
			int lineCount = 0;
			SetAssociativeCache<String, String> cache = null;

			while (!isNullOrEmpty(line = reader.readLine())) {
				lineCount++;
				OutParam<String> replacementAlgoName = new OutParam<>();

				if (lineCount == 1) {

					cache = createCache(line, replacementAlgoName);

				} else {

					// All remaining lines invoke instance methods on the SetAssociativeCache
					Object retValue = SetAssociativeCacheFactory.InvokeCacheMethod(line, cache);

					// Write the method's return value (if any) to stdout
					if (retValue != null) {
						System.out.println(retValue);
					}
				}
			}
		}
	}

	private static SetAssociativeCache<String, String> createCache(String inputLine,
			OutParam<String> replacementAlgoName) {
		String[] cacheParams = Arrays.stream(inputLine.split(",")).map(s -> s.trim()).toArray(n -> new String[n]);
		int setCount = Integer.parseInt(cacheParams[0]);
		int setSize = Integer.parseInt(cacheParams[1]);
		replacementAlgoName.value = cacheParams[2];
		return SetAssociativeCacheFactory.CreateStringCache(setCount, setSize,
				ReplacementAlgo.getAlgo(replacementAlgoName.value));
	}

	// ############################ BEGIN Solution Classes
	// ############################

	/**
	 * NOTE: You are free to modify anything below, except for class names and
	 * generic interface. Other public interface changes may require updating one or
	 * more of the helper classes above for test cases to run and pass.
	 * <p>
	 * A Set-Associative Cache data structure with fixed capacity.
	 * <p>
	 * - Data is structured into setCount # of setSize-sized sets. - Every possible
	 * key is associated with exactly one set via a hashing algorithm - If more
	 * items are added to a set than it has capacity for (i.e. > setSize items), a
	 * replacement victim is chosen from that set using an LRU algorithm.
	 * <p>
	 * NOTE: Part of the exercise is to allow for different kinds of replacement
	 * algorithms...
	 */

	public static class SetAssociativeCache<TKey, TValue> {
		int Capacity;
		int SetSize;
		int SetCount;
		CacheSet<TKey, TValue>[] Sets;
		IReplacementAlgo<TKey> replacementAlgo;

		public SetAssociativeCache(int associativity, int cacheSize, int blockSize, ReplacementAlgo replacementAlgo) {
			this(associativity, cacheSize / blockSize, replacementAlgo);
		}

		public SetAssociativeCache(int setCount, int setSize, ReplacementAlgo replacementAlgo) {
			this.SetCount = setCount;
			this.SetSize = setSize;
			this.Capacity = this.SetCount * this.SetSize;

			if (replacementAlgo == null) {
				replacementAlgo = ReplacementAlgo.LruAlgorithm;
			}

			this.replacementAlgo = ReplacementAlgoFactory.createReplacementAlgo(replacementAlgo);

			// Initialize the sets
			this.Sets = new CacheSet[this.SetCount];
			for (int i = 0; i < this.SetCount; i++) {
				Sets[i] = new CacheSet<>(setSize, this.replacementAlgo);
			}
		}

		/** Gets the value associated with `key`. Throws if key not found. */
		public TValue get(TKey key) {
			int setIndex = this.getSetIndex(key);
			CacheSet<TKey, TValue> set = this.Sets[setIndex];
			return set.get(key);
		}

		/**
		 * Adds the `key` to the cache with the associated value, or overwrites the
		 * existing key. If adding would exceed capacity, an existing key is chosen to
		 * replace using an LRU algorithm (NOTE: It is part of this exercise to allow
		 * for more replacement algos)
		 */
		public void set(TKey key, TValue value) {
			int setIndex = this.getSetIndex(key);
			System.out.println("set " + key + "+" + value + " = " + setIndex);
			CacheSet<TKey, TValue> set = this.Sets[setIndex];
			set.set(key, value);
		}

		/** Returns the count of items in the cache */
		public int getCount() {
			int count = 0;
			for (int i = 0; i < this.Sets.length; i++) {
				count += this.Sets[i].Count;
			}
			return count;
		}

		/**
		 * Returns `true` if the given `key` is present in the set; otherwise, `false`.
		 */
		public boolean containsKey(TKey key) {
			int setIndex = this.getSetIndex(key);
			CacheSet<TKey, TValue> set = this.Sets[setIndex];
			return set.containsKey(key);
		}

		/** Maps a key to a set */
		private int getSetIndex(TKey key) {
			int c = Integer.MAX_VALUE;
			int s = -1;
			for (int i = 0; i < this.Sets.length; i++) {
				if (this.Sets[i].containsKey(key)) {
					return i;
				}
				if (this.Sets[i].Count < c) {
					c = this.Sets[i].Count;
					s = i;
				}
			}
			return s;
		}
	}

	/**
	 * An internal data structure representing one set in a N-Way Set-Associative
	 * Cache
	 */
	static class CacheSet<TKey, TValue> {
		int Capacity;
		CacheItem<TKey, TValue>[] Store;
		LinkedList<TKey> UsageTracker;
		public int Count;
		IReplacementAlgo<TKey> replacementAlgo;

		public CacheSet(int capacity, IReplacementAlgo<TKey> replacementAlgo) {
			this.Capacity = capacity;
			this.UsageTracker = new LinkedList<>();
			this.Store = new CacheItem[capacity];
			this.replacementAlgo = replacementAlgo;
		}

		/** Gets the value associated with `key`. Throws if key not found. */
		public TValue get(TKey key) {
			// If the key is present, update the usage tracker
			if (this.containsKey(key)) {
				this.recordUsage(key);
			} else {
				throw new RuntimeException(String.format("The key '%s' was not found", key));
			}

			return this.Store[this.findIndexOfKey(key)].value;
		}

		/**
		 * Adds the `key` to the cache with the associated value, or overwrites the
		 * existing key. If adding would exceed capacity, an existing key is chosen to
		 * replace using an LRU algorithm (NOTE: It is part of this exercise to allow
		 * for more replacement algos)
		 */
		public void set(TKey key, TValue value) {
			int indexOfKey = this.findIndexOfKey(key);

			if (indexOfKey >= 0) {

				this.Store[indexOfKey].value = value;

			} else {
				int indexToSet;
				// If the set is at it's capacity
				if (this.Count == this.Capacity) {
					// Choose the Least-Recently-Used (LRU) item to replace, which will be at the
					// tail of the usage tracker
					// TODO: Factor this logic out to allow for custom replacement algos
					TKey keyToReplace = this.replacementAlgo.evictKey(this.UsageTracker);
					indexToSet = this.findIndexOfKey(keyToReplace);

					// Remove the existing key
					this.removeKey(keyToReplace);
				} else {
					indexToSet = this.Count;
				}

				this.Store[indexToSet] = new CacheItem<>(key, value);
				this.Count++;

			}

			this.recordUsage(key);
		}

		/**
		 * Returns `true` if the given `key` is present in the set; otherwise, `false`.
		 */
		public boolean containsKey(TKey key) {
			return this.findIndexOfKey(key) >= 0;
		}

		private void removeKey(TKey key) {
			int indexOfKey = this.findIndexOfKey(key);
			if (indexOfKey >= 0) {
				this.UsageTracker.remove(key);
				this.Store[indexOfKey] = null;
				this.Count--;
			}
		}

		private int findIndexOfKey(TKey key) {
			for (int i = 0; i < this.Count; i++) {
				if (this.Store[i] != null && this.Store[i].key.equals(key))
					return i;
			}
			return -1;
		}

		private void recordUsage(TKey key) {
			this.UsageTracker.remove(key);
			this.UsageTracker.addFirst(key);
		}
	}

	/**
	 * An internal data structure representing a single item in an N-Way
	 * Set-Associative Cache
	 */
	static class CacheItem<TKey, TValue> {
		public TKey key;
		public TValue value;

		public CacheItem(TKey key, TValue value) {
			this.key = key;
			this.value = value;
		}
	}

	static enum ReplacementAlgo {

		LruAlgorithm("LRUReplacementAlgo"), MruAlgorithm("MRUReplacementAlgo"),
		CustomAlgorithm("CustomReplacementAlgo");

		private final String value;

		ReplacementAlgo(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

		public static ReplacementAlgo getAlgo(String value) {
			for (ReplacementAlgo e : ReplacementAlgo.values()) {
				if (e.value == value) {
					return e;
				}
			}
			return null;
		}

	}

	/**
	 * A common interface for replacement algos, which decide which item in a
	 * CacheSet to evict
	 */
	interface IReplacementAlgo<TKey> {

		TKey evictKey(LinkedList<TKey> usageTracker);
	}

	static class LRUReplacementAlgo<TKey> implements IReplacementAlgo<TKey> {

		@Override
		public TKey evictKey(LinkedList<TKey> usageTracker) {
			return usageTracker.getLast();
		}
	}

	static class MRUReplacementAlgo<TKey> implements IReplacementAlgo<TKey> {

		@Override
		public TKey evictKey(LinkedList<TKey> usageTracker) {
			return usageTracker.getFirst();
		}
	}

	static class CustomReplacementAlgo<TKey> implements IReplacementAlgo<TKey> {

		@Override
		public TKey evictKey(LinkedList<TKey> usageTracker) {
			return usageTracker.get(usageTracker.size() / 2);
		}
	}

	// ############################ BEGIN Helper Classes
	// ############################
	// NOTE: Your code in the classes below will not be evaluated as part of the
	// exericse.
	// They are just used by the stub code in the header to help run HackerRank test
	// cases.
	// You may need to make small modifications to these classes, depending on your
	// interface design,
	// for tests to run and pass, but it is not a core part of the exercise
	//
	static class OutParam<T> {
		public T value;
	}

	private static boolean isNullOrEmpty(String s) {
		return s == null || s.isEmpty();
	}

	public static class SetAssociativeCacheFactory {
		/// NOTE: replacementAlgoName is provided in case you need it here. Whether you
		/// do will depend on your interface design.
		public static SetAssociativeCache<String, String> CreateStringCache(int setCount, int setSize,
				ReplacementAlgo replacementAlgoName) {
			return new SetAssociativeCache<>(setCount, setSize, replacementAlgoName);
		}

		/// NOTE: Modify only if you change the main interface of SetAssociativeCache
		public static Object InvokeCacheMethod(String inputLine, SetAssociativeCache<String, String> cacheInstance) {
			String[] callArgs = Arrays.stream(inputLine.split(",", -1)).map(a -> a.trim()).toArray(n -> new String[n]);

			String methodName = callArgs[0].toLowerCase();
			// String[] callParams = Arrays.copyOfRange(callArgs, 1, callArgs.length - 1);
			// // TODO: This is unused

			switch (methodName) {
			case "get":
				return cacheInstance.get(callArgs[1]);
			case "set":
				cacheInstance.set(callArgs[1], callArgs[2]);
				return null;
			case "containskey":
				return cacheInstance.containsKey(callArgs[1]);
			case "getcount":
				return cacheInstance.getCount();

			// TODO: If you want to add and test other public methods to
			// SetAssociativeCache,
			// add them to the switch statement here... (this is not common)

			default:
				throw new RuntimeException(String.format("Unknown method name '{%s}'", methodName));
			}
		}
	}

	// TODO: Consider making use of this in the `SetAssociativeCacheFactory` above
	// to map replacement algo name
	// to a IReplacementAlgo instance for the interface you design
	public static class ReplacementAlgoFactory {
		public static IReplacementAlgo createReplacementAlgo(ReplacementAlgo replacementAlgoName) {
			switch (replacementAlgoName) {
			case LruAlgorithm:
				return new LRUReplacementAlgo();
			case MruAlgorithm:
				return new MRUReplacementAlgo();
			default:
				// TODO: If you want to test other replacement algos, add them to the switch
				// statement here...
				throw new RuntimeException(String.format("Unknown replacement algo '%s'", replacementAlgoName));
			}
		}
	}

	// ^^ ######################### END Helper Classes ######################### ^^

}