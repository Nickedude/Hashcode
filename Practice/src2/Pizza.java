import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



public class Pizza {
    public Cell[][] cells;
    public int[][] occupied; //nr at position = the slice in slices (the list) that this slice belongs to
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

    private Cell getType(char c) {
        if (c == 'T')
            return Cell.TOMATO;
        return Cell.MUSHROOM;
    }

    public void calculateSlices () {

        //Find the least occuring type
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                 count += (cells[r][c] == Cell.TOMATO ?  1 : -1);
            }
        }

        Cell minType = (count < 0 ? Cell.TOMATO : Cell.MUSHROOM);

        int slicecounter = 0;

        //Create 1x1 slices
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if(cells[r][c] == minType) {
                    slices.add(new Slice (r, c, r, c));
                    occupied[r][c] = slicecounter;          //Mark this spot as taken
                    slicecounter++;
                }
            }
        }

        slices = expandSlices(slices);
       /// while(true) {
       //     ArrayList<Slice> nextSlices = expandSlices(slices);
       //     if (slices == nextSlices)
       //         break;
       // }
    }

    private ArrayList<Slice> expandSlices (List<Slice> slices) {
        ArrayList<Slice> nextSlices = new ArrayList<>(slices);
        for(int i = 0; i < nextSlices.size(); i++) {
                Slice temp;
                temp = expandLeft(nextSlices.get(i),i);
                if(isValidSlice(temp)) {
                    nextSlices.set(i,temp);
                }
                
                temp = expandRight(nextSlices.get(i),i);
                
                if(isValidSlice(temp)) {
                    nextSlices.set(i,temp);
                }

                temp = expandUp(nextSlices.get(i),i);

                if(isValidSlice(temp)) {
                    nextSlices.set(i,temp);
                }
                //else {
                    //while(!isValidSlice(temp)) {
                        //temp = shrinkSliceByOneDown(temp);
                  //  }
                //}
                temp = expandDown(nextSlices.get(i),i);

                if(isValidSlice(temp)) {
                    nextSlices.set(i,temp);
                }
        }
        return nextSlices;
    }

    //Expands a slice to the left
    private Slice expandLeft (Slice s, int slicenmbr) {
        Slice temp = s;
        while(temp.c1-1 > -1 && occupied[temp.r1][temp.c1-1] == -1) {
            temp.c1--;
            occupied[temp.r1][temp.c1] = slicenmbr;
        }
        return temp;
    }

    //Expands a slice to the right
    private Slice expandRight (Slice s, int slicenmbr) {
        Slice temp = s;
        while(temp.c2+1 < cells[0].length && occupied[temp.r1][temp.c2+1] == -1) {
            temp.c2++;
            occupied[temp.r1][temp.c1] = slicenmbr;
        }
        return temp;
    }

    //Expands a slice upwards
    private Slice expandUp (Slice s, int slicenmbr) {
        Slice temp = s;
        while(temp.r1-1 > -1) {                  //Check if one row upwards is within bounds
            for(int i = temp.c1; i < temp.c2+1; i++) {  //For all columns in the possible expansion
                if(occupied[temp.r1-1][i] != -1)         //Check that they're available
                    return s;                       //If not, return
            }                                       //If we made it through the previous loop all the cells are available
            temp.r1--;                              //Expand;
            for(int i = temp.c1; i < temp.c2+1; i++) {
                occupied[temp.r1][i] = slicenmbr;        //Mark as occupied
            }
        }

        return temp;
    }

    private Slice expandDown (Slice s, int slicenmbr) {
        Slice temp = s;
        while(temp.r2+1 < cells.length) {                  //Check if one row upwards is within bounds
            for(int i = temp.c1; i < temp.c2+1; i++) {  //For all columns in the possible expansion
                if(occupied[temp.r2+1][i] != -1)         //Check that they're available
                    return s;                       //If not, return
            }                                       //If we made it through the previous loop all the cells are available
            temp.r2++;                              //Expand;
            for(int i = temp.c1; i < temp.c2+1; i++) {
                occupied[temp.r2][i] = slicenmbr;        //Mark as occupied
            }
        }

        return temp;
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

