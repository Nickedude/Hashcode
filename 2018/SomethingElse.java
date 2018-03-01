import java.util.Stack;

public class SomethingElse {

    public void calculate(World world) {
        Stack<Ride> rides = new Stack<>();
        rides.addAll(world.rides);

        for(int tick = 0; tick < world.T; tick++) {
            for (Car c: world.cars) {
                if(rides.isEmpty())
                    break;
                if(c.getEndTime() >= tick) {
                    c.addRide(rides.pop());
                }
            }
        }
    }
}
