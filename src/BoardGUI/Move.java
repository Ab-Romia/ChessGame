package BoardGUI;

import ChessCore.Enum.CoordinateEnum;

public class Move {
    private CoordinateEnum src;
    private CoordinateEnum dst;
    public Move(CoordinateEnum src, CoordinateEnum dst) {
        this.src = src;
        this.dst = dst;
    }
    public CoordinateEnum getSrc() {
        return src;
    }
    public CoordinateEnum getDst() {
        return dst;
    }
}
