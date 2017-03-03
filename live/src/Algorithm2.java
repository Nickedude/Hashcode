import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Algorithm2 {

    private World world;

    private HashMap<Video, HashSet<Endpoint>> coveredEndpoints = new HashMap<>();   //Holds which videos are available for wich endpoints in cahce
    private Comparator<VidCachePair> comparator = new Distance();                   //Compares two pairs according to score

    private PriorityQueue<VidCachePair> queue = new PriorityQueue<>(comparator);    //Prio queue that delivers the pair with the best score

    public Algorithm2(World world) {
        this.world = world;
    }

    private void init() {
        for (Video vid : world.videos) {                        //Loop through all the videos
            coveredEndpoints.put(vid,new HashSet<Endpoint>());  //Initialize a hash set for it's endpoints
            queue.add(getBestCachePair(vid));                   //Pair this video with a cache in the best way
        }
        System.out.println("Init ok!");

    }

    public void calculate() {
        init();                                                                                                 //Initialize
        System.out.println("Calculate Started");
        while (!queue.isEmpty()) {                                                                               //Continue until the queue is empty
            VidCachePair top = queue.poll(); //Take the pair with the best score
            VidCachePair best = getBestCachePair(top.vid);
            if (top.cache != best.cache) {
                queue.add(best);
                continue;
            }
            if (top.score > 0) {
                if (top.cache.videoFits(top.vid)) {                                                 //If the video fits in it's associated cache, it has a score and if the cache doesn't hold that video already
                    top.cache.addVideo(top.vid);//Add the video to the cache
                    top.vid.scores.put(top.cache,top.score);
                    for (Endpoint point : top.cache.endpoints) {                                                     //Mark this video as covered for the endpoints connected to this cache
                        coveredEndpoints.get(top.vid).add(point);
                    }
                }
            }
            VidCachePair next = getBestCachePair(top.vid);                                                      //Pair this video with a new cache
            if (next.cache.videoFits(next.vid) && !next.cache.videos.contains(next.vid) && next.score > 0) {                                              //Add it to the queue if it gets a score and if it fits in the cache
                queue.add(next);                                                                                //!!!! we should probably check i getBestCachePair that the video fits, so all pairs are valid
            }
        }
    }

    // Can only be used for me_at_the_zoo becouse it is so slow. The other input sets will not reacting in atleast 10 minutes.
    // TODO: make more efficient so it can be used for all sets
    // O(V² * C * (E + log (v))
    public void calculate2 () {
        System.out.println("Calculate2 Started");
        for (Video vid: world.videos ) {
            for (Cache cache: world.caches.values()) {
                for (Video vid2 : world.videos) {
                    if(vid == vid2)
                        continue;
                    if(cache.videos.contains(vid2)) {
                        System.out.println(vid2.scores.get(cache));
                        long score = calculateScore(new VidCachePair(vid, cache));
                        if (cache.freeSpace + vid2.size - vid.size >= 0 && vid2.scores.get(cache)*vid2.size < score*vid.size) {
                           cache.removeVideo(vid2);
                           cache.addVideo(vid);
                           vid.scores.put(cache,score);
                           System.out.println("Changed videos");
                        }
                    }
                }
            }
        }
    }

    public void calculate3 () {
        System.out.println("Calculate3 Started");
        for(Cache cache : world.caches.values()) {
            if(cache.freeSpace > 0) {
                queue = new PriorityQueue<>(comparator);
                for(Endpoint end : cache.endpoints) {
                    for(Video vid : end.videos) {
                        if(cache.videoFits(vid)) {
                            VidCachePair pair = new VidCachePair(vid, cache);
                            pair.score = calculatePureScore(pair);
                            queue.add(pair);
                        }
                    }
                }
                while(queue.size() > 0) {
                    VidCachePair top = queue.poll();
                    if(!cache.videos.contains(top.vid) && cache.videoFits(top.vid)) {
                        cache.addVideo(top.vid);
                    }
                }
            }
        }
    }

    // O(E)
    private long calculateScore(VidCachePair pair) {
        int timesaved = 0;

        for(Endpoint point : pair.vid.requests.keySet()) {                                  //Iterate over all endpoints requesting this video
            if(!coveredEndpoints.get(pair.vid).contains(point)) {                           //If this video isn't already covered for this endpoint
                timesaved += pair.vid.requests.get(point) * pair.cache.savedTime(point);    //Then we could potentially save time, calculate the time
                                                                                            //The time we could potentially save is the nr of requests
            }                                                                               //times the difference in time between datacenter and cache server
        }
        return (long) (timesaved * 1000000.0 / (pair.vid.size));
    }

    private long calculatePureScore(VidCachePair pair) {
        int timesaved = 0;

        for(Endpoint point : pair.vid.requests.keySet()) {

            timesaved += pair.vid.requests.get(point) * pair.cache.savedTime(point) * point.cacheLatMap.getOrDefault(pair.cache, 0) / world.avgCacheLatency;
        }

        return (long) (timesaved * 1000000.0) ;
    }

    private VidCachePair getBestCachePair(Video vid) {
        VidCachePair best = null;

        for (Cache cache: world.caches.values()) {                                              //Loop through all caches
            VidCachePair pair = new VidCachePair(vid, cache);                                   //Create a pair
            pair.score = calculateScore(pair);                                                  //Get the score for this pair. Will only get a score if it's not already covered.
            // Add !best.cache.videoFits(pair.vid) && pair.cache.videoFits(pair.vid) as a condition to increase the score of me_at_the_zoo and videos worth spreading
            if(best == null || !best.cache.videoFits(pair.vid) && pair.cache.videoFits(pair.vid) || pair.score >= best.score && pair.cache.videoFits(pair.vid)) {    //If we have no pair or if the score of this is better than the one we got
                best = pair;                                                                    //Update the pair
            }
        }
        return best;
    }

    public void printFreeSpace () {
        System.out.println("Free space: ");
        for(Cache c : world.caches.values()) {
            System.out.print(c.freeSpace+", ");
        }
    }


    private class Distance implements Comparator<VidCachePair> {
        @Override
        public int compare(VidCachePair a, VidCachePair b) {
            if(b.score > a.score) {
                return 1;
            } else if(a.score > b.score) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
