package edu.eci.dosw.bowling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests TDD para {@link BowlingGame}.
 *
 * <p>Metodología: RED → GREEN → REFACTOR<br>
 * Cada test representa un caso del dominio del juego de Bowling.</p>
 */
@DisplayName("BowlingGame – Motor de juego")
class BowlingGameTest {

    private BowlingGame game;

    @BeforeEach
    void setUp() {
        game = new BowlingGame();
    }

    // ── Utilidades internas ───────────────────────────────────────────────────

    /** Lanza {@code times} tiros de {@code pins} pines cada uno. */
    private void rollMany(int times, int pins) {
        for (int i = 0; i < times; i++) {
            game.roll(pins);
        }
    }

    /** Lanza un spare: {@code first} pines + el complemento. */
    private void rollSpare(int first) {
        game.roll(first);
        game.roll(10 - first);
    }

    // ── Tests principales ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Gutter game: todos los tiros en 0 → score = 0")
    void testAllGutterBalls() {
        rollMany(20, 0);
        assertEquals(0, game.score());
    }

    @Test
    @DisplayName("All ones: todos los tiros en 1 → score = 20")
    void testAllOnes() {
        rollMany(20, 1);
        assertEquals(20, game.score());
    }

    @Test
    @DisplayName("Un spare seguido de 3 → score = 16")
    void testOneSpare() {
        rollSpare(5);          // frame 1: spare (5+5)
        game.roll(3);          // bono del spare → frame 2: 3
        rollMany(17, 0);       // resto cero
        // Frame 1: 10 + 3 = 13  |  Frame 2: 3  |  Total = 16
        assertEquals(16, game.score());
    }

    @Test
    @DisplayName("Un strike seguido de 3 y 4 → score = 24")
    void testOneStrike() {
        game.roll(10);         // frame 1: strike
        game.roll(3);
        game.roll(4);
        rollMany(16, 0);
        // Frame 1: 10 + 3 + 4 = 17  |  Frame 2: 7  |  Total = 24
        assertEquals(24, game.score());
    }

    @Test
    @DisplayName("Juego perfecto: 12 strikes → score = 300")
    void testPerfectGame() {
        rollMany(12, 10);
        assertEquals(300, game.score());
    }

    @Test
    @DisplayName("Spare en el décimo frame → permite un tiro extra")
    void testTenthFrameSpare() {
        rollMany(18, 0);       // frames 1–9: todos cero
        rollSpare(7);          // décimo: spare (7+3)
        game.roll(5);          // tiro de bonus
        // Décimo frame: 7 + 3 + 5 = 15
        assertEquals(15, game.score());
    }

    @Test
    @DisplayName("Strike en el décimo frame → permite dos tiros extra")
    void testTenthFrameStrike() {
        rollMany(18, 0);       // frames 1–9: todos cero
        game.roll(10);         // strike en décimo
        game.roll(7);
        game.roll(3);
        // Décimo frame: 10 + 7 + 3 = 20
        assertEquals(20, game.score());
    }

    @Test
    @DisplayName("Spare seguido de strike → combinación de bonos")
    void testSpareThenStrike() {
        rollSpare(5);          // frame 1: spare (5+5)
        game.roll(10);         // frame 2: strike (bono del spare)
        rollMany(16, 0);
        // Frame 1: 10 + 10 = 20  |  Frame 2: 10  |  Total = 30
        assertEquals(30, game.score());
    }

    @Test
    @DisplayName("Dos strikes consecutivos → bono correcto")
    void testTwoConsecutiveStrikes() {
        game.roll(10);         // frame 1: strike
        game.roll(10);         // frame 2: strike
        game.roll(5);
        game.roll(3);
        rollMany(14, 0);
        // Frame 1: 10+10+5=25  |  Frame 2: 10+5+3=18  |  Frame 3: 8  |  Total = 51
        assertEquals(51, game.score());
    }

    @Test
    @DisplayName("Spare en último frame con strike de bonus → décimo = 20")
    void testLastFrameSpareWithStrikeBonus() {
        rollMany(18, 0);
        game.roll(5);
        game.roll(5);   // spare
        game.roll(10);  // strike de bonus
        assertEquals(20, game.score());
    }

    // ── Tests de estado del juego ─────────────────────────────────────────────

    @Test
    @DisplayName("isComplete() retorna false al inicio y true al completar la partida")
    void testIsComplete() {
        assertFalse(game.isComplete(), "El juego no debería haber terminado");
        rollMany(20, 0);
        assertTrue(game.isComplete(), "El juego debería haber terminado");
    }

    @Test
    @DisplayName("roll() lanza IllegalStateException si el juego ya terminó")
    void testRollAfterGameOver() {
        rollMany(20, 0);
        assertThrows(IllegalStateException.class, () -> game.roll(5));
    }

    @Test
    @DisplayName("roll() lanza IllegalArgumentException con pines negativos")
    void testRollNegativePins() {
        assertThrows(IllegalArgumentException.class, () -> game.roll(-1));
    }

    @Test
    @DisplayName("roll() lanza IllegalArgumentException con pines > 10")
    void testRollTooManyPins() {
        assertThrows(IllegalArgumentException.class, () -> game.roll(11));
    }

    @Test
    @DisplayName("score() lanza IllegalStateException si el juego no está completo")
    void testScoreBeforeGameComplete() {
        game.roll(5);
        assertThrows(IllegalStateException.class, () -> game.score());
    }

    @Test
    @DisplayName("getFrames() devuelve 10 elementos tras una partida completa")
    void testGetFramesReturnsTenFrames() {
        rollMany(20, 0);
        assertEquals(10, game.getFrames().size());
    }

    @Test
    @DisplayName("getFrames() devuelve lista inmutable")
    void testGetFramesIsImmutable() {
        rollMany(20, 0);
        List<Frame> frames = game.getFrames();
        assertThrows(UnsupportedOperationException.class, () -> frames.add(new Frame(false)));
    }

    // ── Tests parametrizados ──────────────────────────────────────────────────

    @ParameterizedTest(name = "Todos los tiros en {0} pines → score = {1}")
    @CsvSource({"0, 0", "1, 20", "2, 40", "3, 60", "4, 80"})
    @DisplayName("Tests parametrizados: todos los tiros con N pines (sin spare)")
    void testAllRollsWithNPins(int pins, int expectedScore) {
        rollMany(20, pins);
        assertEquals(expectedScore, game.score());
    }
}
