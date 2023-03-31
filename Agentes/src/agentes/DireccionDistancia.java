package agentes;

public record DireccionDistancia (String direccion, double distancia){
    @Override
    public String toString() {
        return "DireccionDistancia{" +
                "direccion='" + direccion + '\'' +
                ", distancia=" + distancia +
                '}';
    }
}
