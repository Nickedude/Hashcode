import java.util.Comparator;
import java.util.PriorityQueue;

public class SomethingElse {
    World world;

    public void calculate(World world) {
        this.world = world;
        PriorityQueue<RideScorePair> queue;
        int n = world.rides.size();
        for(int i = 0; i < n; i++) {
            queue = getQueue();
            RideScorePair rsp = queue.poll();
            rsp.car.addRide(rsp.ride);
            world.rides.remove(rsp.ride);
        }
    }

    public PriorityQueue<RideScorePair> getQueue() {
        PriorityQueue<RideScorePair> queue = new PriorityQueue<>(new PairComparator());

        for (Car car: world.cars) {
            for (Ride ride : world.rides) {
                queue.add(new RideScorePair(ride, car, world));
            }
        }

        return queue;
    }

    public class PairComparator implements Comparator<RideScorePair> {
        @Override
        public int compare(RideScorePair o1, RideScorePair o2) {
            return o2.score - o1.score;
        }
    }

}
