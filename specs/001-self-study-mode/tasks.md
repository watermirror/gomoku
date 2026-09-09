# Tasks: 研学模式 (Self-Study Mode)

**Input**: Design documents from `/specs/001-self-study-mode/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, quickstart.md

**Tests**: Included — CLAUDE.md requires unit tests for code changes.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

---

## Phase 1: Setup (UI Config Extensions)

**Purpose**: Extend UI configuration to support new views and animations needed by all user stories

- [x] T001 Add scene transition animation config methods to `src/main/kotlin/org/bymc/gomoku/ui/window/UiConfig.kt` — add `getEnterGameAnimationDuration(): Duration`, `getExitGameAnimationDuration(): Duration` returning 500ms, and side panel layout methods: `getSidePanelWidth()`, `getSidePanelHeight()`, side panel button/label sizes and margins, rule selection overlay dimensions
- [x] T002 Implement new config methods in `src/main/kotlin/org/bymc/gomoku/ui/window/DefaultUiConfig.kt` — provide default values: animation duration 500ms, side panel width 300px (matching `getControlPanelWidth()`), side panel height matching board height, overlay size ~350x400 centered, button sizes matching existing buttons

---

## Phase 2: Foundational (Model Layer Extensions)

**Purpose**: Core model changes that MUST be complete before ANY user story can be fully implemented

**⚠️ CRITICAL**: The `retractLast()` capability is required by US1 (game lifecycle) and US3 (undo feature)

### Tests

- [x] T003 [P] Add `retractLast()` unit tests in `src/test/kotlin/org/bymc/gomoku/model/impl/HistoryImplTest.kt` — test cases: retract last from multi-record history returns last record only and reduces count by 1; retract last from single-record history returns that record and count becomes 0; retract last from empty history returns null; verify remaining records are untouched after retractLast
- [x] T004 [P] Add `retractLast()` unit tests in `src/test/kotlin/org/bymc/gomoku/game/impl/GameImplTest.kt` — test cases: retractLast during PLAYING removes last stone and swaps round actor; retractLast after BLACK_WON restores to PLAYING; retractLast after WHITE_WON restores to PLAYING; retractLast after DRAW restores to PLAYING; retractLast with 1 stone works (retractionAvailable true when dropCount >= 1); retractLast with 0 stones throws; board state reflects stone removal; dropCount decremented correctly

### Implementation

- [x] T005 [P] Add `retractLast(): DropRecord?` method to `src/main/kotlin/org/bymc/gomoku/model/abstraction/HistoryOperator.kt` interface
- [x] T006 Implement `retractLast()` in `src/main/kotlin/org/bymc/gomoku/model/impl/HistoryImpl.kt` — remove and return the last record from the linked list; return null if empty (depends on T005)
- [x] T007 [P] Add `retractLast()` method to `src/main/kotlin/org/bymc/gomoku/game/abstraction/Game.kt` interface
- [x] T008 Implement `retractLast()` in `src/main/kotlin/org/bymc/gomoku/game/impl/GameImpl.kt` — call `history.retractLast()`, clean cell from board, swap round actor back, call `rule.judgeGameState(board)` to re-judge actual game state (do NOT hardcode PLAYING — re-evaluation handles edge cases where five-in-a-row still exists after undo), update situation with `retractionAvailable = dropCount >= 1`; must work in any GameState (PLAYING, BLACK_WON, WHITE_WON, DRAW); throw if dropCount == 0 (depends on T006, T007)
- [x] T009 Run tests and verify: `mvn test -Dtest=HistoryImplTest,GameImplTest` — all new and existing tests must pass

**Checkpoint**: Model layer ready — retractLast() works in any game state, single-stone undo verified by tests

---

## Phase 3: User Story 1 - 进入研学模式并下棋 (Priority: P1) 🎯 MVP

**Goal**: Player can click "研学模式", select forbidden hand rules via overlay, enter the game with slide animation, place stones alternately, and see victory when five-in-a-row is formed.

**Independent Test**: Launch app → click 研学模式 → confirm rules → place stones alternately → form five-in-a-row → verify victory displayed and board disabled.

### Implementation

- [x] T010 [P] [US1] Create `SidePanelViewEventListener` interface in `src/main/kotlin/org/bymc/gomoku/ui/view/SidePanelViewEventListener.kt` — define `onRetractClicked()` and `onReturnClicked()` callback methods
- [x] T011 [P] [US1] Create `RuleSelectionOverlayView` in `src/main/kotlin/org/bymc/gomoku/ui/view/RuleSelectionOverlayView.kt` — extends `ViewBase` with white background and black border; contains three toggle labels (acting as checkboxes) for 3-3 禁手, 4-4 禁手, 长连禁手 (all default selected); contains "开始" and "取消" `SimpleTextButtonView` buttons; toggle click changes visual state (e.g., background color or text prefix `[✓]`/`[  ]`); dispatches events via listener interface with `onStartClicked(ttnhEnabled, ffnhEnabled, olnhEnabled)` and `onCancelClicked()`; overlay sized and positioned per UiConfig; initially hidden (`showing = false`)
- [x] T012 [P] [US1] Create `SidePanelView` in `src/main/kotlin/org/bymc/gomoku/ui/view/SidePanelView.kt` — extends `ViewBase` with white background and left black border; contains `LabelView` for round actor display ("黑方回合"/"白方回合"), `LabelView` for drop count, `LabelView` for status messages (victory/draw/forbidden hint), `SimpleTextButtonView` for "悔棋" (with enable/disable), `SimpleTextButtonView` for "返回"; exposes update methods: `updateGameInfo(roundActor: Stone, dropCount: Int)`, `showVictory(winner: Stone)`, `showDraw()`, `showForbiddenHint()`, `clearStatus()`, `setRetractionEnabled(enabled: Boolean)`; dispatches events to `SidePanelViewEventListener`; positioned off-screen right initially
- [x] T013 [US1] Create `EnterGameAnimation` in `src/main/kotlin/org/bymc/gomoku/ui/animation/EnterGameAnimation.kt` — follows `TitleAnimation` pattern; manages 6 parallel `ViewAreaAnimator` instances with `baseArea = null` (use current view position as start): board to left-aligned (x=0), title panel to off-screen left, self-study/challenge-AI/dual buttons to off-screen left, side panel to right-of-board (x=boardWidth); all 500ms duration with `EaseInOut` easing; implements `AnimationEventListener`; shows `CoveringLayerView` on play, hides when all animators finish; notifies listener via `EnterGameAnimationEventListener.onFinished()` (depends on T001, T002)
- [x] T014 [US1] Create `ExitGameAnimation` in `src/main/kotlin/org/bymc/gomoku/ui/animation/ExitGameAnimation.kt` — reverse of `EnterGameAnimation` with `baseArea = null` (use current view position as start): side panel to off-screen right, board to center, title panel and buttons to center positions; same 500ms duration, `EaseInOut`, `CoveringLayerView` management; notifies via `ExitGameAnimationEventListener.onFinished()` (depends on T001, T002)
- [x] T015 [US1] Wire self-study mode core flow in `src/main/kotlin/org/bymc/gomoku/ui/window/MainWindow.kt` — add `RuleSelectionOverlayView` and `SidePanelView` as member fields; add them to root view tree (overlay above board but below covering layer; side panel in appropriate z-order); register `selfStudyButtonView` click handler to show overlay; handle overlay "开始" event: hide overlay, create `Game` via `GameFactory.createGame(boardSize, Date(), ttnhEnabled, ffnhEnabled, olnhEnabled)`, bind board view models via `boardView.setViewModels()`, enable board sensor, play `EnterGameAnimation`; handle overlay "取消" event: hide overlay; implement `BoardSensorViewEventListener.onCellClicked()`: call `game.dropStoneAt(location)`, if LEGAL update board view and side panel info, check game state for victory (call `sidePanelView.showVictory()`), draw (call `sidePanelView.showDraw()`), or ongoing play, if non-PLAYING (BLACK_WON/WHITE_WON/DRAW) disable board sensor; on enter-animation finished: hide covering layer, show side panel info; manage `game` as nullable field (null when on title screen) (depends on T010, T011, T012, T013, T014, T008)
- [x] T016 [US1] Manual test: launch app with `mvn clean javafx:run`, click 研学模式, verify overlay appears with three rules selected, click 开始, verify slide animation plays, place stones alternately on board, verify round actor and drop count update in side panel, form five-in-a-row, verify victory message appears and board is disabled

**Checkpoint**: Core self-study mode works — enter game, place stones, win detection, side panel updates

---

## Phase 4: User Story 2 - 禁手规则选择 (Priority: P1)

**Goal**: Player can freely toggle forbidden hand rules in the overlay, and the selected rules are correctly enforced during gameplay. Forbidden move attempts produce visual + text feedback.

**Independent Test**: Open overlay → deselect some rules → start game → verify deselected rules are not enforced; repeat with all rules enabled → attempt forbidden move → verify red flash and text hint.

### Implementation

- [x] T017 [US2] Add forbidden move red flash support to `src/main/kotlin/org/bymc/gomoku/ui/view/BoardGridView.kt` — add `flashCell(location: Location2D)` method that renders a semi-transparent red overlay on the specified cell position; use a `javax.swing.Timer` (single-shot, ~300ms) to clear the flash; in `onRender`, check if a flash is active and draw a red filled rectangle with alpha ~128 at the cell coordinates
- [x] T018 [US2] Add `flashCell()` pass-through to `src/main/kotlin/org/bymc/gomoku/ui/view/BoardIntegrationView.kt` — delegate to `BoardGridView.flashCell(location)` so `MainWindow` can trigger the flash via the integration view
- [x] T019 [US2] Wire forbidden move feedback in `src/main/kotlin/org/bymc/gomoku/ui/window/MainWindow.kt` — in `onCellClicked()`, when `dropStoneAt()` returns `FORBIDDEN_BY_TTNH_RULE`, `FORBIDDEN_BY_FFNH_RULE`, or `FORBIDDEN_BY_OLNH_RULE`: call `boardView.flashCell(location)` for the red flash and `sidePanelView.showForbiddenHint()` for the "禁手位置，无法落子" text; when `dropStoneAt()` returns LEGAL, call `sidePanelView.clearStatus()` to clear any previous forbidden hint; ignore OCCUPIED_BY_SAME_STONE and OCCUPIED_BY_DIFFERENT_STONE silently (depends on T017, T018)
- [x] T020 [US2] Verify rule selection toggle logic in `RuleSelectionOverlayView` — ensure clicking a toggle label flips its state and the visual indicator updates correctly; ensure cancel button hides overlay without starting game; ensure start button passes the three boolean flags correctly to the listener
- [x] T021 [US2] Manual test: launch app, open overlay, deselect "3-3 禁手", start game, place black stones forming a 3-3 pattern (should be allowed), return to title, restart with all rules enabled, attempt same 3-3 placement (should flash red and show hint text)

**Checkpoint**: Forbidden hand rules configurable and enforced; forbidden move feedback (red flash + text) working

---

## Phase 5: User Story 3 - 悔棋 (Priority: P2)

**Goal**: Player can undo the last move at any time — during play, after victory, after draw. The retract button is disabled when no stones exist.

**Independent Test**: Place several stones → click 悔棋 → verify last stone removed and round switched; achieve victory → click 悔棋 → verify game returns to PLAYING and board re-enables.

### Implementation

- [x] T022 [US3] Wire retract button handler in `src/main/kotlin/org/bymc/gomoku/ui/window/MainWindow.kt` — implement `SidePanelViewEventListener.onRetractClicked()`: call `game.retractLast()`, re-bind board view models via `boardView.setViewModels()` to refresh rendering, update side panel via `updateGameInfo()`, re-evaluate `retractionAvailable` and call `sidePanelView.setRetractionEnabled()`, if game was in terminal state (victory/draw) and is now PLAYING: re-enable board sensor via `boardView.enable()` and clear victory/draw status; handle retract button enable/disable: after each `dropStoneAt()` or `retractLast()`, update side panel retract button state based on `game.getGameSituation().dropCount >= 1`; initial state on game start: retract button disabled (dropCount == 0) (depends on T008, T015)
- [x] T023 [US3] Manual test: launch app, enter game, place 3 stones, click 悔棋, verify last stone removed and round actor switched back; place stones to achieve five-in-a-row, click 悔棋, verify victory message clears and board re-enables; undo all stones back to empty board, verify retract button becomes disabled; edge case: place only 1 black stone (white's turn), click 悔棋, verify black's stone is removed and retract button becomes disabled

**Checkpoint**: Full undo support working in all game states — retract button enable/disable correct

---

## Phase 6: User Story 4 - 返回标题画面 (Priority: P2)

**Goal**: Player can return to title screen with slide animation from any game state; game data is fully cleared.

**Independent Test**: Enter game → place some stones → click 返回 → verify exit animation plays → verify title screen restored and board cleared.

### Implementation

- [x] T024 [US4] Wire return button handler in `src/main/kotlin/org/bymc/gomoku/ui/window/MainWindow.kt` — implement `SidePanelViewEventListener.onReturnClicked()`: play `ExitGameAnimation`; on exit-animation finished: set `game = null`, clear board (remove all view model bindings or reset board view), disable board sensor, reset side panel to initial state, reset rule selection overlay defaults (all rules selected); ensure re-entering self-study mode creates a fresh game (depends on T014, T015)
- [x] T025 [US4] Manual test: launch app, enter self-study mode, place several stones, click 返回, verify exit slide animation plays smoothly, verify title screen fully restored, click 研学模式 again, verify fresh game starts with clean board

**Checkpoint**: Complete game lifecycle — enter, play, return, re-enter all working

---

## Phase 7: Polish & Cross-Cutting Concerns

**Purpose**: Edge cases, final validation, and refinements across all user stories

- [x] T026 [P] Verify draw (和局) handling in `src/main/kotlin/org/bymc/gomoku/ui/window/MainWindow.kt` — confirm that T015's `onCellClicked()` implementation correctly handles DRAW state (calls `sidePanelView.showDraw()` and disables board sensor); verify undo from draw state works correctly (restores to PLAYING and re-enables board)
- [x] T027 [P] Handle animation interaction blocking in `src/main/kotlin/org/bymc/gomoku/ui/window/MainWindow.kt` — verify `CoveringLayerView` is shown during both enter and exit animations (already handled by animation classes); ensure no game actions can be triggered during animation playback
- [x] T028 [P] Verify edge case: overlay default-start in `src/main/kotlin/org/bymc/gomoku/ui/view/RuleSelectionOverlayView.kt` — clicking "开始" without modifying any toggles should pass (true, true, true) to the listener; verify this works correctly
- [x] T029 Compile and run full test suite: `mvn clean compile && mvn test` — all existing and new tests must pass
- [x] T030 Full end-to-end manual validation with `mvn clean javafx:run` — run through all acceptance scenarios from spec.md: US1 (enter, play, win), US2 (toggle rules, forbidden feedback), US3 (undo in all states), US4 (return and re-enter); verify all edge cases: draw, animation blocking, occupied cell ignored (click on existing stone should have no effect), undo to empty board, undo single stone; verify SC-001: entire flow from clicking 研学模式 to placing first stone completes within 5 seconds

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies — can start immediately
- **Foundational (Phase 2)**: T005/T007 (interface changes) can start in parallel with Phase 1; T006/T008 depend on T005/T007; T003/T004 (tests) can be written in parallel
- **User Story 1 (Phase 3)**: Depends on Phase 1 (UiConfig) and Phase 2 (retractLast for game lifecycle)
- **User Story 2 (Phase 4)**: Depends on Phase 3 (core game flow must work first)
- **User Story 3 (Phase 5)**: Depends on Phase 3 (side panel and game instance must exist) and Phase 2 (retractLast)
- **User Story 4 (Phase 6)**: Depends on Phase 3 (enter animation and game flow) and Phase 4 (ExitGameAnimation created in US1 phase)
- **Polish (Phase 7)**: Depends on all user stories being complete

### User Story Dependencies

- **US1 (P1)**: Foundation + Setup → can start
- **US2 (P1)**: US1 must be complete (needs working game flow + board feedback)
- **US3 (P2)**: US1 must be complete (needs side panel + game instance)
- **US4 (P2)**: US1 must be complete (needs animation infrastructure + game lifecycle)

### Within Each User Story

- Views/interfaces before animations (animations reference views)
- Animations before MainWindow integration (MainWindow uses animations)
- Core flow before feedback (feedback builds on working flow)

### Parallel Opportunities

- T001 + T002 can be done together (same UiConfig pair)
- T003 + T004 (test writing) in parallel with T005 + T007 (interface declarations)
- T010 + T011 + T012 (new view files) all in parallel
- T013 + T014 (two animation classes) depend on UiConfig but are independent of each other
- T017 + T018 (board flash support) can be done together
- T026 + T027 + T028 (polish tasks) all in parallel

---

## Parallel Example: User Story 1

```text
# Step 1: Create all new view files in parallel
Task T010: "Create SidePanelViewEventListener in ui/view/SidePanelViewEventListener.kt"
Task T011: "Create RuleSelectionOverlayView in ui/view/RuleSelectionOverlayView.kt"
Task T012: "Create SidePanelView in ui/view/SidePanelView.kt"

# Step 2: Create both animations in parallel (after UiConfig ready)
Task T013: "Create EnterGameAnimation in ui/animation/EnterGameAnimation.kt"
Task T014: "Create ExitGameAnimation in ui/animation/ExitGameAnimation.kt"

# Step 3: Wire everything together (sequential — single file)
Task T015: "Wire self-study mode in MainWindow.kt"

# Step 4: Manual verification
Task T016: "Manual test core flow"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: UiConfig extensions
2. Complete Phase 2: retractLast() model changes + tests
3. Complete Phase 3: US1 — core game flow
4. **STOP and VALIDATE**: Enter game, place stones, verify victory → this is a playable MVP

### Incremental Delivery

1. Setup + Foundational → Model ready
2. Add US1 → Core game playable (MVP!)
3. Add US2 → Forbidden rules configurable + feedback
4. Add US3 → Undo in all states
5. Add US4 → Complete lifecycle with return
6. Polish → Edge cases and final validation

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- US2 depends on US1 because forbidden feedback needs a working game session
- US3 and US4 also depend on US1 for the same reason
- Commit after each phase completion
- All animations use EaseInOut (not EaseInOutBack) for smoother transitions
- Board coordinates reference: center x=150, left x=0, side panel x=615, off-screen right x=915
