import java.util.HashMap;
import java.util.Map;


public class Video {
	int id;
    int size;
    Map<Endpoint, Integer> requests = new HashMap<>();      //Maps an endpoint to the nr of requests it generates for this video	

    public Video(int size, int id) {
        this.size = size;
        this.id = id;
    }

    public void addRequests(Endpoint endpoint, int n) {
        Integer no = requests.get(endpoint);
        requests.put(endpoint, no == null ? n : no + n);
    }

}
