# 🎳 Bowling TDD – DOSW 2026-2

> **Taller #1 · Corte 2 · TDD & Cobertura**
> Construcción del motor de puntuación de Bowling con TDD, JUnit 5, JaCoCo y SonarQube.

---

## 📋 Descripción

BowlingTech S.A.S. requiere digitalizar el sistema de puntuación de sus pistas de bolos.
Este proyecto implementa el motor de puntuación aplicando **Test-Driven Development (TDD)** desde cero.

---

## 🗂️ Estructura del proyecto

```
bowling-tdd/
├── pom.xml
├── README.md
└── src/
    ├── main/java/edu/eci/dosw/bowling/
    │   ├── BowlingGame.java      ← Motor principal del juego
    │   ├── Frame.java            ← Representa un frame con sus tiros
    │   ├── FrameType.java        ← Enum: NORMAL, SPARE, STRIKE, TENTH
    │   └── BowlingScorer.java    ← Calcula el puntaje total
    └── test/java/edu/eci/dosw/bowling/
        ├── BowlingGameTest.java
        └── BowlingScorerTest.java
```

---

## 🎯 Reglas del dominio

| Situación | Regla de puntuación |
|-----------|---------------------|
| **Normal** | Suma de los 2 tiros del frame |
| **Spare** | 10 + primer tiro del siguiente frame |
| **Strike** | 10 + suma de los 2 tiros siguientes |
| **10mo frame (spare)** | Permite 1 tiro de bonus |
| **10mo frame (strike)** | Permite 2 tiros de bonus |
| **Juego perfecto** | 12 strikes = 300 puntos |

---

## 🏗️ Clases de dominio

### `FrameType` (enum)
Representa el estado de un frame:
- `NORMAL` – Frame regular sin bonificación
- `SPARE` – Todos los pines en 2 tiros
- `STRIKE` – Todos los pines en el primer tiro
- `TENTH` – Décimo frame especial

### `Frame`
Representa un frame con sus tiros. Gestiona la lógica de completitud y detecta automáticamente el tipo (NORMAL, SPARE o STRIKE).

### `BowlingGame`
Motor principal que orquesta los 10 frames, recibe tiros mediante `roll(int)` y delega el cálculo a `BowlingScorer`.

### `BowlingScorer`
Clase *stateless* que implementa las reglas de puntuación. Recibe el array de frames y calcula la puntuación total con bonos.

---

## 🔧 Stack tecnológico

| Herramienta | Versión | Uso |
|-------------|---------|-----|
| Java | 17 | Lenguaje |
| Maven | 3.x | Build |
| JUnit Jupiter | 5.10.3 | Tests TDD |
| JaCoCo | 0.8.12 | Cobertura de código |
| SonarQube plugin | 3.11.0 | Análisis estático |

---

## ▶️ Ejecución

### Compilar y ejecutar tests
```bash
mvn test
```

### Generar reporte de cobertura JaCoCo
```bash
mvn verify
# Reporte en: target/site/jacoco/index.html
```

### Análisis SonarQube (requiere servidor configurado)
```bash
mvn sonar:sonar -Dsonar.host.url=http://localhost:9000 -Dsonar.login=<TOKEN>
```

---

## ✅ Tests implementados

### `BowlingGameTest`
| Test | Escenario |
|------|-----------|
| `testAllGutterBalls` | 20 tiros en 0 → score = 0 |
| `testAllOnes` | 20 tiros en 1 → score = 20 |
| `testOneSpare` | Spare + 3 → score = 16 |
| `testOneStrike` | Strike + 3,4 → score = 24 |
| `testPerfectGame` | 12 strikes → score = 300 |
| `testTenthFrameSpare` | Spare en 10mo + bonus = 15 |
| `testTenthFrameStrike` | Strike en 10mo + 7,3 = 20 |
| `testSpareThenStrike` | Spare seguido de strike = 30 |
| `testTwoConsecutiveStrikes` | Dos strikes seguidos = 51 |
| `testIsGameOver` | Estado del juego |
| `testRollAfterGameOver` | Excepción si juego terminado |
| `testGetFramesReturnsTenFrames` | Array de 10 frames |
| `testLastFrameSpareWithStrikeBonus` | Spare + strike en 10mo = 20 |
| `testAllRollsWithNPins` | Tests parametrizados 0–4 |

### `BowlingScorerTest`
| Test | Escenario |
|------|-----------|
| `testScoreAllZeros` | Todos cero → 0 |
| `testScoreAllSpares` | Todos spares → 150 |
| `testScoreAllStrikes` | Juego perfecto → 300 |
| `testMixedGame` | Juego mixto → 49 |
| `testTenthFrameWithSpareAndStrikeBonus` | Décimo spare + strike = 20 |
| `testTenthFrameStrikeWithBonus` | Décimo strike + 7,3 = 20 |
| `testTwoConsecutiveStrikesScoring` | Dos strikes → 51 |
| `testCalculateScoreWithNull` | IllegalArgumentException con null |
| `testCalculateScoreWithWrongArraySize` | IllegalArgumentException tamaño inválido |
| `testSpareInNinthFrameUsesFirstRollOfTenth` | Spare frame 9 usa tiro 1 del 10mo |

---

## 🌿 Flujo Git

```
origin/develop
       └── feature/LassoOscar-bowling  ← rama de trabajo
                    │
                    └──(Pull Request)──▶ develop
```

> ⚠️ Los cambios a `develop` llegan **únicamente** mediante Pull Request. No se permiten commits directos a `develop`.

---

*Desarrollado por: Oscar Lasso – DOSW 2026-2*