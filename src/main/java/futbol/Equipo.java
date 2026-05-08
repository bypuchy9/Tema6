package futbol;

public class Equipo {

    private String nombre;
    private Jugador[] jugadores;
    private int goles;

    public Equipo(String nombre, Jugador[] jugadores) {
        this.nombre = nombre;
        this.jugadores = jugadores;
        this.goles = 0;
    }

    public String getNombre() {
        return nombre;
    }

    public Jugador[] getJugadores() {
        return jugadores;
    }

    public int getGoles() {
        return goles;
    }

    public void marcarGol() {
        goles++;
    }
}