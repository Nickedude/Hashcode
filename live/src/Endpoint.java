import java.util.*;

public class Endpoint {
	Map<Cache,Integer> cacheLatMap;
	int latDatacenter;
	int nrofcaches;
	HashSet<Video> videos; 		//Holds the videos this endpoint requests

	public Endpoint (int ld, int ncs) {
		latDatacenter = ld;
		nrofcaches = ncs;
		cacheLatMap = new HashMap<Cache,Integer>();
		videos = new HashSet<Video>();
	}
}