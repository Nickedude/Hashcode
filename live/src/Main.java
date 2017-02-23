import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.stream.Collectors;
import java.util.*;
import java.io.*;

public class Main {
	

	public static void main (String[] args) {
		System.out.println("Hello world!");
		Util utilen = new Util();
		World world = utilen.parseWorld(Paths.get("me_at_the_zoo.in"));
		world.printWorld();

		Algorithm alg = new Algorithm(world);
		alg.calculate();
		utilen.printToFile(world,"zooout.txt");
	}
}