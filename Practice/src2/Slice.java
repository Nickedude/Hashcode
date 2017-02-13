

public class Slice {

    public static final Slice INVALID_SLICE = new Slice(-1,-1,-1,-1,-1);

    public int r1, c1, r2, c2, slicenmbr;

    public Slice (int r1, int c1, int r2, int c2, int slicenmbr){
        this.r1 = r1;
        this.r2 = r2;
        this.c1 = c1;
        this.c2 = c2;
        this.slicenmbr = slicenmbr;
    }

    public Slice (Slice s){
        this.r1 = s.r1;
        this.r2 = s.r2;
        this.c1 = s.c1;
        this.c2 = s.c2;
        this.slicenmbr = s.slicenmbr;
    }

    //Gives the area (points of a slice)
    public int getArea() {
        return (Math.abs(r2-r1)+1) * (Math.abs(c2-c1)+1);
    }

    public boolean intersects (Slice s) {
        return ! (s.c1 > c2 || s.c2 < c1 || s.r2 > r1 || s.r1 < r2);
    }

    public Slice resize(int up, int left, int down, int right) {
        this.r1 += up;
        this.r2 += down;
        this.c1 += left;
        this.c2 += right;
        return this;
    }

    public String sliceToString() {
        return("(" + r1 + " " + c1 + ")" + " -> " + "(" + r2 + " " + c2 + ")");
    }

}
