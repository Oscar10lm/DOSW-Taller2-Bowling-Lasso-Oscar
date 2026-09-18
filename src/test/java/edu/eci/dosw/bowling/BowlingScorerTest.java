package edu.eci.dosw.bowling;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests TDD para {@link BowlingScorer} — Sección B.
 * Metodología: RED → GREEN → REFACTOR
 */
@DisplayName("BowlingScorer – Calculador de puntuación")
class BowlingScorerTest {

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void rollMany(BowlingGame game, int times, int pins) {
        for (int i = 0; i < times; i++) game.roll(pins);
    }

    private void rollPerfectGame(BowlingGame game) {
        for (int i = 0; i < 12; i++) game.roll(10);
    }

    private void rollAllSpares(BowlingGame game, int lastBonus) {
        for (int i = 0; i < 10; i++) { game.roll(5); game.roll(5); }
        game.roll(lastBonus);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // #B  BowlingScorer — calculateScore · puntuación
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("B1: Juego con todos los tiros a 0 → score() == 0")
    void allZeros_scoreIsZero() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 20, 0);
        assertEquals(0, game.score());
    }

    @Test
    @DisplayName("B2: Sin strikes ni spares → suma directa")
    void noStrikesNoSpares_directSum() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 20, 1);
        assertEquals(20, game.score());
    }

    @Test
    @DisplayName("B3: Un spare + siguiente tiro 3 → 13 + frames restantes")
    void oneSpare_bonusApplied() {
        BowlingGame game = new BowlingGame();
        game.roll(5);
        game.roll(5); // spare
        game.roll(3); // bonus
        rollMany(game, 17, 0);
        assertEquals(16, game.score());
    }

    @Test
    @DisplayName("B4: Un strike + 4 + 3 → 17 + frames restantes")
    void oneStrike_bonusApplied() {
        BowlingGame game = new BowlingGame();
        game.roll(10); // strike
        game.roll(4);
        game.roll(3);
        rollMany(game, 16, 0);
        assertEquals(24, game.score());
    }

    @Test
    @DisplayName("B5: Dos strikes consecutivos + 5 + 3")
    void twoConsecutiveStrikes_bonusCorrect() {
        BowlingGame game = new BowlingGame();
        game.roll(10); // strike 1
        game.roll(10); // strike 2
        game.roll(5);
        game.roll(3);
        rollMany(game, 14, 0);
        assertEquals(51, game.score());
    }

    @Test
    @DisplayName("B6: Todos spares con 5+5 y bonus final 5 → 150")
    void allSpares_score150() {
        BowlingGame game = new BowlingGame();
        rollAllSpares(game, 5);
        assertEquals(150, game.score());
    }

    @Test
    @DisplayName("B7: Juego perfecto (12 strikes) → 300")
    void perfectGame_score300() {
        BowlingGame game = new BowlingGame();
        rollPerfectGame(game);
        assertEquals(300, game.score());
    }

    @Test
    @DisplayName("B8: score() antes de completar el juego → IllegalStateException")
    void scoreBeforeComplete_throwsException() {
        BowlingGame game = new BowlingGame();
        game.roll(5);
        assertThrows(IllegalStateException.class, game::score);
    }
}
