package edu.eci.dosw.bowling;

/**
 * Calcula la puntuación total de una partida de Bowling.
 * Clase stateless: recibe datos y devuelve un número.
 */
public class BowlingScorer {

    /** Calcula el puntaje total a partir de los frames de la partida. */
    public int calculateScore(Frame[] frames) {
        int total = 0;
        for (int i = 0; i < frames.length; i++) {
            Frame frame = frames[i];
            if (frame.isTenth()) {
                total += frame.getScore();
            } else if (frame.getType() == FrameType.STRIKE) {
                total += 10 + strikeBonus(frames, i);
            } else if (frame.getType() == FrameType.SPARE) {
                total += 10 + spareBonus(frames, i);
            } else {
                total += frame.getScore();
            }
        }
        return total;
    }

    private int strikeBonus(Frame[] frames, int index) {
        int bonus = 0;
        if (index + 1 < frames.length) {
            Frame next = frames[index + 1];
            bonus += next.getFirstRoll();
            if (next.getType() == FrameType.STRIKE && !next.isTenth()) {
                // El siguiente también es strike → segundo bono viene del frame +2
                if (index + 2 < frames.length) {
                    bonus += frames[index + 2].getFirstRoll();
                }
            } else {
                bonus += next.getSecondRoll();
            }
        }
        return bonus;
    }

    private int spareBonus(Frame[] frames, int index) {
        if (index + 1 < frames.length) {
            return frames[index + 1].getFirstRoll();
        }
        return 0;
    }
}
