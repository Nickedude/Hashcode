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
		for(int i = 0; i < endpoints.size(); i++) {
			System.out.println(endpoints.get(i).latDatacenter);
		}
	}
}