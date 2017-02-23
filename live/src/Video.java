import java.util.HashMap;
import java.util.Map;

/**
 * Created by gusrod on 2017-02-23.
 */
public class Video {
    int size;
    Map<Integer, Integer> requests = new HashMap<>();

    public Video(int size) {
        this.size = size;
    }

    public void addRequest(int endpoint, int n) {
        requests.put(endpoint, n);
    }

}
