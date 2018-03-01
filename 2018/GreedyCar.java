import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GreedyCar {

    World world;
    List<Ride> sortedRides;

    public GreedyCar (World world) {
        this.world = world;
        Collections.sort(world.rides, new RideComparator());
    }

    private class RideComparator implements Comparator<Ride> {

        @Override
        public int compare(Ride o1, Ride o2) {
            int timeDiff = o1.startTime - o2.startTime;

            if(timeDiff != 0) {
                return timeDiff;
            }

            int distDiff = o1.getDistance() - o2.getDistance();
            return distDiff;


        }
    }

    public World compute() {
        int rideCount = 0;
        for(Car car : world.cars) {
            if(rideCount < world.rides.size()) {
                car.addRide(world.rides.get(rideCount++));
            }
        }

        return world;
    }
}
