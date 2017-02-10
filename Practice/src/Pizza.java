import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



public class Pizza {
    public Cell[][] cells;
    public List<Slice> slices = new ArrayList<>();
    private int minSize, maxSize, rows, columns;


    public Pizza(Path filepath) {
        try {
            List<String> lines = Files.lines(filepath, StandardCharsets.UTF_8).collect(Collectors.toList());

            rows = Character.getNumericValue(lines.get(0).charAt(0));
            columns = Character.getNumericValue(lines.get(0).charAt(2));
            maxSize = Character.getNumericValue(lines.get(0).charAt(6));
            minSize = Character.getNumericValue(lines.get(0).charAt(4));

            lines.remove(0);

            cells = new Cell[rows][columns];

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    cells[r][c] = getType(lines.get(r).charAt(c));
                }
            }
        } catch (Exception e) {
            System.out.println("Couldn't read file: " + filepath);
        }
    }

    private Cell getType(char c) {
        if (c == 'T')
            return Cell.TOMATO;
        return Cell.MUSHROOM;
    }

    public void calculateSlices () {

        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                 count += (cells[r][c] == Cell.TOMATO ?  1 : -1);
            }
        }

        Cell minType = (count < 0 ? Cell.TOMATO : Cell.MUSHROOM);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if(cells[r][c] == minType) {
                    slices.add(maximizeSize(new Slice (r, c, r, c)));
                }
            }
        }
    }

    private Slice maximizeSize(Slice slice) {
        if(isValidSlice(slice)) {
            Slice bestSlice = slice;
            Slice candidates[] = {  maximizeSize(new Slice(slice).resize(0,-1,0,0)), maximizeSize(new Slice(slice).resize(-1,0,0,0)),
                                    maximizeSize(new Slice(slice).resize(0,0,1,0)), maximizeSize(new Slice(slice).resize(0,0,0,1)) };

            for (Slice candidate: candidates) {
                if(isValidSlice(candidate) && getSliceScore(candidate) > getSliceScore(bestSlice)) {
                    bestSlice = candidate;
                }
            }

            return bestSlice;
        }
        return Slice.INVALID_SLICE;
    }

    private int getSliceScore(Slice s) {
        return ((!isValidSlice(s)) ? 0 : s.getArea());
    }

    private boolean isValidSlice(Slice slice) {
        if(slice.r1 < 0 || slice.c1 < 0 || slice.r2 >= rows || slice.c2 >= columns || slice.getArea() > maxSize)
            return false;
        for (Slice s: slices) {
            if(s.intersects(slice))
                return false;
        }
        return true;
    }

    public void printCells() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                System.out.print(cells[r][c] + (cells[r][c] == Cell.MUSHROOM ? "  " :  "    "));
            }
            System.out.println();
        }
    }

    public void printHeader() {
        System.out.println("Min: " + minSize + ", Max: " + maxSize + ", Rows: " + rows + ", Columns: " + columns );
    }

    public void printSlices() {
        slices.forEach(slice -> System.out.println("(" + slice.r1 + " " + slice.c1 + ")" + " -> " + "(" + slice.r2 + " " + slice.c2 + "), Points: " + slice.getArea()));
    }

    private enum Cell {
        TOMATO, MUSHROOM
    }
}

