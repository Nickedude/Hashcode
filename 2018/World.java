import java.util.ArrayList;
import java.util.List;

public class World {
    List<Car> cars;
    List<Ride> rides;
    int T;
    int onTimeBonus;

    public World(int T, int onTimeBonus, int nCars, List<Ride> rides) {
        this.T = T;
        this.onTimeBonus = onTimeBonus;
        this.rides = removeUnwantedRides(rides);
        cars = new ArrayList<>();

        for(int i = 0; i < nCars; i++) {
            cars.add(new Car());
        }
    }


    private List<Ride> removeUnwantedRides(List<Ride> rides) {
        List<Ride> filteredRides = new ArrayList<>();

        for (Ride ride : rides) {
            if(ride.getDistance() > ride.finnishTime - ride.startTime)
                continue;
            filteredRides.add(ride);
        }

        return filteredRides;
    }
}
