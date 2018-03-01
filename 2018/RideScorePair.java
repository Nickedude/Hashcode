import java.util.Comparator;

public class RideScorePair {
    private final World world;
    public final Ride ride;
    public final Car car;
    public final int score;


    public RideScorePair(Ride ride, Car car, World world) {
        this.ride = ride;
        this.car = car;
        this.world = world;
        score = getScore();
    }

    public int getScore() {
        int distanceFromCarToRide = car.getDistanceFrom(ride.startRow, ride.endRow);
        int arrivalTime = car.getTick() + distanceFromCarToRide;
        if(arrivalTime <= ride.startTime) {
            return ride.distance + world.onTimeBonus - (ride.startTime - arrivalTime) - distanceFromCarToRide;
        } else if(arrivalTime + ride.getDistance() <= ride.finnishTime){
            return ride.distance - distanceFromCarToRide;
        } else {
            return 0;
        }
    }

}
