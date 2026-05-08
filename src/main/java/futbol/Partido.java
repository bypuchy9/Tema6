package futbol;

import java.util.Random;

/**
 * Clase que representa el motor de simulación de un partido de fútbol.
 * Gestiona la lógica de los turnos, las acciones de los jugadores y el cronómetro.
 *
 * @author TuNombre
 * @version 1.0
 */
public class Partido {

    private Equipo equipoA;
    private Equipo equipoB;
    private Pelota pelota;
    private Narrador narrador;
    private Random random;

    /**
     * Constructor de la clase Partido.
     * Inicializa los equipos, el narrador y decide quién inicia con la posesión.
     *
     * @param equipoA El primer equipo contendiente.
     * @param equipoB El segundo equipo contendiente.
     */
    public Partido(Equipo equipoA, Equipo equipoB) {
        this.equipoA = equipoA;
        this.equipoB = equipoB;
        this.random = new Random();
        this.narrador = new Narrador();

        // Sorteo inicial: se asigna la pelota a un equipo al azar en el centro (índice 5)
        if (random.nextBoolean()) {
            pelota = new Pelota(equipoA, 5);
        } else {
            pelota = new Pelota(equipoB, 5);
        }
    }

    /**
     * Inicia la simulación del partido.
     * El partido tiene una duración de 60 segundos reales, con jugadas cada 2 segundos.
     *
     * @throws InterruptedException Si el hilo de ejecución es interrumpido durante la espera.
     */
    public void jugar() throws InterruptedException {
        long inicio = System.currentTimeMillis();
        long duracion = 60000; // 60 segundos en milisegundos

        while (System.currentTimeMillis() - inicio < duracion) {
            ejecutarTurno();
            // Pausa de 2 segundos entre cada acción para dar realismo a la narración
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

    /**
     * Ejecuta la lógica de una jugada individual.
     * Identifica al atacante, al defensor y determina qué acción ocurre en el campo.
     */
    private void ejecutarTurno() {
        Equipo atacante = pelota.getEquipoActual();
        Equipo defensor = (atacante == equipoA) ? equipoB : equipoA;

        int indice = pelota.getIndiceJugador();

        // El jugador con el balón se enfrenta al rival en la posición espejo del campo
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

    /**
     * Decide qué acción realizará el jugador basándose en su proximidad al área rival.
     *
     * @param indice Posición actual del balón (0-10).
     * @return La acción seleccionada (PASE, REGATE o TIRO).
     */
    private Accion elegirAccion(int indice) {
        // Si está en zona de ataque (índice 7 o superior), puede tirar a puerta
        if (indice >= 7) {
            return Accion.values()[random.nextInt(3)];
        }
        // En zona defensiva o medio campo, solo se permiten pases o regates
        return (random.nextInt(2) == 0) ? Accion.PASE : Accion.REGATE;
    }

    /**
     * Aplica un factor de aleatoriedad a una estadística base.
     * El valor resultante oscila entre el 90% y el 110% de la capacidad del jugador.
     *
     * @param valor Atributo base del jugador.
     * @return El valor modificado por el azar.
     */
    private int modificar(int valor) {
        double variacion = 0.9 + (random.nextDouble() * 0.2);
        return (int) (valor * variacion);
    }

    /**
     * Simula un intento de pase.
     * Si el pase supera la defensa del rival, el balón avanza un índice.
     */
    private void hacerPase(Jugador atacante, Jugador rival) {
        int pase = modificar(atacante.getPase());
        int defensa = modificar(rival.getDefensa());

        if (pase > defensa) {
            pelota.setIndiceJugador(pelota.getIndiceJugador() + 1);
            narrador.narrar(atacante + " hace un pase correcto");
        } else {
            cambiarPosesion();
            narrador.narrar(rival + " roba el balón");
        }
    }

    /**
     * Simula un intento de regate.
     * Al igual que el pase, si tiene éxito, el balón avanza hacia la portería rival.
     */
    private void hacerRegate(Jugador atacante, Jugador rival) {
        int regate = modificar(atacante.getRegate());
        int defensa = modificar(rival.getDefensa());

        if (regate > defensa) {
            pelota.setIndiceJugador(pelota.getIndiceJugador() + 1);
            narrador.narrar(atacante + " supera al rival");
        } else {
            cambiarPosesion();
            narrador.narrar(rival + " recupera el balón");
        }
    }

    /**
     * Simula un tiro a puerta.
     * Calcula la potencia según la distancia y la compara con la habilidad del portero.
     *
     * @param atacante El jugador que realiza el tiro.
     * @param defensor El equipo que intenta evitar el gol.
     */
    private void hacerTiro(Jugador atacante, Equipo defensor) {
        int indice = pelota.getIndiceJugador();

        // Factor de distancia: reduce la efectividad del tiro si se tira desde lejos
        double factor = 1 - ((10 - indice) * 0.10);
        int tiro = (int) (modificar(atacante.getTiro()) * factor);

        // El portero es siempre el jugador en el índice 0 del equipo defensor
        Jugador portero = defensor.getJugadores()[0];
        int parada = modificar(portero.getPortero());

        if (tiro > parada) {
            pelota.getEquipoActual().marcarGol();
            narrador.narrar("GOOOOOOL de " + atacante.getNombre());
        } else {
            narrador.narrar("PARADÓN DEL PORTERO contra " + atacante.getNombre());
        }

        // Tras un tiro (sea gol o no), se reinicia el juego desde el centro
        reiniciarCentro();
    }

    /**
     * Alterna la posesión del balón entre los dos equipos y lo sitúa en el centro.
     */
    private void cambiarPosesion() {
        if (pelota.getEquipoActual() == equipoA) {
            pelota.setEquipoActual(equipoB);
        } else {
            pelota.setEquipoActual(equipoA);
        }
        pelota.setIndiceJugador(5); // Vuelve al medio campo
    }

    /**
     * Reinicia el juego tras una interrupción (como un gol o una parada).
     */
    private void reiniciarCentro() {
        cambiarPosesion();
    }
}