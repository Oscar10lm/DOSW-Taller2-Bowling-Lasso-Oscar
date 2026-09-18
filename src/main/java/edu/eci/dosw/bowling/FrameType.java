package edu.eci.dosw.bowling;

/**
 * Enum que representa los tipos posibles de un frame en el juego de Bowling.
 *
 * <ul>
 *   <li>{@link #NORMAL}  – Frame regular: 2 tiros, ningún strike ni spare.</li>
 *   <li>{@link #SPARE}   – Todos los pines derriban en 2 tiros; bono = primer tiro del siguiente frame.</li>
 *   <li>{@link #STRIKE}  – Todos los pines derriban en el primer tiro; bono = suma de los 2 tiros siguientes.</li>
 *   <li>{@link #TENTH}   – Décimo frame especial: permite hasta 3 tiros si hay strike o spare.</li>
 * </ul>
 */
public enum FrameType {

    /** Frame regular sin bono. */
    NORMAL,

    /** Spare: pines derribados en 2 tiros; 10 + siguiente tiro. */
    SPARE,

    /** Strike: pines derribados en el primer tiro; 10 + 2 tiros siguientes. */
    STRIKE,

    /** Décimo frame: manejo especial de 2 ó 3 tiros de bonus. */
    TENTH
}
