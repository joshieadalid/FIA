package agentes;

public class Position {
    public int i;
    public int j;

    public Position(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public String toString() {
        return "(%d, %d)".formatted(i, j);
    }
}
