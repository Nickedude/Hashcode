import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

public class EineAlgoritm {

    World world;

    public EineAlgoritm (World theWorld) {
        world = theWorld;
    }

    private class PairComparator implements Comparator<RideCarPair> {

        @Override
        public int compare(RideCarPair o1, RideCarPair o2) {
            long c = o1.score - o2.score;
            if(c == 0) {
                return 0;
            }
            else return c > 0 ? -1 : 1;
        }
    }

    public World compute() {

        /*Iterator<Ride> iterator = world.rides.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Ride ride = iterator.next();
            int length = ride.getDistance();
            int time = ride.finnishTime - ride.startTime;
            if(length > time) {
                iterator.remove();
                count++;
            }
        }

        System.out.println("Removed " + count + " rides.");*/
        //int missScore = 0;
        int n = world.rides.size();
        for(int i = 0; i < n; i++) {
            if(i % 1000 == 0)
                System.out.println(i);
            PriorityQueue<RideCarPair> pq = new PriorityQueue<>(new PairComparator());

            for (Ride ride : world.rides) {
                for (Car car : world.cars) {
                    pq.add(new RideCarPair(car, ride, world.T, world.onTimeBonus));
                }
            }

            RideCarPair bestPair = pq.poll();
            Ride r = bestPair.ride;
            Car c = bestPair.car;

            //System.out.println("Car tick: " + c.getTick());
            //System.out.println("Ending time: " + r.finnishTime);
            //System.out.println("Starting time: " + r.startTime);
            //int startTick = c.getTick() + c.getDistanceFrom(r.startRow, r.startColumn);
            //System.out.println("Actual start: " + startTick);
            //int length = Math.abs(r.startRow - r.endRow) + Math.abs(r.startColumn - r.endColumn);
            //int arrivalTick = (c.getTick() + c.getDistanceFrom(r.startRow, r.startColumn) + length);
            //System.out.println("Arrival tick: " + arrivalTick);

            //if(arrivalTick > r.finnishTime)
            //    missScore++;

            c.addRide(r);
            c.travelTo(r.startRow, r.startColumn);
            int wait = r.startTime - c.getTick();
            if(wait > 0) {
                c.wait(wait);
            }
            c.travelTo(r.endRow, r.endColumn);
            world.rides.remove(r);

            //System.out.println("Done tick: " + c.getTick());
            //System.out.println();

        }
        //System.out.println("Miss score: " + missScore);
        return world;
    }
}
