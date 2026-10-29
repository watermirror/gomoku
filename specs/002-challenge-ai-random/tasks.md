# 任务清单：人机对战模式（随机策略 AI）

**输入**: `/specs/002-challenge-ai-random/` 设计文档（plan.md、spec.md、research.md、data-model.md、contracts/、quickstart.md）
**前提**: plan.md（必需）、spec.md（必需）
**测试**: 项目规范要求新增代码必须配套单元测试，本清单包含 game 层测试任务（TDD：先写测试确认失败再实现）；UI 层维持无单测现状。

## 格式说明: `[ID] [P?] [Story] 描述`

- **[P]**: 可并行（不同文件、无未完成依赖）
- **[Story]**: 所属用户故事（US1~US4，映射 spec.md）
- 每条任务均含明确文件路径

---

## Phase 1: Setup（共享基础设施）

**目的**: 确认基线可用

- [x] T001 在分支 `002-challenge-ai-random` 上执行 `mvn clean compile && mvn test` 确认既有基线全部通过

---

## Phase 2: Foundational（阻塞性前置，全部用户故事依赖）

**目的**: AI 玩家抽象、随机策略实现与调度配置，US1~US4 均依赖

- [x] T002 创建 `AiPlayer` 接口（含 KDoc 与线程/副作用约定）于 src/main/kotlin/org/bymc/gomoku/game/abstraction/AiPlayer.kt（契约见 specs/002-challenge-ai-random/contracts/ai-player-and-ui-events.md 契约 1）
- [x] T003 编写 `RandomAiPlayerTest`（空位合法性、黑棋避禁手、白棋无禁手约束、无合法点返回 null、固定种子可复现），先确认编译失败于 src/test/kotlin/org/bymc/gomoku/game/impl/RandomAiPlayerTest.kt
- [x] T004 实现 `RandomAiPlayer`（收集空位、`Rule.checkDropLegality` 过滤、可注入随机种子）于 src/main/kotlin/org/bymc/gomoku/game/impl/RandomAiPlayer.kt，运行 T003 测试转绿
- [x] T005 [P] 新增 `getAiDropDelay()` 配置（默认 500ms）于 src/main/kotlin/org/bymc/gomoku/ui/window/UiConfig.kt 与 src/main/kotlin/org/bymc/gomoku/ui/window/DefaultUiConfig.kt

**检查点**: `mvn test` 通过；AI 能力就绪，可开始用户故事

---

## Phase 3: User Story 1 - 挑战 AI 完整对局 (Priority: P1) 🎯 MVP

**目标**: 从"挑战 AI"入口选择规则与执子（默认黑），与随机 AI 完成一局，终局呈现玩家视角文案
**独立测试**: 按 quickstart.md 场景 1——执黑完整对局一局，验证设置界面、AI 落子节奏（约 500ms）、最后一手标记、终局文案"你赢了!/你输了!"

### 实现任务

- [x] T006 [US1] 扩展 `RuleSelectionOverlayView`：新增执子行（"执子"标签 + 黑/白互斥按钮，默认黑）与 `setStoneSelectionVisible(visible)`，`resetDefaults()` 重置执子为黑棋；行布局参数（标签/按钮尺寸与坐标）加入 src/main/kotlin/org/bymc/gomoku/ui/view/RuleSelectionOverlayView.kt、src/main/kotlin/org/bymc/gomoku/ui/window/UiConfig.kt、src/main/kotlin/org/bymc/gomoku/ui/window/DefaultUiConfig.kt（契约 3）
- [x] T007 [US1] 变更事件签名 `onStartClicked(ttnhEnabled, ffnhEnabled, olnhEnabled, userStone: Stone)` 并同步 `startButton` 点击上报所选执子，修复 src/main/kotlin/org/bymc/gomoku/ui/view/RuleSelectionOverlayViewEventListener.kt 与 src/main/kotlin/org/bymc/gomoku/ui/view/RuleSelectionOverlayView.kt、src/main/kotlin/org/bymc/gomoku/ui/window/MainWindow.kt 的编译（契约 2）
- [x] T008 [P] [US1] 为 `SidePanelView` 新增 `showVictory(winner: Stone, userStone: Stone)` 重载（`winner == userStone` 显示"你赢了!"，否则"你输了!"，红色系沿用）于 src/main/kotlin/org/bymc/gomoku/ui/view/SidePanelView.kt（契约 4）
- [x] T009 [US1] 在 `MainWindow` 集成 AI 调度: 入口分流（挑战 AI 显示执子行/研学隐藏）、`aiStone` 状态、抽取 `applyDrop(location)` 供玩家与 AI 共用、AI 待落子期间禁用棋盘与悔棋、AI 落子后恢复、终局调用双参胜负文案、AI 无合法点判和局于 src/main/kotlin/org/bymc/gomoku/ui/window/MainWindow.kt
- [ ] T010 [US1] 执行 `mvn test` 全量回归并按 quickstart.md 场景 1 手动验证执黑对局

**检查点**: US1 独立可用——执黑完整对局 MVP 达成

---

## Phase 4: User Story 2 - 执白后行 (Priority: P2)

**目标**: 玩家选白棋时 AI 执黑先行，进入动画完成后约 500ms 落第一子
**独立测试**: quickstart.md 场景 2——执白开局，AI 先落子且延迟约 500ms，之后回合交替正常

### 实现任务

- [x] T011 [US2] 在 `onFinished(animation: EnterGameAnimation)` 中当 `aiStone == BLACK` 且棋局进行中时启动 AI 延迟调度（进入动画完成后约 500ms AI 先行，期间棋盘禁用）于 src/main/kotlin/org/bymc/gomoku/ui/window/MainWindow.kt
- [ ] T012 [US2] 按 quickstart.md 场景 2 手动验证执白对局完整流程

**检查点**: US1（执黑）与 US2（执白）均可独立对局

---

## Phase 5: User Story 3 - 悔棋撤两步 (Priority: P3)

**目标**: 挑战 AI 模式悔棋一次撤两子（AI 一子 + 玩家一子），撤后仍轮到玩家；AI 等待期与子数不足时按钮不可用
**独立测试**: quickstart.md 场景 3——常规悔棋减两子、执白仅一手时按钮禁用、终局悔棋恢复对局

### 实现任务

- [x] T013 [US3] 调整 `onRetractClicked` 与悔棋可用性: 挑战 AI 模式依回合归属决定撤子数（轮玩家撤两步、玩家胜终局撤一步，保证撤后轮玩家），研学模式保持撤一步语义于 src/main/kotlin/org/bymc/gomoku/ui/window/MainWindow.kt
- [ ] T014 [US3] 按 quickstart.md 场景 3 手动验证三种悔棋场景（常规/禁用/终局悔棋）

**检查点**: US1~US3 全部独立可用

---

## Phase 6: User Story 4 - 对局中返回 (Priority: P4)

**目标**: 对局中返回立即回到标题画面，取消 AI 待落子，无残留
**独立测试**: quickstart.md 场景 4——AI 待落子期间点返回，退出动画后标题画面干净、重开无残留

### 实现任务

- [x] T015 [US4] 在 `onReturnClicked` 中停止 AI 定时器并清理待落子状态，返回后重开任意模式均为空棋盘于 src/main/kotlin/org/bymc/gomoku/ui/window/MainWindow.kt
- [ ] T016 [US4] 按 quickstart.md 场景 4 手动验证返回与重开

**检查点**: 全部用户故事完成

---

## Phase 7: Polish 与横切关注点

**目的**: 收尾验证与提交

- [x] T017 [P] 执行 `mvn clean test` 全量回归，确认既有研学模式测试无退化
- [ ] T018 按 quickstart.md 全部 5 个场景（含场景 5 研学模式回归）完成端到端人工验收
- [ ] T019 按 Conventional Commits 分组提交（game 层 AI: `feat(game): ...`; UI 集成: `feat(ui): ...`），提交信息不含 AI 归因

---

## 依赖与执行顺序

### 阶段依赖

- **Phase 1**: 无依赖，立即开始
- **Phase 2**: 依赖 Phase 1；阻塞全部用户故事
- **Phase 3~6（US1~US4）**: 均依赖 Phase 2；建议按优先级串行（US1 为 MVP，后续故事在 US1 的 MainWindow 集成之上递增，避免同文件冲突）
- **Phase 7**: 依赖全部用户故事完成

### 用户故事依赖

- **US1 (P1)**: Phase 2 后即可开始，无跨故事依赖
- **US2 (P2)**: 复用 US1 的执子选择与 AI 调度能力，建议在 US1 后实施（同文件 MainWindow.kt，不建议并行）
- **US3 (P3)**: 依赖 US1 的挑战 AI 模式状态（同文件，串行）
- **US4 (P4)**: 依赖 US1 的 AI 定时器（同文件，串行）

### 故事内顺序

- 测试先于实现（T003 → T004）
- 契约/接口先于集成（T002 → T006/T007/T009）
- US1 内: 执子行 UI（T006）→ 事件签名（T007）→ AI 调度集成（T009）；T008 与 T006/T007 可并行（不同文件）

### 并行机会

- Phase 2: T005 与 T003/T004 可并行（不同文件）
- Phase 3: T008（SidePanelView.kt）可与 T006/T007（覆盖层文件）并行
- Phase 7: T017 可与 T018 的场景准备并行

---

## 并行示例: Phase 2 与 Phase 3

```bash
# Phase 2 内并行:
Task: "T005 getAiDropDelay 配置 (UiConfig.kt + DefaultUiConfig.kt)"
Task: "T004 RandomAiPlayer 实现 (game/impl/RandomAiPlayer.kt)"   # 与 T005 不同文件

# Phase 3 内并行:
Task: "T006 执子行 UI (RuleSelectionOverlayView.kt + UiConfig.kt + DefaultUiConfig.kt)"
Task: "T008 SidePanelView 胜负重载 (SidePanelView.kt)"
```

---

## 实施策略

### MVP 优先（仅 US1）

1. Phase 1 → Phase 2 → Phase 3（US1）
2. **停下验证**: quickstart.md 场景 1 独立验收执黑对局
3. 可交付演示

### 增量交付

1. 基础（AI 玩家 + 配置）→ 验证测试绿
2. US1 执黑对局 → 验收（MVP!）
3. US2 执白后行 → 验收
4. US3 悔棋撤两步 → 验收
5. US4 返回取消 → 验收
6. Phase 7 回归 + 提交

---

## 备注

- [P] = 不同文件且无未完成依赖
- [Story] 标签映射 spec.md 用户故事，便于追溯
- 每个检查点可暂停独立验证
- 每完成一个任务或逻辑组提交一次（Conventional Commits，无 AI 归因）
- 避免: 模糊任务、同文件并行冲突、破坏故事独立性的跨故事依赖
