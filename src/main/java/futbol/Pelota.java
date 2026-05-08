package futbol;

public class Pelota {

    private Equipo equipoActual;
    private int indiceJugador;

    public Pelota(Equipo equipoActual, int indiceJugador) {
        this.equipoActual = equipoActual;
        this.indiceJugador = indiceJugador;
    }

    public Equipo getEquipoActual() {
        return equipoActual;
    }

    public void setEquipoActual(Equipo equipoActual) {
        this.equipoActual = equipoActual;
    }

    public int getIndiceJugador() {
        return indiceJugador;
    }

    public void setIndiceJugador(int indiceJugador) {
        this.indiceJugador = indiceJugador;
    }
}