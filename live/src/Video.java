import java.util.HashMap;
import java.util.Map;

/**
 * Created by gusrod on 2017-02-23.
 */
public class Video {
	int id;
    int size;
    Map<Endpoint, Integer> requests = new HashMap<>();      //Maps an endpoint to the nr of requests it generates for this video	

    public Video(int size, int id) {
        this.size = size;
        this.id = id;
    }

    public void addRequest(Endpoint endpoint, int n) {
        requests.put(endpoint, n);
    }

}
