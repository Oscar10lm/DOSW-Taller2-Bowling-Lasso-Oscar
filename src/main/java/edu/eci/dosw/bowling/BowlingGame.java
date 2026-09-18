package edu.eci.dosw.bowling;

/**
 * Motor principal del juego de Bowling.
 *
 * <p>Gestiona los 10 frames de una partida y delega el cálculo de la
 * puntuación a {@link BowlingScorer}. El uso esperado es:</p>
 *
 * <pre>{@code
 * BowlingGame game = new BowlingGame();
 * game.roll(10);          // strike
 * game.roll(3);
 * game.roll(6);
 * int score = game.score(); // 10 + 3 + 6 + 3 + 6 = 28
 * }</pre>
 *
 * <p>El juego acepta exactamente 10 frames. El décimo frame permite tiros
 * adicionales según las reglas de Bowling.</p>
 */
public class BowlingGame {

    /** Número total de frames en una partida de Bowling. */
    private static final int TOTAL_FRAMES = 10;

    /** Índice base cero del décimo frame. */
    private static final int TENTH_FRAME_INDEX = 9;

    /** Los 10 frames de la partida. */
    private final Frame[] frames;

    /** Índice del frame actual (0-based). */
    private int currentFrameIndex;

    /** Calculador de puntuación. */
    private final BowlingScorer scorer;

    /**
     * Construye un nuevo juego de Bowling con los 10 frames inicializados.
     */
    public BowlingGame() {
        frames = new Frame[TOTAL_FRAMES];
        for (int i = 0; i < TOTAL_FRAMES - 1; i++) {
            frames[i] = new Frame(false);
        }
        frames[TENTH_FRAME_INDEX] = new Frame(true);
        currentFrameIndex = 0;
        scorer = new BowlingScorer();
    }

    /**
     * Registra un tiro (lanzamiento de bola).
     *
     * @param pins número de pines derribados (0–10).
     * @throws IllegalStateException si el juego ya terminó.
     */
    public void roll(int pins) {
        if (isGameOver()) {
            throw new IllegalStateException("El juego ya ha terminado.");
        }

        frames[currentFrameIndex].addRoll(pins);

        // Avanzar al siguiente frame si el actual está completo
        if (frames[currentFrameIndex].isComplete()
                && currentFrameIndex < TENTH_FRAME_INDEX) {
            currentFrameIndex++;
        }
    }

    /**
     * Calcula y devuelve la puntuación total de la partida.
     *
     * @return puntuación total incluyendo bonos de strike y spare.
     */
    public int score() {
        return scorer.calculateScore(frames);
    }

    /**
     * Indica si la partida ha concluido (el décimo frame está completo).
     *
     * @return {@code true} si el juego terminó.
     */
    public boolean isGameOver() {
        return frames[TENTH_FRAME_INDEX].isComplete();
    }

    /**
     * Devuelve la copia del array de frames para consulta.
     *
     * @return array de 10 {@link Frame}.
     */
    public Frame[] getFrames() {
        return frames.clone();
    }

    /**
     * Devuelve el índice del frame actual (0-based).
     *
     * @return índice del frame en curso.
     */
    public int getCurrentFrameIndex() {
        return currentFrameIndex;
    }
}
