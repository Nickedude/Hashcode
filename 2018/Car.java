import java.util.ArrayList;
import java.util.List;

public class Car {


    List<Ride> rides = new ArrayList<>();
    private int r = 0;
    private int c = 0;
    private int tick = 0;

    int getCurrentRow() {
        return r;
    }

    int getCurrentColumn() {
        return c;
    }

    void setPos(int r, int c) {
        this.r = r; this.c = c;
    }

    void addRide(Ride r) {
            rides.add(r);
    }

    Ride getLastRide() {
        if(rides.size() == 0) {
            return null;
        }

        return rides.get(rides.size() - 1);
    }

    int getEndTime() {
        if(rides.size() == 0) {
            return 0;
        }

        return getLastRide().finnishTime;
    }

    int getEndColumn() {
        if(rides.size() == 0) {
            return 0;
        }

        return getLastRide().endColumn;
    }

    int getEndRow() {
        if(rides.size() == 0) {
            return 0;
        }

        return getLastRide().endRow;
    }


    public int getDistanceFrom(int r, int c) {
        return Math.abs(getCurrentRow() - r) + Math.abs(getCurrentColumn() - c);
    }

    public void travelTo(int r, int c) {
        tick += getDistanceFrom(r, c);
        this.r = r;
        this.c = c;
    }

    public int getTick() {
        return tick;
    }
}
