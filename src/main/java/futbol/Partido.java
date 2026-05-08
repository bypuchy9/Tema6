package futbol;

import java.util.Random;

public class Partido {

    private Equipo equipoA;
    private Equipo equipoB;

    private Pelota pelota;

    private Narrador narrador;

    private Random random;

    public Partido(Equipo equipoA, Equipo equipoB) {

        this.equipoA = equipoA;
        this.equipoB = equipoB;

        random = new Random();

        narrador = new Narrador();

        if (random.nextBoolean()) {
            pelota = new Pelota(equipoA, 5);
        } else {
            pelota = new Pelota(equipoB, 5);
        }
    }
    public void jugar() throws InterruptedException {

        long inicio = System.currentTimeMillis();

        long duracion = 60000;

        while (System.currentTimeMillis() - inicio < duracion) {

            ejecutarTurno();

            Thread.sleep(2000);
        }

        narrador.narrar("FIN DEL PARTIDO");

        narrador.narrar(
                equipoA.getNombre() + " "
                        + equipoA.getGoles()
                        + " - "
                        + equipoB.getGoles()
                        + " "
                        + equipoB.getNombre()
        );
    }
    private void ejecutarTurno() {

        Equipo atacante = pelota.getEquipoActual();

        Equipo defensor;

        if (atacante == equipoA) {
            defensor = equipoB;
        } else {
            defensor = equipoA;
        }

        int indice = pelota.getIndiceJugador();

        Jugador jugador = atacante.getJugadores()[indice];

        Jugador rival = defensor.getJugadores()[10 - indice];

        Accion accion = elegirAccion(indice);

        switch (accion) {

            case PASE:
                hacerPase(jugador, rival);
                break;

            case REGATE:
                hacerRegate(jugador, rival);
                break;

            case TIRO:
                hacerTiro(jugador, defensor);
                break;
        }
    }
    private Accion elegirAccion(int indice) {

        if (indice >= 7) {

            int numero = random.nextInt(3);

            return Accion.values()[numero];
        }

        int numero = random.nextInt(2);

        if (numero == 0) {
            return Accion.PASE;
        }

        return Accion.REGATE;
    }
    private int modificar(int valor) {

        double variacion = 0.9 + (random.nextDouble() * 0.2);

        return (int) (valor * variacion);
    }
    private void hacerPase(Jugador atacante, Jugador rival) {

        int pase = modificar(atacante.getPase());

        int defensa = modificar(rival.getDefensa());

        if (pase > defensa) {

            pelota.setIndiceJugador(
                    pelota.getIndiceJugador() + 1
            );

            narrador.narrar(atacante + " hace un pase correcto");

        } else {

            cambiarPosesion();

            narrador.narrar(rival + " roba el balón");
        }
    }
    private void hacerRegate(Jugador atacante, Jugador rival) {

        int regate = modificar(atacante.getRegate());

        int defensa = modificar(rival.getDefensa());

        if (regate > defensa) {

            pelota.setIndiceJugador(
                    pelota.getIndiceJugador() + 1
            );

            narrador.narrar(atacante + " supera al rival");

        } else {

            cambiarPosesion();

            narrador.narrar(rival + " recupera el balón");
        }
    }
    private void hacerTiro(Jugador atacante, Equipo defensor) {

        int indice = pelota.getIndiceJugador();

        double factor = 1 - ((10 - indice) * 0.10);

        int tiro = (int) (
                modificar(atacante.getTiro()) * factor
        );

        Jugador portero = defensor.getJugadores()[0];

        int parada = modificar(portero.getPortero());

        if (tiro > parada) {

            pelota.getEquipoActual().marcarGol();

            narrador.narrar("GOOOOOOL");

        } else {

            narrador.narrar("PARADÓN DEL PORTERO");
        }

        reiniciarCentro();
    }
    private void cambiarPosesion() {

        if (pelota.getEquipoActual() == equipoA) {

            pelota.setEquipoActual(equipoB);

        } else {

            pelota.setEquipoActual(equipoA);
        }

        pelota.setIndiceJugador(5);
    }
    private void reiniciarCentro() {

        cambiarPosesion();
    }
}