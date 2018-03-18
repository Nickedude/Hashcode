public class RideCarPair {

    public final Car car;
    public final Ride ride;
    public final long score;

    public RideCarPair(Car car, Ride ride, int T, int B) {
        this.car = car;
        this.ride = ride;
        this.score = calculateScore(T,B);
    }

    private long calculateScore(int T, int B) {

        int length = Math.abs(ride.startRow - ride.endRow) + Math.abs(ride.startColumn - ride.endColumn);

        // 0 score if car is at a tick after the finnish time
        if(ride.finnishTime < car.getTick()) {
            return -100000000;
        }

        // 0 score if the time it takes for the car to drive to the start and then complete the ride is linger than the time from the cars current tick to the finnish time
        if((car.getTick() + car.getDistanceFrom(ride.startRow, ride.startColumn) + length) >= ride.finnishTime){
            return -100000000;
        }

        long timeScore = Math.abs(ride.startTime - T) * 10000;
        //System.out.println("Time score: " + timeScore);

        int wait = ride.startTime - car.getTick();
        long waitScore = (wait > 0) ? (- (ride.startTime - car.getTick()) * 1000) : 0;

        long distScore = - car.getDistanceFrom(ride.startRow, ride.startColumn) * 1000;
        //System.out.println("Dist score: " + distScore);

        long bonusScore = ((car.getTick() + car.getDistanceFrom(ride.startRow, ride.startColumn)) > ride.startTime) ? 0 : B * 10000;

        return timeScore + distScore + bonusScore + waitScore;
    }
}
