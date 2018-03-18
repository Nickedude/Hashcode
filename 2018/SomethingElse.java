import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class SomethingElse {
    World world;

    public void calculate(World world) {
        this.world = world;
        PriorityQueue<RideScorePair> queue;
        int n = world.rides.size();
        int x = 0;
        double percentage = 0.05;

        while(!world.rides.isEmpty()) {
            PriorityQueue<PriorityQueue<RideScorePair>> scorePerCar = new PriorityQueue<>(new QueueComparator());
            for(Car c : world.cars) {
                scorePerCar.add(getQueuePerCar(c));
            }
            for(PriorityQueue<RideScorePair> scorePair : scorePerCar) {
                if(world.rides.isEmpty())
                    return;

                RideScorePair p;
                while((p = scorePair.poll()).ride.isAssigned) {
                    if(world.rides.isEmpty())
                        return;
                }

                if(((double) x) / n > percentage) {
                    System.out.println((int) (percentage * 100) + "%");
                    percentage += 0.05;
                }

                RideScorePair rsp = p;
                rsp.car.addRide(rsp.ride);
                rsp.car.travelTo(rsp.ride.startRow, rsp.ride.startColumn);
                rsp.car.travelTo(rsp.ride.endRow, rsp.ride.endColumn);
                rsp.ride.isAssigned = true;
                world.rides.remove(rsp.ride);
                x++;
            }
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

    public PriorityQueue<RideScorePair> getQueuePerCar(Car car) {
        PriorityQueue<RideScorePair> queue = new PriorityQueue<>(new PairComparator());

        for (Ride ride : world.rides) {
            queue.add(new RideScorePair(ride, car, world));
        }

        return queue;
    }

    public class PairComparator implements Comparator<RideScorePair> {
        @Override
        public int compare(RideScorePair o1, RideScorePair o2) {
            return o2.score - o1.score;
        }
    }

    public class QueueComparator implements Comparator<PriorityQueue<RideScorePair>> {
        @Override
        public int compare(PriorityQueue<RideScorePair> o1, PriorityQueue<RideScorePair> o2) {
            return o2.peek().score - o1.peek().score;
        }
    }

}
