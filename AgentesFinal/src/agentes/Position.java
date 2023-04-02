package agentes;

public record Position(int i, int j) {
    public Direction getDirection(){
        return new Direction(i(), j());
    }
    public Position addPosition(Position pos) {
        return new Position(this.i() + pos.i(), this.j() + pos.j());
    }
    public Position addDirection(Direction dir) {
        return new Position(this.i() + dir.i(), this.j() + dir.j());
    }

    public Position subtractDirection(Direction dir) {
        return new Position(this.i() - dir.i(), this.j() - dir.j());
    }

    public Position subtractPosition(Position pos) {
        return new Position(this.i() - pos.i(), this.j() - pos.j());
    }
    public int manhattan() {
        return Math.abs(i) + Math.abs(j);
    }
    @Override
    public String toString() {
        return "Position{" + "i=" + i + ", j=" + j + '}';
    }
}
