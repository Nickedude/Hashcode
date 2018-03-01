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
        this.rides = rides;
        cars = new ArrayList<>();

        for(int i = 0; i < nCars; i++) {
            cars.add(new Car());
        }
    }
}
