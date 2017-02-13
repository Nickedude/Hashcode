import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.*;



public class Pizza {
    public Cell[][] cells;
    public int[][] occupied; //nr at position = the slice in slices (the list) that this slice belongs to
    public List<Slice> slices = new ArrayList<>();
    private int minCont, maxSize, rows, columns;


    public Pizza(Path filepath) {
        try {
            List<String> lines = Files.lines(filepath, StandardCharsets.UTF_8).collect(Collectors.toList());

            rows = Character.getNumericValue(lines.get(0).charAt(0));
            columns = Character.getNumericValue(lines.get(0).charAt(2));
            maxSize = Character.getNumericValue(lines.get(0).charAt(6));
            minCont = Character.getNumericValue(lines.get(0).charAt(4));

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

        Cell minType = leastOccuring();

        int slicecounter = 0;

        //Create 1x1 slices
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                if(cells[r][c] == minType) {
                    Slice temp = new Slice(r, c, r, c, slicecounter);
                    slices.add(temp);
                    markOccupied(temp);     //Mark this spot as taken
                    slicecounter++;
                }
            }
        }

        while(!allSlicesValid(slices)) {                        //Expands the small slices until they're all valid
            expandSlices2(slices);
            for(int i = 0; i < slices.size(); i++) {    
                if(!isValidSlice(slices.get(i))) {              //If it's not valid
                    markFree(slices.get(i).slicenmbr);          //Then remove it
                    slices.remove(i);
                }
            }
        }

        for(int i = 0; i < 10; i++) {
            Slice prospect = getProspect(leastOccuring());  //Get a prospect for a new slice starting in a cell of the least occuring type
            if(prospect == null)                            //No new slices could be placed
                break;
            else {
                slices.add(prospect);                       //Add the prospect
                expandSlices(slices);             //Expand it 
            }
        }

        for(int i = 0; i < slices.size(); i++) {    
            if(!isValidSlice(slices.get(i))) {              //If it's not valid
                markFree(slices.get(i).slicenmbr);          //Then remove it
                slices.remove(i);
            }
        }
    }

    //Returns a prospect for a slice
    private Slice getProspect (Cell type) {
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < columns; col++) {
                if(occupied[row][col] == -1 && cells[row][col] == type) {   //If it isn't occupied and if it's of the right type
                    int slicenmbr = nextSliceNmbr();
                    occupied[row][col] = slicenmbr;
                    return(new Slice(row,col,row,col, slicenmbr));
                }
            }
        }
        return null;
    }

    private int nextSliceNmbr () {
        int next = 0;
        for(Slice slice: slices) {
            if(slice.slicenmbr > next)
                next = slice.slicenmbr;
        }
        return next+1;
    }


    //Find the least occuring type
    private Cell leastOccuring() {
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                 count += (cells[r][c] == Cell.TOMATO ?  1 : -1);
            }
        }

        Cell minType = (count < 0 ? Cell.TOMATO : Cell.MUSHROOM);
        return minType;
    }

    //Another expansion strategy than expandSlices
    private void expandSlices2 (List<Slice> slices) {
        for(int i = 0; i < slices.size(); i++) {
                Slice temp = slices.get(i);
                Slice newtemp;
                newtemp = expandLeft2(temp);
                if(isValidSliceArea(newtemp)) {
                    temp = newtemp;
                }
                
                newtemp = expandRight2(temp);
                
                if(isValidSliceArea(newtemp)) {
                    temp = newtemp;
                }

                newtemp = expandUp(temp);

                if(isValidSliceArea(newtemp)) {
                    temp = newtemp;
                }
                newtemp = expandDown(temp);

                if(isValidSliceArea(newtemp)) {
                    temp = newtemp;
                }

                if(isValidSlice(temp)) {
                    slices.set(i,temp);
                    markOccupied(temp);
                }
        }
    }

    //Takes minimum slices and expands them 
    private void expandSlices (List<Slice> slices) {
        for(int i = 0; i < slices.size(); i++) {
                System.out.println(slices.get(i).slicenmbr);
                Slice temp = new Slice(slices.get(i));
                Slice newtemp;
                newtemp = expandLeft(temp);
                if(isValidSliceArea(newtemp)) {
                    System.out.println("left");
                    temp = newtemp;
                }
                
                newtemp = expandRight(temp);
                
                if(isValidSliceArea(newtemp)) {
                    System.out.println("right");
                    temp = newtemp;
                }

                newtemp = expandUp(temp);

                if(isValidSliceArea(newtemp)) {
                    temp = newtemp;
                }
                //else {
                    //while(!isValidSlice(temp)) {
                        //temp = shrinkSliceByOneDown(temp);
                  //  }
                //}
                newtemp = expandDown(temp);

                if(isValidSliceArea(newtemp)) {
                    temp = newtemp;
                }

                if(isValidSlice(temp)) {
                    slices.set(i,temp);
                    markOccupied(temp);
                }
        }
    }

    //Marks all the cells of a slices as free
    private void markFree(int slicenmbr) {
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < columns; col++) {
                if(occupied[row][col] == slicenmbr) {
                    occupied[row][col] = -1;
                } 
            }
        }

    }

    //Marks all the cells of a slices as occupied
    private void markOccupied(Slice s) {
        for(int i = s.r1; i < s.r2+1; i++)
            for(int j = s.c1; j < s.c2+1; j++) {
                occupied[i][j] = s.slicenmbr;
            }

    }

    //Checks that all slices in a list are valid
    private boolean allSlicesValid(List<Slice> slices) {
        for(int i = 0; i < slices.size(); i++) {
            if(!isValidSlice(slices.get(i)))
                return false;
        }
        return true;
    }

    //Expands a slice to the left
    private Slice expandLeft (Slice s) {
        Slice temp = new Slice(s);
        while(temp.c1-1 > -1 && occupied[temp.r1][temp.c1-1] == -1) {
            temp.c1--;
        }
        return temp;
    }

    private Slice expandLeft2 (Slice s) {
        Slice temp = new Slice(s);
        while(temp.c1-1 > -1) {
            for(int i = temp.r1; i < temp.r2+1; i++) {
                if(occupied[i][temp.c1-1] != -1)
                    return s;
                    System.out.println("Stopped expanding " + s.slicenmbr + " at " + i + "," + (temp.c1-1));
            }
            temp.c1--;
        }
        return temp;
    }

    private Slice expandRight2 (Slice s) {
        Slice temp = new Slice(s);
        while(temp.c2+1 < columns) {
            for(int i = temp.r1; i < temp.r2+1; i++) {
                if(occupied[i][temp.c2+1] != -1)
                    return s;
            }
            temp.c2++;
        }
        return temp;
    }

    //Expands a slice to the right
    private Slice expandRight (Slice s) {
        Slice temp = new Slice(s);
        while(temp.c2+1 < columns && occupied[temp.r1][temp.c2+1] == -1) {
            temp.c2++;
        }
        return temp;
    }

    //Expands a slice upwards
    private Slice expandUp (Slice s) {
        Slice temp = new Slice(s);
        while(temp.r1-1 > -1) {                  //Check if one row upwards is within bounds
            for(int i = temp.c1; i < temp.c2+1; i++) {  //For all columns in the possible expansion
                if(occupied[temp.r1-1][i] != -1)         //Check that they're available
                    return s;                       //If not, return
            }                                       //If we made it through the previous loop all the cells are available
            temp.r1--;                              //Expand;
        }

        return temp;
    }

    private Slice expandDown (Slice s) {
        Slice temp = s;
        while(temp.r2+1 < rows) {                  //Check if one row upwards is within bounds
            for(int i = temp.c1; i < temp.c2+1; i++) {  //For all columns in the possible expansion
                if(occupied[temp.r2+1][i] != -1)         //Check that they're available
                    return s;                       //If not, return
            }                                       //If we made it through the previous loop all the cells are available
            temp.r2++;                              //Expand;
        }

        return temp;
    }

    private int getSliceScore(Slice s) {
        return ((!isValidSlice(s)) ? 0 : s.getArea());
    }

    //Checks that a slice is valid
    private boolean isValidSlice(Slice slice) {
        return isValidSliceArea(slice) && isValidSliceContent(slice);
    }

    //Checks that a slice contains the minimum amount of shrooms and tomatos
    private boolean isValidSliceContent(Slice slice) {
        int shrmcnt = 0;
        int tmtocnt = 0;

        for(int row = slice.r1; row < slice.r2+1; row++) {
            for(int col = slice.c1; col < slice.c2+1; col++) {
                if(cells[row][col] == Cell.TOMATO)
                    tmtocnt++;
                else
                    shrmcnt++;
            }
        }

        if(shrmcnt >= minCont && tmtocnt >= minCont)
            return true;
        else
            return false;
    }

    //Checks if a slice is within bounds, not too large and that it doesn't intersect with any other
    private boolean isValidSliceArea(Slice slice) {
        if(slice.r1 < 0 || slice.c1 < 0 || slice.r2 >= rows || slice.c2 >= columns || slice.getArea() > maxSize)
            return false;
        else
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
        System.out.println("Min: " + minCont + ", Max: " + maxSize + ", Rows: " + rows + ", Columns: " + columns );
    }

    public void printSlices() {
        int tot = 0;
        for(Slice slice: slices) {
            System.out.println(slice.sliceToString() + ", Points: " + slice.getArea());
            tot += slice.getArea();
        }
        System.out.println("Total points: " + tot + " out of " + (rows*columns) + " possible.");
    }

    public void printOccupied() {
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < columns; col++) {
                System.out.print(occupied[row][col] + " ");
            }
            System.out.println(" ");
        }
    }

    private enum Cell {
        TOMATO, MUSHROOM
    }

    public void pizzaToFile(String path) {
        Slice temp = Slice.INVALID_SLICE;
        try {
            PrintWriter out = new PrintWriter(path);
            out.println(slices.size());
            for(int i = 1; i < slices.size(); i++) {
                temp = slices.get(i);
                out.println(temp.r1 + " " + temp.c1 + " " + temp.r2 + " " + temp.c2);
            }
            out.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }
}

