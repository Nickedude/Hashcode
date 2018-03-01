
public class Ride {
    final int startColumn;
    final int startRow;
    final int endColumn;
    final int endRow;
    final int startTime;
    final int finnishTime;
    final int rideID;

    public Ride(int sr, int sc, int er, int ec, int st, int ft, int id) {
        startColumn = sc;
        startRow = sr;
        endColumn = ec;
        endRow = er;
        startTime = st;
        finnishTime = ft;
        rideID = id;
    }

    public int getDistance() {
        return Math.abs(endRow - startRow) + Math.abs(endColumn - startColumn);
    }
}
