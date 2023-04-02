package agentes;

public record Direction(int i, int j) {
    public Position toPosition(){
        return new Position(i, j);
    }
    @Override
    public String toString() {
        return "Direction{" + "i=" + i + ", j=" + j + '}';
    }

}
