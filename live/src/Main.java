import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.stream.Collectors;
import java.util.*;
import java.io.*;

public class Main {
	

	public static void main (String[] args) {
		System.out.println("Hello world!");
		Util utilen = new Util();
		System.out.println(args[0]);
		World world = utilen.parseWorld(Paths.get(args[0]));
		world.printWorld();
		utilen.printToFile(world,args[1]);
	}
}