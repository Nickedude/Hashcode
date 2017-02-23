import java.util.*;

public class World {
	int videos;
	int nrOfEndpoints;
	int nrOfCaches;
	int cacheCapacity;
	int nrOfRequests;
	List<Endpoint> endpoints;
	List<Cache> caches;

	public World (int v, int e , int c, int cc, int rs, List<Endpoint> es, List<Cache> cs) {
		videos = v;
		nrOfEndpoints = e;
		nrOfCaches = c;
		cacheCapacity = cc;
		nrOfRequests = rs;
		caches = cs;
		endpoints = es;
	}
}