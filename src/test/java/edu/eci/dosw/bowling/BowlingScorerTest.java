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
}
