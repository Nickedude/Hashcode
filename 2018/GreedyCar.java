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
            /*int timeDiff = o1.startTime - o2.startTime;

            if(timeDiff != 0) {
                return timeDiff;
            }*/

            int distDiff = o1.getDistance() - o2.getDistance();
            return distDiff;


        }
    }

    public World compute() {
        int rideCount = 0;

        for(Ride ride : world.rides) {

            // Select the car most fit for this ride
            Car bestCar = null;
            int bestTimeDiff = Integer.MAX_VALUE;

            for(Car car : world.cars) {
                int distance = car.getDistanceFrom(ride.startRow, ride.startColumn);    // Get the distance from the car to the start position
                int timeDiff = ride.startTime - car.getTick();                          // Get the time it will take to travel to the start pos
                int arrivalTick = car.getTick() + distance;                             // Calculate the arrival time

                if((timeDiff < bestTimeDiff) && (arrivalTick <= ride.finnishTime)) {    // Select the closest car that will be there on time
                    bestCar = car;
                }
            }

            if(bestCar == null) {
                continue;
            }

            bestCar.addRide(ride);
            bestCar.travelTo(ride.startRow, ride.startColumn);
            bestCar.travelTo(ride.endRow, ride.endColumn);
        }

        return world;
    }
}
