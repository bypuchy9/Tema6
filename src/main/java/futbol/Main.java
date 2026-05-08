package futbol;

public class Main {

    public static void main(String[] args)
            throws InterruptedException {

        Jugador[] jugadoresA = new Jugador[11];

        Jugador[] jugadoresB = new Jugador[11];

        for (int i = 0; i < 11; i++) {

            jugadoresA[i] =
                    GeneradorJugadores.crearJugador(i);

            jugadoresB[i] =
                    GeneradorJugadores.crearJugador(i);
        }

        Equipo equipoA =
                new Equipo("Barcelona", jugadoresA);

        Equipo equipoB =
                new Equipo("Madrid", jugadoresB);

        Partido partido =
                new Partido(equipoA, equipoB);

        partido.jugar();
    }
}