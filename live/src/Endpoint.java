import java.util.*;

public class Endpoint {
	Map<Cache,Integer> cacheLatMap;
	int latDatacenter;
	int nrofcaches;

	public Endpoint (int ld, int ncs) {
		latDatacenter = ld;
		nrofcaches = ncs;
		cacheLatMap = new HashMap<Cache,Integer>();
	}
}