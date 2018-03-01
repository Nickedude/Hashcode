import java.io.File;

public class Main {
    public static void main(String[] args) {
        try {
            String fileName = "a_example.in";
            System.out.println("Starting file: " + fileName);
            World world = Parser.parse(new File(fileName));
            System.out.println("Finnished file: " + fileName);
            GreedyCar greedyCar = new GreedyCar(world);
            world = greedyCar.compute();
            System.out.println("Finnished file: " + fileName);
            Output.outputWorld(world, fileName);
        } catch (Exception e) {e.printStackTrace();}
    }

}
