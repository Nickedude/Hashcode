import java.util.HashMap;
import java.util.Map;

/**
 * Created by gusrod on 2017-02-23.
 */
public class Video {
    int size;
    Map<Endpoint, Integer> requests = new HashMap<>();	

    public Video(int size) {
        this.size = size;
    }

    public void addRequest(Endpoint endpoint, int n) {
        requests.put(endpoint, n);
    }

}
