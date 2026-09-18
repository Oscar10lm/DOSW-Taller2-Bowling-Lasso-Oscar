package edu.eci.dosw.bowling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests TDD para {@link BowlingGame} — Secciones A y C.
 * Metodología: RED → GREEN → REFACTOR
 */
@DisplayName("BowlingGame – Motor de juego")
class BowlingGameTest {

    private BowlingGame game;

    @BeforeEach
    void setUp() {
        game = new BowlingGame();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void rollMany(int times, int pins) {
        for (int i = 0; i < times; i++) game.roll(pins);
    }
}
