import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Output {
    public static void outputWorld(World world, String filename) throws IOException {

        FileWriter fileWriter = new FileWriter(filename + ".out");
        PrintWriter printWriter = new PrintWriter(fileWriter);

        for (Car car : world.cars) {
            printWriter.write("" + car.rides.size());
            car.rides.forEach(ride -> printWriter.write(" " + ride.rideID));
            printWriter.write("\n");
        }
        printWriter.close();
    }
}
