import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {
    public static World parse(File file) throws IOException {
        Scanner scan = new Scanner(file);
        int rows = scan.nextInt();
        int cols = scan.nextInt();
        int vehicles = scan.nextInt();
        int nRides = scan.nextInt();
        int timeBonus = scan.nextInt();
        int T = scan.nextInt();

        List<Ride> rides = new ArrayList<>();

        for(int i = 0; i < nRides; i++) {
            int startRow = scan.nextInt();
            int startCol = scan.nextInt();
            int endRow = scan.nextInt();
            int endCol = scan.nextInt();
            int startTime = scan.nextInt();
            int endTime = scan.nextInt();

            rides.add(new Ride(startRow, startCol, endRow, endCol, startTime, endTime));
        }


        return new World(T,timeBonus, vehicles, rides);
    }
}
