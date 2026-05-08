package futbol;

import java.util.Random;

public class GeneradorJugadores {

    private static Random random = new Random();

    public static Jugador crearJugador(int dorsal) {

        return new Jugador(
                dorsal,
                "Jugador" + dorsal,
                random.nextInt(101),
                random.nextInt(101),
                random.nextInt(101),
                random.nextInt(101),
                random.nextInt(101)
        );
    }
}