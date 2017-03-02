import java.util.*;

public class World {
	int nrOfvideos;
	int nrOfEndpoints;
	int nrOfCaches;
	int cacheCapacity;
	int nrOfRequests;
	List<Endpoint> endpoints;
	Map<Integer,Cache> caches;
	List<Video> videos;

	public World (int v, int e , int c, int cc, int rs, List<Endpoint> es, Map<Integer,Cache> cs, List<Video> vids) {
		nrOfvideos = v;
		nrOfEndpoints = e;
		nrOfCaches = c;
		cacheCapacity = cc;
		nrOfRequests = rs;
		caches = cs;
		endpoints = es;
		videos = vids;
    }

	public void printWorld () {
		System.out.println(nrOfvideos);
		System.out.println(nrOfEndpoints);
		System.out.println(nrOfCaches);
		System.out.println(nrOfRequests);
		System.out.println("Printing endpoints");
		for(int i = 0; i < endpoints.size(); i++) {
			System.out.println(endpoints.get(i).latDatacenter);
		}
		System.out.println("Printing videos");
		for(int i = 0; i < videos.size(); i++) {
			System.out.println(videos.get(i).id);
		}
		System.out.println("Printing caches");
		for(Cache c : caches.values()) {
			System.out.println(c.freeSpace);
		}
		System.out.println("The end");
	}

	public int score () {
	    int score = 0;
        for (Video vid : videos ) {
            for (Endpoint end :vid.requests.keySet()) {
                for(Cache cache : end.cacheLatMap.keySet()) {
                    if(cache.videos.contains(vid)) {
                        score += cache.savedTime(end) * vid.requests.get(end);
                    }
                }
            }
        }
        return score;
    }
}