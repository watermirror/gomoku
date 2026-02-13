# Quickstart: Self-Study Mode (研学模式)

**Date**: 2026-02-13

## Overview

The self-study mode allows a single player to play both black and white sides on a Gomoku board, with configurable forbidden hand rules, full undo support (including after game end), and smooth slide animations for scene transitions.

## Key Technical Decisions

1. **Extend `Game`/`GameImpl` rather than create a wrapper** — add `retractLast()` for single-stone undo that works in any game state
2. **Extend `HistoryOperator`/`HistoryImpl`** — add `retractLast()` for removing only the last record
3. **Two separate animation classes** (`EnterGameAnimation`, `ExitGameAnimation`) following `TitleAnimation` pattern
4. **Rule selection as modal overlay** — simple `ViewBase` subclass with toggle labels and buttons
5. **Side panel as new view** — `SidePanelView` with labels and action buttons

## Implementation Order

1. **Model layer** (no UI dependency):
   - `HistoryOperator.retractLast()` + `HistoryImpl` implementation
   - `Game.retractLast()` + `GameImpl` implementation
   - Unit tests for both

2. **New UI views** (no wiring yet):
   - `RuleSelectionOverlayView`
   - `SidePanelView`

3. **Animations**:
   - `EnterGameAnimation`
   - `ExitGameAnimation`

4. **Forbidden move feedback**:
   - Red flash on `BoardGridView`

5. **MainWindow integration** (wires everything together):
   - Button click handlers
   - Game lifecycle management
   - `BoardSensorViewEventListener` implementation

## Build & Test

```bash
# Build
mvn clean compile

# Run all tests
mvn test

# Run specific test
mvn test -Dtest=GameImplTest

# Run GUI
mvn clean javafx:run
```

## Layout Coordinates Reference

- Window client size: `boardWidth + 300` x `boardHeight` (where boardWidth = boardHeight = 15 * 41 = 615)
- Board center position: `x = (915 - 615) / 2 = 150, y = 0`
- Board left-aligned position: `x = 0, y = 0`
- Side panel position (visible): `x = 615, y = 0, w = 300, h = 615`
- Side panel position (off-screen): `x = 915, y = 0, w = 300, h = 615`
- Title panel, buttons: slide to `x = -(their width)` when exiting left
