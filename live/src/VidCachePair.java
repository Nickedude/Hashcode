/**
 * Created by gusrod on 2017-02-23.
 */
public class VidCachePair {
    public Video vid;
    public Cache cache;
    public long score = 0;

    public VidCachePair(Video vid, Cache cache) {
        this.cache = cache;
        this.vid = vid;
    }

    public void setScore(long score) {
        this.score = score;
    }



}
