package agentes;

public record Node( Position position,double distance) {
    @Override
    public String toString() {
        return "Node{" +
                "pos=" + position +
                ", distance=" + distance +
                '}';
    }
}
