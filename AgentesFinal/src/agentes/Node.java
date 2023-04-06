package agentes;

public record Node(Node parent, Position position, int manhattan) implements Comparable<Node> {
    @Override
    public String toString() {
        return "Node{" + ", position=" + position + ", manhattan=" + manhattan + '}';
    }

    @Override
    public int compareTo(Node o) {
        return Double.compare(this.manhattan, o.manhattan);
    }
}
