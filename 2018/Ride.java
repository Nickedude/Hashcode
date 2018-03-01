
public class Ride {
    final int startColumn;
    final int startRow;
    final int endColumn;
    final int endRow;
    final int startTime;
    final int finnishTime;

    public Ride(int sr, int sc, int er, int ec, int st, int ft) {
        startColumn = sc;
        startRow = sr;
        endColumn = ec;
        endRow = er;
        startTime = st;
        finnishTime = ft;
    }
}
