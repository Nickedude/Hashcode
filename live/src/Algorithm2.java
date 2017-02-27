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
            coveredEndpoints.put(vid,new HashSet<>());  //Initialize a hash set for it's endpoints

            queue.add(getBestCachePair(vid));                   //Pair this video with a cache in the best way
        }
        System.out.println("Init ok!");

    }

    public void calculate() {
        init();                                                                                                 //Initialize

        while(!queue.isEmpty()) {                                                                               //Continue until the queue is empty
            //System.out.println(queue.size());
            VidCachePair top = queue.poll(); //Take the pair with the best score
            if(top.cache.videoFits(top.vid) && top.score > 0) {                                                 //If the video fits in it's associated cache, it has a score and if the cache doesn't hold that video already
                top.cache.addVideo(top.vid);                                                                    //Add the video to the cache
                for (Endpoint point: top.cache.endpoints) {                                                     //Mark this video as covered for the endpoints connected to this cache
                    coveredEndpoints.get(top.vid).add(point);
                }
            }
            VidCachePair next = getBestCachePair(top.vid);                                                      //Pair this video with a new cache
            if(next.score > 0 && next.cache.videoFits(next.vid)) {                                              //Add it to the queue if it gets a score and if it fits in the cache
                queue.add(next);                                                                                //!!!! we should probably check i getBestCachePair that the video fits, so all pairs are valid
            }
        }
    }

    // Can only be used for me_at_the_zoo becouse it is so slow. The other input sets will not reacting in atleast 10 minutes.
    // Will give a substantial boost for the zoo set though.
    // TODO: make more efficient so it can be used for all sets
    public void calculate2 () {
        for (Video vid: world.videos ) {
            for (Cache cache: world.caches.values()) {
                for (Video vid2 : world.videos) {
                    if(cache.videos.contains(vid2)) {
                        int oldscore = world.score();
                        if (cache.freeSpace + vid2.size - vid.size >= 0) {
                            cache.removeVideo(vid2);
                            cache.addVideo(vid);
                            if (!(world.score() > oldscore)) {
                                cache.removeVideo(vid);
                                cache.addVideo(vid2);
                            }
                        }
                    }
                }
            }
        }
    }

    private int calculateScore(VidCachePair pair) {
        int timesaved = 0;

        for(Endpoint point : pair.vid.requests.keySet()) {                                  //Iterate over all endpoints requesting this video
            if(!coveredEndpoints.get(pair.vid).contains(point)) {                           //If this video isn't already covered for this endpoint
                timesaved += pair.vid.requests.get(point) * pair.cache.savedTime(point);    //Then we could potentially save time, calculate the time
                                                                                            // The time we could potentially save is the nr of requests
            }                                                                               //times the difference in time between datacenter and cache server
        }
        return timesaved;                                                                   //  All but trending_today runs better with => return timesaved * pair.vid.size
                                                                                            // but trending_today is MUCH worse.;
    }

    private VidCachePair getBestCachePair(Video vid) {
        VidCachePair best = null;

        for (Cache cache: world.caches.values()) {                                              //Loop through all caches
            VidCachePair pair = new VidCachePair(vid, cache);                                   //Create a pair
            pair.score = calculateScore(pair);                                                  //Get the score for this pair. Will only get a score if it's not already covered.
            // Add !best.cache.videoFits(pair.vid) && pair.cache.videoFits(pair.vid) as a condition to increase the score of me_at_the_zoo and videos worth spreading
            if(best == null ||  pair.score >= best.score && pair.cache.videoFits(pair.vid)) {    //If we have no pair or if the score of this is better than the one we got
                best = pair;                                                                    //Update the pair
            }
        }
        return best;
    }


    private class Distance implements Comparator<VidCachePair> {
        @Override
        public int compare(VidCachePair a, VidCachePair b) {
            return a.score - b.score;
        }
    }

}
