public class Util {
   

   public static Integer parseFile(Path filepath) {
        try {
            List<String> lines = Files.lines(filepath, StandardCharsets.UTF_8).collect(Collectors.toList());

            String[] firstLine = lines.get(0).split("[\\s]");
            rows = Integer.parseInt(firstLine[0]);
            columns = Integer.parseInt(firstLine[1]);
            maxSize = Integer.parseInt(firstLine[3]);
            minCont = Integer.parseInt(firstLine[2]);

            lines.remove(0);

            cells = new Cell[rows][columns];
            occupied = new int[rows][columns];

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    cells[r][c] = getType(lines.get(r).charAt(c));
                    occupied[r][c] = -1; //Default value
                }
            }

        } catch (Exception e) {
            System.out.println("Couldn't read file: " + filepath);
        }
    }	
}