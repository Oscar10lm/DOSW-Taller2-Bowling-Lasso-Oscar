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

    @Test
    @DisplayName("A3: roll(11) — valor mayor a 10 lanza IllegalArgumentException")
    void rollMoreThanTenPins_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> game.roll(11));
    }

    @Test
    @DisplayName("A4: Dos tiros suman > 10 — lanza IllegalArgumentException")
    void rollTwoRollsSumOverTen_throwsException() {
        game.roll(7);
        assertThrows(IllegalArgumentException.class, () -> game.roll(6));
    }

    @Test
    @DisplayName("A5: roll() cuando el juego ya esta completo lanza IllegalStateException")
    void rollWhenGameIsComplete_throwsException() {
        rollMany(20, 0);
        assertThrows(IllegalStateException.class, () -> game.roll(0));
    }

    @Test
    @DisplayName("A6: Frame regular se completa con strike (un solo tiro)")
    void rollStrike_completesFrame() {
        game.roll(10);
        game.roll(5);
        assertEquals(2, game.getFrames().size(), "Debe haber 2 frames");
        assertEquals(1, game.getFrames().get(0).getRollCount(), "Frame 1 tiene 1 tiro (strike)");
    }

    @Test
    @DisplayName("A7: Frame regular se completa con 2 tiros")
    void rollTwoTimes_completesFrame() {
        game.roll(3);
        game.roll(4);
        game.roll(1); // Deberia ir al segundo frame
        assertEquals(2, game.getFrames().size(), "Debe haber 2 frames");
        assertEquals(2, game.getFrames().get(0).getRollCount(), "Frame 1 tiene 2 tiros");
        assertEquals(1, game.getFrames().get(1).getRollCount(), "Frame 2 tiene 1 tiro");
    }

    @Test
    @DisplayName("A8: Décimo frame admite hasta 3 tiros si hay strike/spare")
    void tenthFrame_allowsThreeRollsWithStrikeOrSpare() {
        rollMany(18, 0); // Llega al décimo frame

        // Caso spare
        game.roll(5);
        game.roll(5); // Spare
        assertFalse(game.isComplete(), "Juego no termina con spare en el 10mo frame");
        
        game.roll(1); // 3er tiro
        assertTrue(game.isComplete(), "Juego termina despues del 3er tiro del 10mo frame");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // #C  BowlingGame — isComplete() · estado del juego
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("C1: isComplete() al inicio del juego retorna false")
    void isComplete_atStart_returnsFalse() {
        assertFalse(game.isComplete());
    }

    @Test
    @DisplayName("C2: isComplete() después de 9 frames → false")
    void isComplete_afterNineFrames_returnsFalse() {
        rollMany(18, 0);
        assertFalse(game.isComplete());
    }

    @Test
    @DisplayName("C3: isComplete() después de 10 frames normales → true")
    void isComplete_afterTenNormalFrames_returnsTrue() {
        rollMany(20, 0);
        assertTrue(game.isComplete());
    }

    @Test
    @DisplayName("C4: Spare en frame 10 + bono → isComplete() true")
    void isComplete_spareInTenthPlusBonus_returnsTrue() {
        rollMany(18, 0);
        game.roll(5);
        game.roll(5); // spare
        game.roll(3); // bono
        assertTrue(game.isComplete());
    }

    @Test
    @DisplayName("C5: Strike en frame 10 + 2 bonos → isComplete() true")
    void isComplete_strikeInTenthPlusTwoBonuses_returnsTrue() {
        rollMany(18, 0);
        game.roll(10); // strike
        game.roll(3);
        game.roll(2);
        assertTrue(game.isComplete());
    }

    @Test
    @DisplayName("C6: Juego perfecto (12 strikes) → isComplete() true")
    void isComplete_perfectGame_returnsTrue() {
        for (int i = 0; i < 12; i++) game.roll(10);
        assertTrue(game.isComplete());
    }
}
