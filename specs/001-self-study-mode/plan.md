# Implementation Plan: 研学模式 (Self-Study Mode)

**Branch**: `001-self-study-mode` | **Date**: 2026-02-13 | **Spec**: [spec.md](spec.md)
**Input**: Feature specification from `/specs/001-self-study-mode/spec.md`

## Summary

Implement a self-study game mode where a single player plays both black and white sides on a 15x15 Gomoku board. The feature includes: configurable forbidden hand rules (3-3, 4-4, overline) via a modal overlay, smooth 500ms slide animations for scene transitions, a right-side information panel with game status and action buttons, single-stone undo that works in any game state (including after win/draw), and forbidden move visual feedback (red flash + text hint).

## Technical Context

**Language/Version**: Kotlin 1.6 / JVM
**Primary Dependencies**: JavaFX (OpenJFX), AWT/Swing (for animation timers and Canvas)
**Storage**: N/A (in-memory game state only)
**Testing**: JUnit 5 + Mockito-Kotlin
**Target Platform**: Desktop (macOS/Linux/Windows) via JavaFX
**Project Type**: Single Maven project
**Performance Goals**: 60fps animations, <16ms per frame during transitions
**Constraints**: All animations 500ms per spec; game state changes must be immediate
**Scale/Scope**: Single-player local mode; ~10 new/modified files

## Constitution Check

*GATE: No constitution.md found — no gate constraints to enforce.*

**Post-design re-check**: N/A (no constitution)

## Project Structure

### Documentation (this feature)

```text
specs/001-self-study-mode/
├── plan.md              # This file
├── research.md          # Phase 0 output — technical decisions
├── data-model.md        # Phase 1 output — entity and view models
├── quickstart.md        # Phase 1 output — implementation guide
└── tasks.md             # Phase 2 output (created by /speckit.tasks)
```

### Source Code (repository root)

```text
src/main/kotlin/org/bymc/gomoku/
├── game/
│   ├── abstraction/Game.kt              # [MODIFY] Add retractLast()
│   ├── impl/GameImpl.kt                 # [MODIFY] Implement retractLast()
│   └── factory/GameFactory.kt           # [NO CHANGE] Already supports forbidden hand config
├── model/
│   ├── abstraction/HistoryOperator.kt   # [MODIFY] Add retractLast()
│   └── impl/HistoryImpl.kt             # [MODIFY] Implement retractLast()
├── ui/
│   ├── animation/
│   │   ├── TitleAnimation.kt           # [NO CHANGE] Reference pattern
│   │   ├── EnterGameAnimation.kt       # [NEW] Title→Game transition
│   │   └── ExitGameAnimation.kt        # [NEW] Game→Title transition
│   ├── view/
│   │   ├── BoardIntegrationView.kt     # [MODIFY] Add flash cell support
│   │   ├── BoardGridView.kt            # [MODIFY] Add red flash rendering
│   │   ├── RuleSelectionOverlayView.kt # [NEW] Forbidden hand rule selector
│   │   ├── SidePanelView.kt            # [NEW] Right-side game info panel
│   │   └── SidePanelViewEventListener.kt # [NEW] Side panel event interface
│   └── window/
│       ├── MainWindow.kt               # [MODIFY] Wire self-study mode logic
│       ├── UiConfig.kt                 # [MODIFY] Add side panel & animation config
│       └── DefaultUiConfig.kt          # [MODIFY] Add default values
└── uifx/                               # [NO CHANGE] Framework layer untouched

src/test/kotlin/org/bymc/gomoku/
├── game/impl/GameImplTest.kt           # [MODIFY] Add retractLast() tests
└── model/impl/HistoryImplTest.kt       # [MODIFY] Add retractLast() tests
```

**Structure Decision**: Single Maven project, existing directory structure. New files follow established patterns — views in `ui/view/`, animations in `ui/animation/`, tests mirror source paths.

## Design Decisions

### D-001: Single-Stone Undo via retractLast()

Add `retractLast()` to `Game` and `HistoryOperator` interfaces rather than modifying existing `retract(actor)`. This preserves backward compatibility while providing the simple "undo last move" semantics needed for self-study mode.

**Key behaviors**:
- Works in any `GameState` (PLAYING, BLACK_WON, WHITE_WON, DRAW)
- Removes exactly one stone (the last placed)
- Swaps round actor back to the previous player
- Removes stone from board, then calls `rule.judgeGameState(board)` to re-judge the actual game state (typically restores to PLAYING, but handles edge cases correctly)
- `retractionAvailable = dropCount >= 1`

### D-002: Animation Architecture

Two separate classes (`EnterGameAnimation`, `ExitGameAnimation`) following `TitleAnimation` pattern:
- Each manages 6 parallel `ViewAreaAnimator` instances
- Duration: 500ms, ease function: `EaseInOut` (no overshoot)
- `CoveringLayerView` shown during animation, hidden after all animators finish
- Animators use `baseArea = null` (current position) for flexibility

### D-003: Rule Selection Overlay

`RuleSelectionOverlayView` extends `ViewBase`:
- Centered on screen, white background, black border
- Three toggle rows (label-based, click to toggle) for forbidden hand rules
- "开始" and "取消" buttons
- All rules default to selected
- Dispatches events via listener interface

### D-004: Side Panel

`SidePanelView` extends `ViewBase`:
- Width: 300px (matches `UiConfig.getControlPanelWidth()`)
- Height: full window height (615px)
- Contains: round actor label, drop count label, status label, retract button, return button
- Updates via setter methods called from `MainWindow`

### D-005: Forbidden Move Feedback

Dual feedback on forbidden move attempt:
1. **Visual**: `BoardGridView` renders a semi-transparent red overlay on the target cell, cleared after ~300ms via a timer
2. **Text**: `SidePanelView` status label shows "禁手位置，无法落子", auto-cleared on next successful move

### D-006: MainWindow as Controller

`MainWindow` becomes the central controller for self-study mode:
- Implements `ButtonViewEventListener` (already) — handles "研学模式" button click
- Implements `BoardSensorViewEventListener` — handles cell clicks
- Manages game lifecycle: creation (on "开始"), destruction (on "返回")
- Coordinates animations, view visibility, and game state updates

## Implementation Phases

### Phase 1: Model Layer Extensions
- Add `retractLast()` to `HistoryOperator` + `HistoryImpl`
- Add `retractLast()` to `Game` + `GameImpl`
- Unit tests for both

### Phase 2: UI Config Extensions
- Add animation duration/config to `UiConfig` + `DefaultUiConfig`
- Add side panel layout config

### Phase 3: New UI Views
- `RuleSelectionOverlayView` with toggle and button logic
- `SidePanelView` with labels and buttons
- `SidePanelViewEventListener` interface

### Phase 4: Animations
- `EnterGameAnimation` (title → game)
- `ExitGameAnimation` (game → title)

### Phase 5: Board Feedback
- Add red flash cell support to `BoardGridView`/`BoardIntegrationView`

### Phase 6: MainWindow Integration
- Wire button handlers, game lifecycle, cell click handling
- Connect all views, animations, and game logic
- End-to-end manual testing

## Complexity Tracking

No constitution violations to track.
