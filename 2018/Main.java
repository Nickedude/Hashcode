import java.io.File;

public class Main {
    public static void main(String[] args) {
        String path = "in/";
        String fileName = "a_example.in";
        System.out.println("Starting file: " + fileName);
        World world = null;
        try {
            world = Parser.parse(new File(fileName));
        } catch (Exception e) {e.printStackTrace();}
        GreedyCar greedyCar = new GreedyCar(world);
        world = greedyCar.compute();
        System.out.println("Finnished file: " + fileName);

    }

}
