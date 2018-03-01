import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.nio.file.*;

public class Main {
	

	public static void main (String[] args) {

        System.out.println("Start1");
		Util utilen = new Util();
		World world;
        Algorithm2 alg;

		world = utilen.parseWorld(Paths.get("kittens.in"));
		alg = new Algorithm2(world);
		alg.calculate();
        alg.calculate3();

        //alg.optimize();
		utilen.printToFile(world,"kittens.out");
        System.out.println("Kitten Done: " + world.score());
        //System.out.println(world.score());

        world = utilen.parseWorld(Paths.get("me_at_the_zoo.in"));
        alg = new Algorithm2(world);
        alg.calculate();
        alg.calculate3();
        //alg.calculate2();
        utilen.printToFile(world,"me_at_the_zoo.out");
        System.out.println("Zoo Done: " + world.score());
        //System.out.println(world.score());

        world = utilen.parseWorld(Paths.get("trending_today.in"));
        alg = new Algorithm2(world);
        alg.calculate();
        alg.calculate3();
        //alg.optimize();
        //alg.analyzeOptimization();
        utilen.printToFile(world,"trending_today.out");
        System.out.println("Trending Done: " + world.score());
       // System.out.println(world.score());

        world = utilen.parseWorld(Paths.get("videos_worth_spreading.in"));
        alg = new Algorithm2(world);
        alg.calculate();
        alg.calculate3();
        utilen.printToFile(world,"videos_worth_spreading.out");
        System.out.println("Videos worth spreading Done: " + world.score());
       // System.out.println(world.score());


        System.out.println("All Done");

    }
}