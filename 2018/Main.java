import java.io.File;

public class Main {
    public static void main(String[] args) {
        String[] files = {"a_example.in", "b_should_be_easy.in", "c_no_hurry.in", "d_metropolis.in", "e_high_bonus.in"};

        for(String file : files) {
            try {
                SomethingElse s = new SomethingElse();
                System.out.println("Starting file: " + file);
                World world = Parser.parse(new File(file));
                s.calculate(world);
                //GreedyCar greedyCar = new GreedyCar(world);
                //world = greedyCar.compute();
                System.out.println("Finnished file: " + file);
                Output.outputWorld(world, file);
            } catch (Exception e) {e.printStackTrace();}
        }

    }

}
