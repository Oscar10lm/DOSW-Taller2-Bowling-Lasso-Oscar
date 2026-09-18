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
        if (pins < 0 || pins > 10) {
            throw new IllegalArgumentException("Pines fuera de rango: " + pins);
        }
        if (isComplete()) {
            throw new IllegalStateException("El juego ya ha terminado.");
        }
        if (!frames.isEmpty()) {
            Frame lastFrame = frames.get(frames.size() - 1);
            if (!lastFrame.isComplete() && !lastFrame.isTenth()) {
                if (lastFrame.getFirstRoll() + pins > 10) {
                    throw new IllegalArgumentException("La suma excede 10");
                }
            }
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
        return frames.size() == 10 && frames.get(9).isComplete();
    }

    public List<Frame> getFrames() { return List.copyOf(frames); }
}
