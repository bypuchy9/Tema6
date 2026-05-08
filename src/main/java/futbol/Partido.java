package futbol;

import java.util.Random;

/**
 * Motor de simulación de partidos de fútbol.
 * @author Manel Malonda
 * @version 1.0
 */
public class Partido {

    private Equipo equipoA;
    private Equipo equipoB;
    private Pelota pelota;
    private Narrador narrador;
    private Random random;

    /**
     * Configura los equipos y realiza el sorteo de saque inicial.
     */
    public Partido(Equipo equipoA, Equipo equipoB) {
        this.equipoA = equipoA;
        this.equipoB = equipoB;
        this.random = new Random();
        this.narrador = new Narrador();

        if (random.nextBoolean()) {
            pelota = new Pelota(equipoA, 5);
        } else {
            pelota = new Pelota(equipoB, 5);
        }
    }
    /**
     * Ejecuta el partido durante 60 segundos con pausas de 2 segundos.
     */
    public void jugar() throws InterruptedException {
        long inicio = System.currentTimeMillis();
        long duracion = 60000;

        while (System.currentTimeMillis() - inicio < duracion) {
            ejecutarTurno();
            Thread.sleep(2000);
        }

        narrador.narrar("FIN DEL PARTIDO");
        narrador.narrar(equipoA.getNombre() + " " + equipoA.getGoles() + " - "
                + equipoB.getGoles() + " " + equipoB.getNombre());
    }
    /**
     * Gestiona el duelo entre el poseedor actual y su rival directo.
     */
    private void ejecutarTurno() {
        Equipo atacante = pelota.getEquipoActual();
        Equipo defensor = (atacante == equipoA) ? equipoB : equipoA;

        int indice = pelota.getIndiceJugador();
        Jugador jugador = atacante.getJugadores()[indice];
        Jugador rival = defensor.getJugadores()[10 - indice];

        Accion accion = elegirAccion(indice);

        switch (accion) {
            case PASE -> hacerPase(jugador, rival);
            case REGATE -> hacerRegate(jugador, rival);
        }
    }
    /**
     * Determina la acción según la posición (habilita TIRO en zona de ataque).
     */
    private Accion elegirAccion(int indice) {
        if (indice >= 7) {
            return Accion.values()[random.nextInt(3)];
        }
        return (random.nextInt(2) == 0) ? Accion.PASE : Accion.REGATE;
    }
    /**
     * Añade un margen de error/azar (90%-110%) a las estadísticas.
     */
    private int modificar(int valor) {
        return (int) (valor * (0.9 + random.nextDouble() * 0.2));
    }
    /**
     * Resuelve el éxito del pase comparando con la defensa rival.
     */
    private void hacerPase(Jugador atacante, Jugador rival) {
        if (modificar(atacante.getPase()) > modificar(rival.getDefensa())) {
            pelota.setIndiceJugador(pelota.getIndiceJugador() + 1);
            narrador.narrar(atacante + " hace un pase correcto");
        } else {
            cambiarPosesion();
            narrador.narrar(rival + " roba el balón");
        }
    }
    /**
     * Resuelve el regate. Si falla, el rival recupera la posesión.
     */
    private void hacerRegate(Jugador atacante, Jugador rival) {
        if (modificar(atacante.getRegate()) > modificar(rival.getDefensa())) {
            pelota.setIndiceJugador(pelota.getIndiceJugador() + 1);
            narrador.narrar(atacante + " supera al rival");
        } else {
            cambiarPosesion();
            narrador.narrar(rival + " recupera el balón");
        }
    }
    /**
     * Cambia el turno de ataque y resetea el balón al centro del campo.
     */
    private void cambiarPosesion() {
        pelota.setEquipoActual(pelota.getEquipoActual() == equipoA ? equipoB : equipoA);
        pelota.setIndiceJugador(5);
    }
    private void reiniciarCentro() {
        cambiarPosesion();
    }
}