import java.util.*;

public class World {
	int nrOfvideos;
	int nrOfEndpoints;
	int nrOfCaches;
	int cacheCapacity;
	int nrOfRequests;
	double avgCacheLatency;
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

		avgCacheLatency = 0;
        int connections = 0;
		for(Endpoint end : endpoints) {
            for (int latency: end.cacheLatMap.values()) {
                connections++;
                avgCacheLatency += latency;
            }
        }
        avgCacheLatency = avgCacheLatency / connections;
    }

	public void printWorld () {
		System.out.println(nrOfvideos);
		System.out.println(nrOfEndpoints);
		System.out.println(nrOfCaches);
		System.out.println(nrOfRequests);
		System.out.println("Printing endpoints");

		endpoints.forEach(end -> System.out.println(end.latDatacenter));

		videos.forEach(video -> System.out.println(video.id));

        System.out.println("Printing caches");
        caches.forEach((id, cache) -> System.out.println(cache.freeSpace));

		System.out.println("The end");
	}

	public long score () {
	    long score = 0;
        for (Video vid : videos ) {
            for (Endpoint end : vid.requests.keySet()) {
                for(Cache cache : end.cacheLatMap.keySet()) {
                    if(cache.videos.contains(vid)) {
                        score += cache.savedTime(end) * vid.requests.get(end);
                    }
                }
            }
        }
        return (long)((score*1.0 / nrOfRequests) * 1000);
    }
}