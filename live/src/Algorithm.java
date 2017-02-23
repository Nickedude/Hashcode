import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * Created by gusrod on 2017-02-23.
 */
public class Algorithm {

    private World world;

    private HashMap<Video, HashSet<Endpoint>> coveredEndpoints;
    private HashMap<VidCachePair, Integer> scores;
    private Comparator<VidCachePair> comparator = new Distance();

    private PriorityQueue<VidCachePair> queue = new PriorityQueue<>(comparator);

    public Algorithm(World world) {
        this.world = world;
    }

    private void init() {
        for (Video vid : world.videos) {
            for (Cache cache : world.caches.values()) {
                VidCachePair pair = new VidCachePair(vid, cache);
                scores.put(pair, calculateScore(pair));
            }
            queue.add(getBestCachePair(vid));
        }

    }

    public void calculate() {
        init();

        while(!queue.isEmpty()) {
            VidCachePair top = queue.poll();
            if(top.cache.videoFits(top.vid) && top.score > 0) {
                top.cache.addVideo(top.vid);
                for (Endpoint point: top.cache.enpoints) {
                    coveredEndpoints.get(top.vid).add(point);
                }
                queue.add(getBestCachePair(top.vid));
            }
        }


    }

    private int calculateScore(VidCachePair pair) {
        int score = 0;

        for (Endpoint point : world.endpoints) {

            if(!coveredEndpoints.get(pair.vid).contains(point)) {
                score += pair.cache.savedTime(point);
            }
        }

        return score;
    }

    private VidCachePair getBestCachePair(Video vid) {
        VidCachePair best = null;

        for (Cache cache: world.caches.values()) {
            VidCachePair pair = new VidCachePair(vid, cache);
            pair.score = calculateScore(pair);
            if(best == null || pair.score >= best.score) {
                best = pair;
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
