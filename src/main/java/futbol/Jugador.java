package futbol;

public class Jugador {

    private int dorsal;
    private String nombre;

    private int portero;
    private int defensa;
    private int regate;
    private int pase;
    private int tiro;

    public Jugador(int dorsal, String nombre,int portero, int defensa, int regate, int pase, int tiro) {

        this.dorsal = dorsal;
        this.nombre = nombre;
        this.portero = portero;
        this.defensa = defensa;
        this.regate = regate;
        this.pase = pase;
        this.tiro = tiro;
    }

    public int getPortero() {
        return portero;
    }

    public int getDefensa() {
        return defensa;
    }

    public int getRegate() {
        return regate;
    }

    public int getPase() {
        return pase;
    }

    public int getTiro() {
        return tiro;
    }

    @Override
    public String toString() {
        return nombre;
    }
}