import java.util.ArrayList;
import java.util.List;

/**
 * Created by gusrod on 2017-02-23.
 */
public class Cache {

    int freeSpace;
    List<Video> videos = new ArrayList<Video>();

    public Cache(int maxSize) {
        freeSpace = maxSize;
    }

    public void addVideo(Video vid) {
        videos.add(vid);
        freeSpace -= vid.size;
    }

}
