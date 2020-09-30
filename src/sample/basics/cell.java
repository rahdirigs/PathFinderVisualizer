package sample.basics;

public class cell {
    public int i, j, distance, Pi, Pj;

    public long f, g, h;
    public boolean inPQ;

    public cellState state;

    public cell(int i, int j) {
        this.i = i;
        this.j = j;
        state = cellState.UNVISITED;
    }

    public void AStarInit() {
        this.f = this.g = this.h = Long.MAX_VALUE;
        this.inPQ = false;
    }

    public void setParent(int i, int j) {
        this.Pi = i;
        this.Pj = j;
    }

    @Override
    public String toString() {
        return "Cell{" + "i = " + i + ", j = " + j + ", f = " + f + ", g = " + g + ", h = " + h + "}";
    }
}
