# Data Model: Self-Study Mode (研学模式)

**Date**: 2026-02-13

## Entities

### ForbiddenHandConfig

Represents the user's selection of which forbidden hand rules to apply in a game session.

| Field | Type | Description |
|-------|------|-------------|
| ttnhEnabled | Boolean | 3-3 forbidden hand rule enabled (default: true) |
| ffnhEnabled | Boolean | 4-4 forbidden hand rule enabled (default: true) |
| olnhEnabled | Boolean | Overline forbidden hand rule enabled (default: true) |

**Notes**: This is a simple data carrier passed from the rule selection UI to `GameFactory.createGame()`. All defaults are `true` per spec (FR-002).

### Game (extended)

The existing `Game` interface is extended with self-study mode capabilities.

| Method | Signature | Description |
|--------|-----------|-------------|
| retractLast() | `fun retractLast()` | Retracts only the last stone, works in any game state |

**State transitions after `retractLast()`**:
- `BLACK_WON` / `WHITE_WON` / `DRAW` → `PLAYING` (undo restores play)
- `PLAYING` → `PLAYING` (normal undo during play)

### GameSituation (existing, behavior change)

| Field | Change |
|-------|--------|
| retractionAvailable | For self-study mode: `true` when `dropCount >= 1` (was `>= 2`) |

### HistoryOperator (extended)

| Method | Signature | Description |
|--------|-----------|-------------|
| retractLast() | `fun retractLast(): DropRecord?` | Removes and returns only the last record |

## UI View Entities

### RuleSelectionOverlayView

Modal overlay for selecting forbidden hand rules before starting a game.

| Component | Type | Description |
|-----------|------|-------------|
| ttnhToggle | Toggle label | "3-3 禁手" checkbox, default selected |
| ffnhToggle | Toggle label | "4-4 禁手" checkbox, default selected |
| olnhToggle | Toggle label | "长连禁手" checkbox, default selected |
| startButton | SimpleTextButtonView | "开始" button — confirms and starts game |
| cancelButton | SimpleTextButtonView | "取消" button — closes overlay |

**Events**: `onStartClicked(config: ForbiddenHandConfig)`, `onCancelClicked()`

### SidePanelView

Right-side information panel displayed during gameplay.

| Component | Type | Description |
|-----------|------|-------------|
| roundActorLabel | LabelView | Shows "黑方回合" or "白方回合" |
| dropCountLabel | LabelView | Shows current drop count |
| statusLabel | LabelView | Shows victory/draw message, forbidden move hint |
| retractButton | SimpleTextButtonView | "悔棋" button, disabled when no stones |
| returnButton | SimpleTextButtonView | "返回" button, always enabled |

**Events**: `onRetractClicked()`, `onReturnClicked()`

## Animation Entities

### EnterGameAnimation

Orchestrates the transition from title screen to game screen.

| Animator | View | From | To | Duration |
|----------|------|------|----|----------|
| boardAnimator | BoardIntegrationView | center (current) | left-aligned (x=0) | 500ms |
| titlePanelAnimator | TitlePanelView | center (current) | off-screen left | 500ms |
| selfStudyBtnAnimator | selfStudyButtonView | center (current) | off-screen left | 500ms |
| challengeAiBtnAnimator | challengeAiButtonView | center (current) | off-screen left | 500ms |
| dualBtnAnimator | dualButtonView | center (current) | off-screen left | 500ms |
| sidePanelAnimator | SidePanelView | off-screen right | right of board (x=boardWidth) | 500ms |

### ExitGameAnimation

Orchestrates the transition from game screen back to title screen. Reverse of EnterGameAnimation.

| Animator | View | From | To | Duration |
|----------|------|------|----|----------|
| sidePanelAnimator | SidePanelView | right of board | off-screen right | 500ms |
| boardAnimator | BoardIntegrationView | left-aligned | center | 500ms |
| titlePanelAnimator | TitlePanelView | off-screen left | center | 500ms |
| selfStudyBtnAnimator | selfStudyButtonView | off-screen left | center | 500ms |
| challengeAiBtnAnimator | challengeAiButtonView | off-screen left | center | 500ms |
| dualBtnAnimator | dualButtonView | off-screen left | center | 500ms |

## Relationships

```
MainWindow
├── owns Game (created on "开始", destroyed on "返回")
├── owns RuleSelectionOverlayView (shown on "研学模式" click)
├── owns SidePanelView (shown during gameplay)
├── owns EnterGameAnimation (played on game start)
├── owns ExitGameAnimation (played on return to title)
└── implements BoardSensorViewEventListener (handles cell clicks)

Game
├── uses Board (stone placement/removal)
├── uses History (move recording/undo)
└── uses Rule (legality checking, configured by ForbiddenHandConfig)
```
