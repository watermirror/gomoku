# Research: Self-Study Mode (研学模式)

**Date**: 2026-02-13

## R-001: Game Layer Modification for Self-Study Mode Retraction

**Decision**: Modify `GameImpl` to support retraction after win/draw and with only 1 stone on the board.

**Rationale**: The current `GameImpl.retract()` has two constraints incompatible with the spec:
1. It throws if `state != PLAYING` — but the spec requires undo after win/draw (FR-014)
2. `isRetractionAvailable()` requires `recordCount >= 2` — but the spec says undo should work as long as there's at least 1 stone (User Story 3, Scenario 3)

The retraction logic in `HistoryImpl.retract(actor)` finds the last record of the given actor's color and removes everything from the end back to that record. For self-study mode, we want simpler behavior: just remove the last single stone regardless of color.

**Approach**:
- Add a new method to `Game` interface: `retractLast()` — retracts only the last single stone (not back to the current actor's previous move)
- `retractLast()` should work in any game state (PLAYING, BLACK_WON, WHITE_WON, DRAW)
- After removing the stone, calls `rule.judgeGameState(board)` to re-evaluate the actual game state (rather than hardcoding PLAYING)
- `retractionAvailable` should be `true` when `dropCount >= 1` (not >= 2)

**Alternatives considered**:
- Modify existing `retract()` behavior: Rejected — would break the existing contract and semantics
- Create a wrapper/decorator around `Game`: Rejected — adds unnecessary indirection; better to extend the interface

## R-002: Animation System for Scene Transitions

**Decision**: Create two new animation orchestrators: `EnterGameAnimation` and `ExitGameAnimation`, following the same pattern as `TitleAnimation`.

**Rationale**: The existing `TitleAnimation` demonstrates the exact pattern needed — multiple parallel `ViewAreaAnimator` instances coordinated by a single class. The self-study mode needs:
- **Enter**: Board slides from center to left, title panel + buttons slide out left (off-screen), right side panel slides in from right. Duration: 500ms.
- **Exit**: Reverse — right panel slides out right, board slides from left back to center, title + buttons slide in from left. Duration: 500ms.

**Approach**:
- Use `EaseInOut` (not `EaseInOutBack`) for a smoother, more professional transition without overshoot
- All animators share the same 500ms duration with no stagger/delay (simpler than the title animation)
- Both animation classes implement `AnimationEventListener` and manage `CoveringLayerView` showing/hiding
- Board target position (left-aligned): `x = 0, y = 0`
- Board center position: `x = (windowWidth - boardWidth) / 2, y = 0`
- Right panel: slides from `x = windowWidth` to `x = boardWidth`

**Alternatives considered**:
- Single bidirectional animation class: Rejected — two separate classes are clearer and avoid conditional logic
- Reuse `TitleAnimation` with reverse: Rejected — `TitleAnimation` has different timing and targets

## R-003: Forbidden Hand Rule Selection UI

**Decision**: Create a modal overlay view (`RuleSelectionOverlayView`) that appears on top of the title screen.

**Rationale**: The spec says the overlay is modal, uses simple show/hide (no slide animation), and matches existing visual style (white background, black border). It needs three checkboxes and two buttons.

**Approach**:
- The overlay is a `ViewBase` subclass with white background and black border
- Contains three toggle labels (acting as checkboxes) for: 3-3 禁手, 4-4 禁手, 长连禁手
- Contains "开始" and "取消" buttons (`SimpleTextButtonView`)
- Default state: all three rules selected
- The overlay is added to the root view tree and shown/hidden as needed
- Toggle state tracked as three boolean properties
- Visual toggle: use `[✓]` / `[  ]` prefix in label text, or background color change

**Alternatives considered**:
- Custom checkbox component: Rejected — over-engineering; toggling label text/color is sufficient for 3 options
- Separate window/dialog: Rejected — spec says overlay on the same window

## R-004: Right Side Panel Design

**Decision**: Create a `SidePanelView` as a `ViewBase` subclass with labels and buttons.

**Rationale**: The right side panel (spec FR-011, FR-012) shows:
- Current round actor (黑方/白方 回合)
- Drop count
- Victory/draw message (when applicable)
- "悔棋" button (disabled when no stones)
- "返回" button

**Approach**:
- Panel width: `UiConfig.getControlPanelWidth()` = 300px
- Panel height: same as board (full height)
- Position when visible: `x = boardWidth, y = 0`
- Contains `LabelView` instances for status text and `SimpleTextButtonView` for buttons
- Exposes update methods: `updateRoundActor(Stone)`, `updateDropCount(Int)`, `showVictory(Stone)`, `showDraw()`, `setRetractionEnabled(Boolean)`, `showForbiddenMessage()`
- White background with left black border

**Alternatives considered**:
- Reuse TitlePanelView: Rejected — completely different content and layout

## R-005: Forbidden Move Visual Feedback

**Decision**: Implement a brief red flash on the cell where the forbidden move was attempted.

**Rationale**: The spec (FR-018) requires two feedback mechanisms:
1. The cell position flashes red briefly
2. The right side panel shows "禁手位置，无法落子" text

**Approach**:
- For the red flash: use a `ViewAreaAnimator` (or a simple timer-based approach) on a semi-transparent red overlay placed at the cell position, lasting ~300ms
- Alternatively, modify `BoardGridView` to support a "flash cell" mode that draws a red semi-transparent overlay on a specific cell and clears it after a short delay
- The side panel message is simpler — just call `showForbiddenMessage()` which sets the label text (auto-clears after a few seconds or on next move)

**Alternatives considered**:
- Shake animation on the cell: Rejected — spec specifically says "flash red"
- Permanent red marker: Rejected — spec says "brief flash" (短暂闪烁)

## R-006: History Model for Single-Step Undo

**Decision**: Add `retractLast()` method to `HistoryOperator` that removes only the last record.

**Rationale**: The current `retract(actor)` removes all records back to the given actor's last move, potentially removing multiple records. For self-study mode, the spec says "undo the last move" which is always exactly one stone.

**Approach**:
- Add `retractLast(): DropRecord?` to `HistoryOperator` interface
- Implementation simply removes and returns the last record from the linked list
- This avoids modifying the existing `retract(actor)` semantics

**Alternatives considered**:
- Calling `retract(lastStone)` where `lastStone` is the color of the last move: This would work in most cases but is semantically wrong — `retract(actor)` is designed around the concept of "the current actor undoes their last move"
