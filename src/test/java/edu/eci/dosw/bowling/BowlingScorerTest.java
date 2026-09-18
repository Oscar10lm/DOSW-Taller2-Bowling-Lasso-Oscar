package edu.eci.dosw.bowling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests TDD para {@link BowlingScorer}.
 *
 * <p>Verifica directamente la lógica de cálculo de puntuación
 * construyendo arrays de {@link Frame} de forma programática.</p>
 */
@DisplayName("BowlingScorer – Calculador de puntuación")
class BowlingScorerTest {

    private BowlingScorer scorer;

    @BeforeEach
    void setUp() {
        scorer = new BowlingScorer();
    }

    // ── Utilidades internas ───────────────────────────────────────────────────

    /**
     * Crea un array de 10 frames con todos los tiros en 0 (gutter).
     */
    private Frame[] buildGutterFrames() {
        Frame[] frames = new Frame[10];
        for (int i = 0; i < 9; i++) {
            frames[i] = new Frame(false);
            frames[i].addRoll(0);
            frames[i].addRoll(0);
        }
        frames[9] = new Frame(true);
        frames[9].addRoll(0);
        frames[9].addRoll(0);
        return frames;
    }

    /**
     * Crea un frame regular con dos tiros.
     */
    private Frame normalFrame(int first, int second) {
        Frame f = new Frame(false);
        f.addRoll(first);
        f.addRoll(second);
        return f;
    }

    /**
     * Crea un frame de strike.
     */
    private Frame strikeFrame() {
        Frame f = new Frame(false);
        f.addRoll(10);
        return f;
    }

    /**
     * Crea un spare frame (first + (10 - first)).
     */
    private Frame spareFrame(int first) {
        Frame f = new Frame(false);
        f.addRoll(first);
        f.addRoll(10 - first);
        return f;
    }

    /**
     * Crea el décimo frame con los tiros indicados.
     */
    private Frame tenthFrame(int... rolls) {
        Frame f = new Frame(true);
        for (int r : rolls) {
            f.addRoll(r);
        }
        return f;
    }

    // ── Tests ─────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Todos los frames en 0 → score = 0")
    void testScoreAllZeros() {
        Frame[] frames = buildGutterFrames();
        assertEquals(0, scorer.calculateScore(frames));
    }

    @Test
    @DisplayName("Todos los frames con spare (5+5) y último tiro 5 → score = 150")
    void testScoreAllSpares() {
        Frame[] frames = new Frame[10];
        for (int i = 0; i < 9; i++) {
            frames[i] = spareFrame(5);
        }
        frames[9] = tenthFrame(5, 5, 5);
        // Cada frame 1–9: 10 + 5 = 15 → 9 × 15 = 135 | Frame 10: 5+5+5=15 | Total = 150
        assertEquals(150, scorer.calculateScore(frames));
    }

    @Test
    @DisplayName("Juego perfecto: 12 strikes → score = 300")
    void testScoreAllStrikes() {
        Frame[] frames = new Frame[10];
        for (int i = 0; i < 9; i++) {
            frames[i] = strikeFrame();
        }
        frames[9] = tenthFrame(10, 10, 10);
        assertEquals(300, scorer.calculateScore(frames));
    }

    @Test
    @DisplayName("Juego mixto predecible → score correcto")
    void testMixedGame() {
        Frame[] frames = new Frame[10];
        // Frame 1: strike (10)
        frames[0] = strikeFrame();
        // Frame 2: spare 7+3 (10)
        frames[1] = spareFrame(7);
        // Frame 3: normal 5+4 (9)
        frames[2] = normalFrame(5, 4);
        // Frames 4–9: gutter (0)
        for (int i = 3; i < 9; i++) {
            frames[i] = normalFrame(0, 0);
        }
        // Frame 10: normal 3+2 (5)
        frames[9] = tenthFrame(3, 2);

        // Frame 1: 10 + 7 + 3 = 20
        // Frame 2: 10 + 5 = 15
        // Frame 3: 9
        // Frames 4–9: 0
        // Frame 10: 5
        // Total = 20 + 15 + 9 + 5 = 49
        assertEquals(49, scorer.calculateScore(frames));
    }

    @Test
    @DisplayName("Un spare en el décimo frame con bonus strike → décimo = 20")
    void testTenthFrameWithSpareAndStrikeBonus() {
        Frame[] frames = new Frame[10];
        for (int i = 0; i < 9; i++) {
            frames[i] = normalFrame(0, 0);
        }
        frames[9] = tenthFrame(5, 5, 10);
        assertEquals(20, scorer.calculateScore(frames));
    }

    @Test
    @DisplayName("Un strike en el décimo frame con bonus 7+3 → décimo = 20")
    void testTenthFrameStrikeWithBonus() {
        Frame[] frames = new Frame[10];
        for (int i = 0; i < 9; i++) {
            frames[i] = normalFrame(0, 0);
        }
        frames[9] = tenthFrame(10, 7, 3);
        assertEquals(20, scorer.calculateScore(frames));
    }

    @Test
    @DisplayName("Dos strikes consecutivos en frames 1 y 2 → bono correcto")
    void testTwoConsecutiveStrikesScoring() {
        Frame[] frames = new Frame[10];
        frames[0] = strikeFrame();            // strike
        frames[1] = strikeFrame();            // strike
        frames[2] = normalFrame(5, 3);        // normal
        for (int i = 3; i < 9; i++) {
            frames[i] = normalFrame(0, 0);
        }
        frames[9] = tenthFrame(0, 0);

        // Frame 1: 10 + 10 + 5 = 25
        // Frame 2: 10 + 5 + 3 = 18
        // Frame 3: 8
        // Total = 51
        assertEquals(51, scorer.calculateScore(frames));
    }

    @Test
    @DisplayName("calculateScore() lanza IllegalArgumentException con null")
    void testCalculateScoreWithNull() {
        assertThrows(IllegalArgumentException.class, () -> scorer.calculateScore(null));
    }

    @Test
    @DisplayName("calculateScore() lanza IllegalArgumentException con array de tamaño incorrecto")
    void testCalculateScoreWithWrongArraySize() {
        Frame[] frames = new Frame[5];
        assertThrows(IllegalArgumentException.class, () -> scorer.calculateScore(frames));
    }

    @Test
    @DisplayName("Frame con spare en posición 9 (índice 8) → bono usa primer tiro del décimo")
    void testSpareInNinthFrameUsesFirstRollOfTenth() {
        Frame[] frames = new Frame[10];
        for (int i = 0; i < 8; i++) {
            frames[i] = normalFrame(0, 0);
        }
        frames[8] = spareFrame(6);            // spare en frame 9
        frames[9] = tenthFrame(8, 1);         // décimo: primer tiro = 8

        // Frame 9: 10 + 8 = 18 | Frame 10: 8 + 1 = 9 | Total = 27
        assertEquals(27, scorer.calculateScore(frames));
    }
}
