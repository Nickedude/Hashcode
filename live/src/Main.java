import java.nio.file.*;

public class Main {
	

	public static void main (String[] args) {
		System.out.println("Hello world!");
		Util utilen = new Util();
		World world = utilen.parseWorld(Paths.get(args[0]));
		//world.printWorld();
		Algorithm2 alg = new Algorithm2(world);
		alg.calculate();
		alg.printFreeSpace();
		utilen.printToFile(world,args[1]);
	}
}