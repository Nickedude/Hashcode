public class Main {
	

	public static void main (String[] args) {
		System.out.println("Hello world!");
		Util utilen = new Util();
		System.out.println(args[0]);
		World world = utilen.parseWorld(args[0]);
		System.out.println("Success!");
		world.printworld();
	}
}