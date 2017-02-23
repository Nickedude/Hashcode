public class World {
	int videos;
	int endpoints;
	int caches;
	int cacheCapacity;
	int requests;

	public World (int v, int e , int c, int cc, int rs) {
		videos = v;
		endpoints = e;
		caches = c;
		cacheCapacity = cc;
		requests = rs;
	}
}