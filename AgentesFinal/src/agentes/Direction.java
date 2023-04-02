package agentes;

public record Direction(int i, int j) {
    @Override
    public String toString() {
        return "Direction{" + "i=" + i + ", j=" + j + '}';
    }
}
