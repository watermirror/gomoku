# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Tips

You always communicate with the user in Chinese, but you always use English in code comments.

## Project Overview

Gomoku (五子棋) is a JavaFX desktop application implementing the traditional strategy board game with Chinese competition rules (3-3 禁手, 4-4 禁手, 长连禁手). Built with Kotlin 1.6 on JVM, using Maven and OpenJFX.

- **Main class**: `org.bymc.gomoku.ApplicationKt`
- **Package namespace**: `org.bymc.gomoku.*`

## Build and Development Commands

**Build**:
```bash
mvn clean compile
```

**Run all tests**:
```bash
mvn test
```

**Run a specific test class**:
```bash
mvn test -Dtest=RuleImplTest
```

**Run a specific test method**:
```bash
mvn test -Dtest=RuleImplTest#methodName
```

**Run the GUI application**:
```bash
mvn clean javafx:run
```

## Architecture

The codebase has three major layers:

### Model Layer (`model/`)
Core game data and rules, separated by interface (`abstraction/`) and implementation (`impl/`):
- **Board / Cell**: 15x15 board state management
- **Rule**: Move validation and game state determination (win/draw/forbidden moves)
- **History**: Move history with undo support

Rule verification is handled by specialized verifiers in `model/common/util/`:
- `GomokuVerifier` — five-in-a-row detection
- `TtnhVerifier` — 3-3 forbidden hand rule
- `FfnhVerifier` — 4-4 forbidden hand rule
- `OlnhVerifier` — overline (长连) forbidden hand rule
- `DirectedPatternCounter` — pattern counting along the 4 polar axes (N-S, E-W, NE-SW, SE-NW)

### Game Layer (`game/`)
Turn-based game flow built on top of Model. `GameImpl` manages actor turns, move validation, and game situation tracking. Instantiated via `GameFactory`.

### UI Layer (`ui/` and `uifx/`)
- `uifx/` — Custom UI framework on top of JavaFX/AWT: view hierarchy, event system, animation engine with easing functions, hit testing, canvas rendering
- `ui/` — Application-specific views: `MainWindow`, `BoardIntegrationView` (board rendering), `BoardSensorView` (input), `TitlePanelView`, `CoveringLayerView`

## Testing

- **Framework**: JUnit 5 + Mockito-Kotlin
- **Test location**: `src/test/kotlin/` with `*Test.kt` naming
- **Helper**: `TestBoardViewModel` for setting up board states in tests

## Commit Message Convention

Follow Conventional Commits style:
- `feat:` for new features
- `fix:` for bug fixes
- `refactor:` for refactoring
- `test:` for test changes
- `docs:` for documentation updates

**Important**: Do not include any AI/Claude attribution in commit messages (no "Generated with Claude", "Co-Authored-By: Claude", etc.)

## Git Push Rules

- **Never push without explicit request**: Do not run `git push` unless the user explicitly asks to push.
- **Never force push without explicit request**: When the user asks to push, use normal push only. Never use `--force` or `-f` unless the user explicitly requests a force push.

## Active Technologies
- Kotlin 1.6 / JVM + JavaFX (OpenJFX), AWT/Swing (for animation timers and Canvas) (001-self-study-mode)
- N/A (in-memory game state only) (001-self-study-mode)

## Recent Changes
- 001-self-study-mode: Added Kotlin 1.6 / JVM + JavaFX (OpenJFX), AWT/Swing (for animation timers and Canvas)
