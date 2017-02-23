import java.nio.file.*;

public class Main {
	

	public static void main (String[] args) {
		System.out.println("Hello world!");
		Util utilen = new Util();
		World world = utilen.parseWorld(Paths.get("kittens.in"));
		world.printWorld();

		Algorithm alg = new Algorithm(world);
		alg.calculate();
		utilen.printToFile(world,"kittenout.txt");
	}
}