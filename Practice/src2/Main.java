import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main (String args[]) {
        Pizza pizza = new Pizza(Paths.get("medium.in"));
        pizza.calculateSlices2();
        pizza.printHeader();
        //pizza.printCells();
        pizza.printSlices();
        //pizza.printOccupied();
        pizza.pizzaToFile("result.txt");

    }
}
