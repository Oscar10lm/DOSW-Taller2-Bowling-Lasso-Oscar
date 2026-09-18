package edu.eci.dosw.bowling;

/**
 * Representa un frame (cuadro) dentro de una partida de Bowling.
 * Un frame regular contiene hasta 2 tiros.
 * El décimo frame puede contener hasta 3 tiros si hay strike o spare.
 */
public class Frame {

    private static final int MAX_ROLLS_REGULAR = 2;
    private static final int MAX_ROLLS_TENTH = 3;
    private static final int TOTAL_PINS = 10;

    private final int[] rolls;
    private int rollCount;
    private FrameType type;
    private final boolean isTenth;

    /** Crea un frame regular (frames 1 al 9). */
    public Frame() {
        this(false);
    }

    /** Crea un frame indicando si es el décimo. */
    public Frame(boolean isTenth) {
        this.isTenth = isTenth;
        int maxRolls = isTenth ? MAX_ROLLS_TENTH : MAX_ROLLS_REGULAR;
        this.rolls = new int[maxRolls];
        this.rollCount = 0;
        this.type = isTenth ? FrameType.TENTH : FrameType.NORMAL;
    }

    /** Registra un tiro en este frame. Solo valida completitud del frame. */
    public void addRoll(int pins) {
        if (isComplete()) {
            throw new IllegalStateException("El frame ya está completo.");
        }
        rolls[rollCount++] = pins;
        updateType();
    }

    private void updateType() {
        if (isTenth) {
            type = FrameType.TENTH;
            return;
        }
        if (rollCount == 1 && rolls[0] == TOTAL_PINS) {
            type = FrameType.STRIKE;
        } else if (rollCount == 2 && (rolls[0] + rolls[1]) == TOTAL_PINS) {
            type = FrameType.SPARE;
        } else if (rollCount >= 2) {
            type = FrameType.NORMAL;
        }
    }

    /** Determina si el frame está completo. */
    public boolean isComplete() {
        if (isTenth) {
            return isTenthComplete();
        }
        return type == FrameType.STRIKE || rollCount >= MAX_ROLLS_REGULAR;
    }

    private boolean isTenthComplete() {
        if (rollCount < 2) return false;
        if (rolls[0] != TOTAL_PINS && (rolls[0] + rolls[1]) < TOTAL_PINS) {
            return true;
        }
        return rollCount >= MAX_ROLLS_TENTH;
    }

    public int getScore() {
        int score = 0;
        for (int i = 0; i < rollCount; i++) score += rolls[i];
        return score;
    }

    public int getFirstRoll()  { return rollCount > 0 ? rolls[0] : 0; }
    public int getSecondRoll() { return rollCount > 1 ? rolls[1] : 0; }
    public int getThirdRoll()  { return (isTenth && rollCount > 2) ? rolls[2] : 0; }
    public int getRollCount()  { return rollCount; }
    public FrameType getType() { return type; }
    public boolean isTenth()   { return isTenth; }

    @Override
    public String toString() {
        return String.format("Frame{type=%s, rolls=%d, score=%d}", type, rollCount, getScore());
    }
}
