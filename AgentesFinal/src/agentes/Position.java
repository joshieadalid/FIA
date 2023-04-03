package agentes;

public record Position(int i, int j) {
    public Direction getDirection(){
        return new Direction(i(), j());
    }
    public Position plus(Position pos) {
        return new Position(this.i() + pos.i(), this.j() + pos.j());
    }
    public Position plus(Direction dir) {
        return new Position(this.i() + dir.i(), this.j() + dir.j());
    }

    public Position minus(Position pos) {
        return new Position(this.i() - pos.i(), this.j() - pos.j());
    }
    public int manhattan() {
        return Math.abs(i) + Math.abs(j);
    }

    public int manhattan(Position second) {
        return this.minus(second).manhattan();
    }

    @Override
    public String toString() {
        return "Position{" + "i=" + i + ", j=" + j + '}';
    }
}
