package sample.basics;

public class stateNum {
    private stateNum() {}

    public static cellState getNum(char ch) {
        switch (ch) {
            case '-': return cellState.UNVISITED;
            case 'x': return cellState.TARGET;
            case '.': return cellState.SOURCE;
            case '*': return cellState.WALL;
        }
        return null;
    }

    public static char getChar(cellState state) {
        if (state == cellState.SOURCE)
            return 'o';
        if (state == cellState.WALL)
            return '*';
        if (state == cellState.TARGET)
            return 'x';
        return '-';
    }
}
