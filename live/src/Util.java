import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.stream.Collectors;
import java.util.*;
import java.io.*;


public class Util {
   

   public void parseFile(Path filepath) {
        try {
            List<String> lines = Files.lines(filepath, StandardCharsets.UTF_8).collect(Collectors.toList());

            String[] firstLine = lines.get(0).split("[\\s]");
            int rows = Integer.parseInt(firstLine[0]);
            int columns = Integer.parseInt(firstLine[1]);
            int maxSize = Integer.parseInt(firstLine[3]);
            int minCont = Integer.parseInt(firstLine[2]);

            lines.remove(0);

            for (int r = 0; r < rows; r++) {
                
            }

        } catch (Exception e) {
            System.out.println("Couldn't read file: " + filepath);
        }
    }

    public void printToFile (String path) {
        try {
            PrintWriter out = new PrintWriter(path);
            out.println("Stuff");
            out.println("More stuff");
            out.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }
}