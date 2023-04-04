package agentes;

public record Position(int i, int j) {
    public Position plus(Position pos) {
        return new Position(i() + pos.i(), j() + pos.j());
    }

    public Position minus(Position pos) {
        return new Position(i() - pos.i(), j() - pos.j());
    }

    public int manhattan(final Position second) {
        Position diff = minus(second);
        return Math.abs(diff.i) + Math.abs(diff.j);
    }

    @Override
    public String toString() {
        return "Position{" + "i=" + this.i + ", j=" + this.j + '}';
    }
}
