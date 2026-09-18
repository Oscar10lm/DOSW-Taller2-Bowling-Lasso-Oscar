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


    private void rollMany(int times, int pins) {
        for (int i = 0; i < times; i++) game.roll(pins);
    }

    @Test
    @DisplayName("A1: roll(0) — primer tiro a cero no lanza excepción y registra frame")
    void rollZeroPins_doesNotThrowAndRegistersFrame() {
        // Arrange & Act
        assertDoesNotThrow(() -> game.roll(0));
        // Assert — debe existir un frame con 0 pinos
        assertFalse(game.getFrames().isEmpty(), "Debe haber al menos un frame");
        assertEquals(0, game.getFrames().get(0).getFirstRoll());
    }

    @Test
    @DisplayName("A2: roll(-1) — valor negativo lanza IllegalArgumentException")
    void rollNegativePins_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> game.roll(-1));
    }
}
