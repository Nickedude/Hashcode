import java.util.*;

/**
 * Created by gusrod on 2017-02-23.
 */
public class Cache {

	HashSet<Endpoint> endpoints = new HashSet<Endpoint>();
    int freeSpace;
    List<Video> videos = new ArrayList<Video>();

    public Cache(int maxSize) {
        freeSpace = maxSize;
    }

    public void addVideo(Video vid) {
        videos.add(vid);
        freeSpace -= vid.size;
    }

    public boolean videoFits(Video vid) {
        return vid.size <= freeSpace;
    }

    public int savedTime(Endpoint point) {
        if(point.cacheLatMap.containsKey(this)) {
            return point.latDatacenter - point.cacheLatMap.get(this);
        } else {
            return 0;
        }
    }

}
