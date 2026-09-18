package edu.eci.dosw.bowling;

/**
 * Representa un frame (cuadro) dentro de una partida de Bowling.
 *
 * <p>Un frame regular contiene hasta 2 tiros. El décimo frame ({@link FrameType#TENTH})
 * puede contener hasta 3 tiros cuando el jugador logra un strike o spare.</p>
 *
 * <p>Reglas de completitud:
 * <ul>
 *   <li>Strike (frames 1–9): completo con 1 tiro.</li>
 *   <li>Normal/Spare (frames 1–9): completo con 2 tiros.</li>
 *   <li>Décimo frame: completo con 2 tiros si el primero no es strike y no hay spare;
 *       3 tiros si hay strike o spare.</li>
 * </ul>
 * </p>
 */
public class Frame {

    /** Máximo de tiros en un frame regular. */
    private static final int MAX_ROLLS_REGULAR = 2;

    /** Máximo de tiros en el décimo frame. */
    private static final int MAX_ROLLS_TENTH = 3;

    /** Total de pines en una pista estándar. */
    private static final int TOTAL_PINS = 10;

    /** Pines derribados en cada tiro. */
    private final int[] rolls;

    /** Número de tiros registrados en este frame. */
    private int rollCount;

    /** Tipo del frame (se actualiza automáticamente al registrar tiros). */
    private FrameType type;

    /** Indica si este es el décimo frame. */
    private final boolean isTenth;

    /**
     * Crea un frame regular (frames 1 al 9).
     */
    public Frame() {
        this(false);
    }

    /**
     * Crea un frame indicando si es el décimo.
     *
     * @param isTenth {@code true} para el décimo frame, {@code false} para los demás.
     */
    public Frame(boolean isTenth) {
        this.isTenth = isTenth;
        int maxRolls = isTenth ? MAX_ROLLS_TENTH : MAX_ROLLS_REGULAR;
        this.rolls = new int[maxRolls];
        this.rollCount = 0;
        this.type = isTenth ? FrameType.TENTH : FrameType.NORMAL;
    }

    /**
     * Registra un tiro en este frame.
     *
     * @param pins número de pines derribados (0–10).
     * @throws IllegalStateException  si el frame ya está completo.
     * @throws IllegalArgumentException si el número de pines es inválido.
     */
    public void addRoll(int pins) {
        if (isComplete()) {
            throw new IllegalStateException("El frame ya está completo.");
        }
        if (pins < 0 || pins > TOTAL_PINS) {
            throw new IllegalArgumentException("Número de pines inválido: " + pins);
        }
        if (!isTenth && rollCount == 1 && (rolls[0] + pins) > TOTAL_PINS) {
            throw new IllegalArgumentException(
                    "La suma de pines en un frame no puede superar " + TOTAL_PINS);
        }

        rolls[rollCount++] = pins;
        updateType();
    }

    /**
     * Actualiza el tipo del frame según los tiros registrados.
     */
    private void updateType() {
        if (isTenth) {
            type = FrameType.TENTH;
            return;
        }
        if (rollCount == 1 && rolls[0] == TOTAL_PINS) {
            type = FrameType.STRIKE;
        } else if (rollCount == 2 && (rolls[0] + rolls[1]) == TOTAL_PINS) {
            type = FrameType.SPARE;
        } else {
            type = FrameType.NORMAL;
        }
    }

    /**
     * Determina si el frame está completo (no se pueden registrar más tiros).
     *
     * @return {@code true} si el frame está completo.
     */
    public boolean isComplete() {
        if (isTenth) {
            return isTenthComplete();
        }
        return type == FrameType.STRIKE || rollCount >= MAX_ROLLS_REGULAR;
    }

    /**
     * Lógica de completitud para el décimo frame.
     *
     * @return {@code true} si el décimo frame está completo.
     */
    private boolean isTenthComplete() {
        if (rollCount < 2) return false;
        // Si el primer tiro no es strike y los dos primeros no forman spare → 2 tiros
        if (rolls[0] != TOTAL_PINS && (rolls[0] + rolls[1]) < TOTAL_PINS) {
            return true;
        }
        // Si hay strike o spare → necesitan 3 tiros
        return rollCount >= MAX_ROLLS_TENTH;
    }

    /**
     * Devuelve la puntuación base del frame (sin bonos).
     *
     * @return suma de los pines derribados en este frame.
     */
    public int getScore() {
        int score = 0;
        for (int i = 0; i < rollCount; i++) {
            score += rolls[i];
        }
        return score;
    }

    /**
     * Devuelve el primer tiro del frame.
     *
     * @return pines del primer tiro, o 0 si aún no se ha lanzado.
     */
    public int getFirstRoll() {
        return rollCount > 0 ? rolls[0] : 0;
    }

    /**
     * Devuelve el segundo tiro del frame.
     *
     * @return pines del segundo tiro, o 0 si aún no se ha lanzado.
     */
    public int getSecondRoll() {
        return rollCount > 1 ? rolls[1] : 0;
    }

    /**
     * Devuelve el tercer tiro del frame (solo disponible en el décimo frame).
     *
     * @return pines del tercer tiro, o 0 si no aplica / no se ha lanzado.
     */
    public int getThirdRoll() {
        return (isTenth && rollCount > 2) ? rolls[2] : 0;
    }

    /**
     * Devuelve el número de tiros registrados.
     *
     * @return cantidad de tiros en este frame.
     */
    public int getRollCount() {
        return rollCount;
    }

    /**
     * Devuelve el tipo actual del frame.
     *
     * @return {@link FrameType} de este frame.
     */
    public FrameType getType() {
        return type;
    }

    /**
     * Indica si este es el décimo frame.
     *
     * @return {@code true} si es el décimo frame.
     */
    public boolean isTenth() {
        return isTenth;
    }

    @Override
    public String toString() {
        return String.format("Frame{type=%s, rolls=%d tiros, score=%d}", type, rollCount, getScore());
    }
}
