package edu.eci.dosw.bowling;

import java.util.ArrayList;
import java.util.List;

/**
 * Motor de un juego de Bowling para un jugador.
 * Un juego tiene exactamente 10 frames.
 *
 * <p>Flujo TDD (RED → GREEN → REFACTOR):
 * <ol>
 *   <li>Escribir el test que falla (RED).</li>
 *   <li>Implementar el mínimo código que lo haga pasar (GREEN).</li>
 *   <li>Limpiar/mejorar sin romper tests (REFACTOR).</li>
 * </ol>
 * </p>
 */
public class BowlingGame {

    /** Número total de frames en una partida. */
    private static final int TOTAL_FRAMES = 10;

    /** Total de pines en una pista estándar. */
    private static final int TOTAL_PINS = 10;

    private final List<Frame> frames;
    private int currentFrame;

    public BowlingGame() {
        this.frames = new ArrayList<>();
        this.currentFrame = 0;
    }

    /**
     * Registra pinos derribados.
     *
     * @param pins número de pines (0–10).
     * @throws IllegalArgumentException si {@code pins < 0} o {@code pins > 10}.
     * @throws IllegalStateException    si el juego ya terminó.
     */
    public void roll(int pins) {
        if (pins < 0 || pins > TOTAL_PINS) {
            throw new IllegalArgumentException(
                    "Número de pines inválido: " + pins + ". Debe estar entre 0 y 10.");
        }
        if (isComplete()) {
            throw new IllegalStateException("El juego ya ha terminado.");
        }

        // Si no hay frames, o el último está completo y aún caben más frames, crear uno nuevo
        if (frames.isEmpty()
                || (frames.get(frames.size() - 1).isComplete() && frames.size() < TOTAL_FRAMES)) {
            boolean isTenth = (frames.size() == TOTAL_FRAMES - 1);
            frames.add(new Frame(isTenth));
        }

        frames.get(frames.size() - 1).addRoll(pins);
    }

    /**
     * Calcula y devuelve el puntaje total de la partida.
     *
     * @return puntaje total (0–300).
     * @throws IllegalStateException si el juego aún no está completo.
     */
    public int score() {
        if (!isComplete()) {
            throw new IllegalStateException("El juego aún no ha terminado.");
        }
        Frame[] frameArray = frames.toArray(new Frame[0]);
        return new BowlingScorer().calculateScore(frameArray);
    }

    /**
     * Indica si la partida ha concluido (los 10 frames están completos).
     *
     * @return {@code true} cuando los 10 frames han sido completados.
     */
    public boolean isComplete() {
        return frames.size() == TOTAL_FRAMES
                && frames.get(TOTAL_FRAMES - 1).isComplete();
    }

    /**
     * Devuelve una copia inmutable de la lista de frames registrados.
     *
     * @return lista de {@link Frame}.
     */
    public List<Frame> getFrames() {
        return List.copyOf(frames);
    }
}
