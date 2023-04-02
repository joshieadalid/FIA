package agentes;

public record Position(int i, int j) {
    public Position sumDirection(Direction dir) {
        return new Position(this.i() + dir.i(), this.j() + dir.j());
    }

    public Position subtractDirection(Direction dir) {
        return new Position(this.i() - dir.i(), this.j() - dir.j());
    }

    public Position subtractPosition(Position pos) {
        return new Position(this.i() - pos.i(), this.j() - pos.j());
    }

    @Override
    public String toString() {
        return "Position{" + "i=" + i + ", j=" + j + '}';
    }
}
