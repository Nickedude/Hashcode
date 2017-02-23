import java.util.*;

public class Endpoint {
	List<Integer> caches;
	Map<Integer,Integer> cacheLatMap;
	int latDatacenter;

	public Endpoint () {
		cacheLatMap = new HashMap<Integer,Integer>();
		caches = new ArrayList<Integer>();
	}
}