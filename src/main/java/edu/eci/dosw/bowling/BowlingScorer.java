package edu.eci.dosw.bowling;

/**
 * Calcula la puntuación total de una partida de Bowling.
 *
 * <p>Implementa las reglas oficiales de puntuación:</p>
 * <ul>
 *   <li><b>Normal:</b>  suma de pines de los 2 tiros.</li>
 *   <li><b>Spare:</b>   10 + primer tiro del siguiente frame.</li>
 *   <li><b>Strike:</b>  10 + suma de los 2 tiros siguientes.</li>
 *   <li><b>Décimo frame:</b> suma de todos los tiros del frame (sin bono externo).</li>
 * </ul>
 *
 * <p>Esta clase es <em>stateless</em>: puede reutilizarse para múltiples partidas.</p>
 */
public class BowlingScorer {

    /** Total de pines en una pista. */
    private static final int TOTAL_PINS = 10;

    /** Número total de frames de una partida. */
    private static final int TOTAL_FRAMES = 10;

    /** Índice base cero del décimo frame. */
    private static final int TENTH_FRAME_INDEX = 9;

    /**
     * Calcula la puntuación total a partir de los 10 frames de la partida.
     *
     * @param frames array de 10 {@link Frame} que representa la partida.
     * @return puntuación total (0–300).
     * @throws IllegalArgumentException si el array es {@code null} o no tiene 10 frames.
     */
    public int calculateScore(Frame[] frames) {
        if (frames == null || frames.length != TOTAL_FRAMES) {
            throw new IllegalArgumentException(
                    "Se requieren exactamente " + TOTAL_FRAMES + " frames.");
        }

        int totalScore = 0;

        for (int i = 0; i < TOTAL_FRAMES; i++) {
            Frame current = frames[i];

            if (current == null) {
                continue;
            }

            if (i == TENTH_FRAME_INDEX) {
                // El décimo frame suma sus tiros directamente (sin bono externo)
                totalScore += current.getScore();
            } else if (current.getType() == FrameType.STRIKE) {
                totalScore += TOTAL_PINS + getBonusForStrike(frames, i);
            } else if (current.getType() == FrameType.SPARE) {
                totalScore += TOTAL_PINS + getBonusForSpare(frames, i);
            } else {
                // NORMAL
                totalScore += current.getScore();
            }
        }

        return totalScore;
    }

    /**
     * Calcula el bono de un strike: suma de los 2 tiros siguientes.
     *
     * @param frames array de frames.
     * @param strikeIndex índice del frame donde ocurrió el strike.
     * @return valor del bono (0–20).
     */
    private int getBonusForStrike(Frame[] frames, int strikeIndex) {
        int nextIndex = strikeIndex + 1;
        if (nextIndex >= TOTAL_FRAMES || frames[nextIndex] == null) {
            return 0;
        }

        Frame next = frames[nextIndex];

        if (next.isTenth()) {
            // Bono: primer + segundo tiro del décimo frame
            return next.getFirstRoll() + next.getSecondRoll();
        }

        if (next.getType() == FrameType.STRIKE) {
            // Strike consecutivo: 10 + primer tiro del frame siguiente al siguiente
            int afterNext = nextIndex + 1;
            if (afterNext >= TOTAL_FRAMES || frames[afterNext] == null) {
                return TOTAL_PINS;
            }
            return TOTAL_PINS + frames[afterNext].getFirstRoll();
        }

        // Frame normal o spare
        return next.getFirstRoll() + next.getSecondRoll();
    }

    /**
     * Calcula el bono de un spare: primer tiro del siguiente frame.
     *
     * @param frames array de frames.
     * @param spareIndex índice del frame donde ocurrió el spare.
     * @return valor del bono (0–10).
     */
    private int getBonusForSpare(Frame[] frames, int spareIndex) {
        int nextIndex = spareIndex + 1;
        if (nextIndex >= TOTAL_FRAMES || frames[nextIndex] == null) {
            return 0;
        }
        return frames[nextIndex].getFirstRoll();
    }
}
