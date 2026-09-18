package edu.eci.dosw.bowling;

import java.util.ArrayList;
import java.util.List;

/**
 * Motor de un juego de Bowling para un jugador.
 * Un juego tiene exactamente 10 frames.
 */
public class BowlingGame {

    private final List<Frame> frames;
    private int currentFrame;

    public BowlingGame() {
        this.frames = new ArrayList<>();
        this.currentFrame = 0;
    }

    /** Registra pinos derribados. Lanza IllegalArgumentException si pines < 0 o > 10.
     *  Lanza IllegalStateException si el juego ya termino. */
    public void roll(int pins) {
        if (pins < 0) {
            throw new IllegalArgumentException("Pines negativos: " + pins);
        }
        if (frames.isEmpty() || frames.get(frames.size() - 1).isComplete()) {
            frames.add(new Frame(frames.size() == 9));
        }
        frames.get(frames.size() - 1).addRoll(pins);
    }

    /** Puntaje total. Lanza IllegalStateException si el juego no esta completo. */
    public int score() {
        return 0;
    }

    /** true cuando los 10 frames han sido completados. */
    public boolean isComplete() {
        return false;
    }

    public List<Frame> getFrames() { return List.copyOf(frames); }
}
