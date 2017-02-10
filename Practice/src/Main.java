import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main (String args[]) {
        Pizza pizza = new Pizza(Paths.get("example.in"));
        pizza.calculateSlices();
        pizza.printHeader();
        pizza.printCells();
        pizza.printSlices();

    }
}
