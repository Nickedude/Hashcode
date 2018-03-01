import java.util.ArrayList;
import java.util.List;

public class Car {


    List<Ride> rides = new ArrayList<>();
    private int r = 0;
    private int c = 0;

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
        return rides.get(rides.size() - 1);
    }

    int getEndTime() {
        return getLastRide().finnishTime;
    }

    int getEndColumn() {
        return getLastRide().endColumn;
    }

    int getEndRow() {
        return getLastRide().endRow;
    }



}
