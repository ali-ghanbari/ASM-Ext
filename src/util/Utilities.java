package util;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

/**
 * General utility functions
 * @author Ali Ghanbari
 *
 */
public final class Utilities {
	private Utilities() {
		
	}
	
	public static <T> String printCollection(Collection<T> coll,
			Function<T, String> toString, String delim, String nullString) {
		String res = "";
		if(coll == null)
			return nullString;
		Iterator<T> it = coll.iterator();
		if(it.hasNext()) {
			res = toString.apply(it.next());
			while(it.hasNext()) {
				res += delim + toString.apply(it.next());
			}
		}
		return res;
	}
	
	public static <T> String printArray(T[] arr,
			Function<T, String> toString, String delim, String nullString) {
		String res = "";
		if(arr == null)
			return nullString;
		if(arr.length > 0) {
			res = toString.apply(arr[0]);
			for(int i = 1; i < arr.length; i++) {
				res += delim + toString.apply(arr[i]);
			}
		}
		return res;
	}
}
