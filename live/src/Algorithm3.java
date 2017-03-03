import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Algorithm3 {

    private World world;

    private HashMap<Video, HashSet<Endpoint>> coveredEndpoints = new HashMap<>();   //Holds which videos are available for wich endpoints in cahce
    private Comparator<VidCachePair> comparator = new Distance();                   //Compares two pairs according to score

    private PriorityQueue<VidCachePair> queue = new PriorityQueue<>(comparator);    //Prio queue that delivers the pair with the best score

    public Algorithm3(World world) {
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

        while(!queue.isEmpty()) {                                                                               //Continue until the queue is empty
            VidCachePair top = queue.poll(); //Take the pair with the best score
            VidCachePair best = getBestCachePair(top.vid);
            if(top.cache != best.cache) {
                queue.add(best);
                continue;
            }
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
    // O(V³ * C² * E)
    public void calculate2 () {
        for (Video vid: world.videos ) {
            for (Cache cache: world.caches.values()) {
                for (Video vid2 : world.videos) {
                    if(cache.videos.contains(vid2)) {
                        int oldscore = world.score(); // O(V*E*C)
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
        return (int)(timesaved / pair.vid.size);                                                                   //  All but trending_today runs better with => return timesaved * pair.vid.size
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

    public void printFreeSpace () {
        System.out.println("Free space: ");
        for(Cache c : world.caches.values()) {
            System.out.print(c.freeSpace+", ");
        }
    }

    public void optimize () {
        //Does the actual optimization
        int j = 0;
        for(Cache c : world.caches.values()) {                                                  //For each cache
            System.out.println("Cache: " + j);
            j++;
            HashSet<Endpoint> eps = new HashSet<>(c.endpoints);                                 //Get associated endpoints
            PriorityQueue<VidCachePair> potScores1 = new PriorityQueue<>(new ByScore());        //Videos in the datacenter sorted by score
            PriorityQueue<VidCachePair> potScores2 = new PriorityQueue<>(new ByScore());        //Need 2
            HashMap<Endpoint,HashSet<Video>> epToVidInDc = new HashMap<>();                     //Maps an endpoint to the videos it needs to request from the datacenter
            PriorityQueue<Video> vidsInCache = new PriorityQueue<>(new BySize());               //A prio queue sorting the videos in the cache by size

            //Builds epToVidInDc
            for(Endpoint ep : eps) {                                                            //Iterate over all eps in this cache
                epToVidInDc.put(ep,new HashSet<Video>());                                       //Create an empty hashset for this ep

                for(Video v: ep.videos) {                                                       //Iterate over an eps videos
                    if(!coveredEndpoints.get(v).contains(ep))                                   //If this video isn't covered for this ep
                        epToVidInDc.get(ep).add(v);                                             //Add it to the set
                }
             }
           
            //Builds vidsInCache prio queue
            for(Video v : c.videos) {
                vidsInCache.add(v);
            }

            while(!vidsInCache.isEmpty()) {                                 //Iterate over all the videos, starting with the largest one
                Video v = vidsInCache.poll();                               //Get the largest video

                long oldscore = calculateOldScore(new VidCachePair(v,c));    //Score for old video
                long altscore = 0;                                           //Score for the alternative

                Video v1 = null;                                            //Potential replacements
                Video v2 = null;

                potScores1 = buildPotScore(c, eps,epToVidInDc);             //Sort the possible replacements by score

                while(!potScores1.isEmpty()) {
                    VidCachePair temp = potScores1.poll();                  //Get potential replacement video with highest score
                    long v1score = temp.score;                               //Get it's score
                    v1 = temp.vid;                                          //Save reference to video
                    if((v.size)+(c.freeSpace) < v1.size) {                  //If it doesn't fit, throw it away
                        continue;
                    }
                    else{                                                   //If it does fit, find a potential partner
                        altscore = v1score;                                 //Score uptil now is just score for v1
                        potScores2 = buildPotScore(c, eps,epToVidInDc);     //Get queue to look for partner in

                        while(!potScores2.isEmpty()) {
                            temp = potScores2.poll();                       //Get the one with highest score
                            long v2score = temp.score;                       //Get score
                            v2 = temp.vid;                                  //Save ref to video

                            if((v.size)+(c.freeSpace) >= (v1.size + v2.size) && oldscore < (v1score + v2score)){    //If the replacements fit and if their combined score is higher we have a match
                                System.out.println("Found match!");
                                altscore = v2score + v1score;               //Update score
                                break;                                      //Break in order to update
                            }
                        }

                        if(altscore > oldscore) {                           //If the alternative score is higher than the old we break
                            break;
                        }
                    }

                }


                if(altscore > oldscore) {
                    removeFromCache(v,c,epToVidInDc);   //Remove the old video from the cache
                    addToCache(v1,c,epToVidInDc);       //Add the new video to the cache
                    addToCache(v2,c,epToVidInDc);       //Add the new video to the cache
                }
            }
        }
        //General thought: 
        //Create a map which maps Endpoints -> Videos in the datacenter
        //For each cache
        //Get the endpoints associated with it 
        //Get the set of videos the endpoints currently needs to fetch from the datacenter
        //Calculate the score for each of these videos
        //Take score of the largest video on the fastest cache and investigate what the score
        //could be if we were to replace it with several smaller videos which are now being
        //streamed from the datacenter. Replace if benefitial.
        //Do this for all videos and all caches.

        //Get the best possible replacement for it
                //This can probably be done in a better way but i'll do this for now
                //Try to find two videos which can replace this
                //Take the first video in the datacenter with the highest score
                //At all times, if altscore > oldscore - break
                //It it's size is larger than v's - throw it away
                //If it isn't - save it and update altscore
                //Iterate through the rest (sorted by score) and try to pair it with another video
                //If we can't pair it with another video such that it fits - throw it away and move on
                //If we can pair it with another video s.t it fits - check score.
                //If altscore < oldscore we can throw the first one away and move on since we iterated through the rest based on score
                //If altscore > oldscore we have our replacement

    }

    //Prints usefull info for checking if any optimization is actually possible using optimize
    public void analyzeOptimization () {
        int j = 0;
        int overallavrg = 0;

        for(Cache c : world.caches.values()) {                                                  //For each cache
            int cacheavrg = 0;
            j++;
            HashSet<Endpoint> eps = new HashSet<>(c.endpoints);                                 //Get associated endpoints
            PriorityQueue<VidCachePair> potScores1 = new PriorityQueue<>(new ByScore());        //Videos in the datacenter sorted by score
            PriorityQueue<VidCachePair> potScores2 = new PriorityQueue<>(new ByScore());        //Need 2
            HashMap<Endpoint,HashSet<Video>> epToVidInDc = new HashMap<>();                     //Maps an endpoint to the videos it needs to request from the datacenter
            PriorityQueue<Video> vidsInCache = new PriorityQueue<>(new BySize());               //A prio queue sorting the videos in the cache by size

            //Builds epToVidInDc
            int k = 0;
            for(Endpoint ep : eps) {                                                          //Iterate over all eps in this cache
                epToVidInDc.put(ep,new HashSet<Video>());                                       //Create an empty hashset for this ep
                int avrgvidsize = 0;
                int i = 0;
                for(Video v: ep.videos) {                                                       //Iterate over an eps videos
                    if(!coveredEndpoints.get(v).contains(ep)) {                                 //If this video isn't covered for this ep
                        epToVidInDc.get(ep).add(v);                                             //Add it to the set
                        i++;
                        avrgvidsize += v.size;
                    }
                }
                if(i != 0) {
                    avrgvidsize = avrgvidsize/i;
                    cacheavrg += avrgvidsize;
                    k++;
                }
            }
            if(k != 0)
                cacheavrg = cacheavrg/k;
            overallavrg += cacheavrg;

            for(Video v : c.videos) {
                vidsInCache.add(v);
            }

            int avrglargestsize = 0;
            k = 0;
            while(!vidsInCache.isEmpty()) {                                 //Iterate over all the videos, starting with the largest one
                Video v = vidsInCache.poll();                               //Get the largest video
                avrglargestsize += v.size;
                k++;
            }

            System.out.println("Average possible free space: " + ((avrglargestsize/k)+c.freeSpace));
        }

        System.out.println("Overall average video size: " + (overallavrg/j));
        System.out.println("Cache size: " + world.cacheCapacity);
    }

    //Specialized for optimize
    private void removeFromCache (Video v, Cache c,HashMap<Endpoint,HashSet<Video>> epToVidInDc) {
        for(Endpoint p : c.endpoints) {                             //Mark the video removed as not covered
            if(v.requests.get(p) != null && v.requests.get(p) > 0){ //If this endpoint actually requests the video
                coveredEndpoints.get(v).remove(p);                  //This video is no longer covered for this endpoint
                epToVidInDc.get(p).add(v);                          //Mark it as being in the datacenter
            }
        }

        c.removeVideo(v);
    }

    //Specialized for optimize
    private void addToCache (Video v, Cache c,HashMap<Endpoint,HashSet<Video>> epToVidInDc) {
        c.addVideo(v);
        if(coveredEndpoints.get(v) == null) {
            coveredEndpoints.put(v,new HashSet<Endpoint>());                //Create slots in coveredEndpoints for the new video
        }

        for(Endpoint p : c.endpoints) {                                     //For each endpoint that is connected to this cache
                if(v.requests.get(p) != null && v.requests.get(p) > 0) {    //If that endpoint requests v
                    coveredEndpoints.get(v).add(p);                         //Mark this video as covered for that endpoint
                    epToVidInDc.get(p).remove(v);                           //Mark this video as no longer in the datacenter
                }
        }
    }

    //Builds potScores
    private PriorityQueue<VidCachePair> buildPotScore (Cache c, HashSet<Endpoint> eps,HashMap<Endpoint,HashSet<Video>> epToVidInDc) {
            PriorityQueue<VidCachePair> potScores = new PriorityQueue<>(new ByScore());    
            for(Endpoint ep : eps) {                                        //For each endpoint in this caxhe
                HashSet<Video> inDc = epToVidInDc.get(ep);                  //Get all the videos it fetches from the datacenter
                for(Video v : inDc) {                                       //For all these videos
                    VidCachePair p = new VidCachePair(v,c);
                    p.setScore(calculatePotScore(p));                       //Calculate the score the endpoints would save if this video was in the cache
                    potScores.add(p);
                }
            }
            return potScores;
    }

    //Specialized for optimize
    public int calculateOldScore (VidCachePair p) {
        int score = 0;
            for(Endpoint ep : p.cache.endpoints) {                      //For all the endpoints in this cache
                if(p.vid.requests.get(ep) != null) {                    //if this endpoint requests this video
                    int dt = p.cache.savedTime(ep);                     //Difference in time between cache and datacenter
                    score += dt * p.vid.requests.get(ep);        //Calculate score
                }
            }
        return score;
    }

    //Specialized for optimize
    public int calculatePotScore (VidCachePair p) {
        int score = 0;
        for(Endpoint ep : p.cache.endpoints) {                      //For all the endpoints in this cache
            if(p.vid.requests.get(ep) != null && !coveredEndpoints.get(p.vid).contains(ep)) {         //if this endpoint doesn't already have this video cached
                int dt = p.cache.savedTime(ep);                     //Difference in time between cache and datacenter
                score += dt * p.vid.requests.get(ep);        //Calculate score
            }
        }
        return score;
    }

    private class Distance implements Comparator<VidCachePair> {
        @Override
        public int compare(VidCachePair a, VidCachePair b) {
            if(b.score > a.score){
                return 1;
            } else if(a.score > b.score) {
                return -1;
            } else {
                return 0;
            }
        }
    }


    private class BySize implements Comparator<Video> {
        @Override
        public int compare(Video a, Video b) {
            return b.size - a.size;
        }
    }

    private class ByScore implements Comparator<VidCachePair> {
        @Override
        public int compare(VidCachePair a, VidCachePair b) {
            if(b.score > a.score){
                return 1;
            } else if(a.score > b.score) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
