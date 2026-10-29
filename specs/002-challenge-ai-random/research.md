# 研究记录：人机对战模式（随机策略 AI）

**日期**: 2026-10-28 | **分支**: `002-challenge-ai-random` | **状态**: 已完成

技术上下文所有条目均已在前期调研中确认，无 NEEDS CLARIFICATION 遗留。以下记录关键设计决策及其依据，供 Phase 1 设计与后续任务分解引用。

## 研究任务 1: AI 落子调度机制

**决策**: 使用 `javax.swing.Timer`（`setRepeats(false)` 单次触发）在玩家落子后延迟约 500ms 调度 AI 落子。

**依据**: 项目动画框架 `AnimatorBase` 已基于 `javax.swing.Timer`，回调天然在 EDT 上执行，与 UI 渲染同线程，无需加锁；单次触发语义恰好匹配"等待一步 AI 落子"的需求。

**备选方案**: `Thread.sleep` 后台线程（需处理线程安全与 UI 提交，复杂度不必要）；`ScheduledExecutorService`（引入多线程，违反单线程约束假设）；JavaFX `PauseTransition`（项目渲染混用 AWT，不引入新依赖）。

## 研究任务 2: AI 玩家抽象归属与形态

**决策**: `AiPlayer` 接口放 `game/abstraction/`，`RandomAiPlayer` 实现放 `game/impl/`，接口方法接收 `BoardViewModel`、`Rule`、`Stone`，返回可空 `Location2D`。

**依据**: game 层本就依赖 model 层（`GameImpl` 使用 `BoardViewModel`/`Rule`），依赖方向合规；接口化便于后续替换更强 AI；返回可空以覆盖"无合法落子点"极端局面；实现类支持注入随机种子以保证测试可复现。

**备选方案**: 放 ui 层（不可单测，耦合界面）；放 model 层（AI 是对弈行为而非棋盘数据，职责不符）；接口接收 `Game`（过度耦合游戏流程，校验仅需棋面与规则）。

## 研究任务 3: 合法落子判定方式

**决策**: `RandomAiPlayer` 收集空位后逐点调用 `Rule.checkDropLegality(boardViewModel, drop)` 过滤，仅从 `LEGAL` 结果中随机取点。

**依据**: `Rule.checkDropLegality` 为纯校验接口，不产生落子副作用，恰好满足"只查不下"的需求；三条禁手规则（3-3/4-4/长连）的判定逻辑已在 model 层完备实现，直接复用零重复成本。

**备选方案**: AI 直接尝试 `Game.dropStoneAt` 并回滚（副作用风险高，历史记录会受污染）；在 AI 内自行实现禁手判定（重复 model 层逻辑，违反分层）。

## 研究任务 4: 覆盖层执子选择与事件签名

**决策**: `RuleSelectionOverlayView` 新增执子行（黑/白互斥按钮，默认黑）与 `setStoneSelectionVisible(visible)` 方法；`RuleSelectionOverlayViewEventListener.onStartClicked` 直接增加 `userStone: Stone` 参数（研学模式调用方忽略该值）。

**依据**: 该监听器唯一实现是 `MainWindow`，直接改签名是可控重构，避免新增并行回调造成双入口；互斥按钮比开关（仅表达布尔）与下拉（实现成本高、样式突兀）更贴合"二选一"语义。

**备选方案**: 新增独立回调 `onStartClickedWithStone`（双入口歧义）；用 `ToggleSwitchView` 表达执黑开/关（语义迂回）；新增颜色选择弹窗（多一层交互，违背轻量原则）。

## 研究任务 5: 回合驱动与竞态防护

**决策**: 将 `MainWindow.onCellClicked` 中落子后的刷新/判定逻辑抽取为私有方法 `applyDrop(location)` 供玩家与 AI 共用；AI 回合期间同时禁用棋盘与悔棋按钮；`onReturnClicked` 中停止 AI 定时器。

**依据**: 澄清会话确认悔棋在 AI 等待期一并禁用（与棋盘一致，避免撤两步后 AI 定时落子仍触发的竞态）；返回时停止定时器是第二道防线，两道防护叠加后无状态错乱窗口。

**备选方案**: 悔棋触发时取消 AI 定时并撤子（已明确否决，交互语义复杂）；每帧轮询回合归属（过度设计）。

## 研究任务 6: 悔棋撤两步语义

**决策**: 挑战 AI 模式下悔棋按钮可用条件为 `dropCount >= 2`；触发时连续两次 `Game.retractLast()`，随后按现有逻辑刷新界面并恢复玩家回合。

**依据**: 撤两步后回合归属必然回到玩家（黑先手的两子或白后手的两子都构成完整回合对），无需额外回合修正逻辑；终局状态悔棋由 `Game.retractLast` 既有能力支持恢复 `PLAYING`。

**备选方案**: 仅撤一步（撤后立即又轮到 AI，体验怪异，讨论中已否决）；新增"撤到玩家上一手"的通用接口（超出本功能范围）。

## 研究任务 7: 胜负文案玩家视角

**决策**: `SidePanelView` 新增带执子方参数的胜负展示方法，挑战 AI 模式显示“你赢了!/你输了!”，研学模式维持“黑方胜利!/白方胜利!”。

**依据**: 澄清会话确认采用玩家视角；通过参数化方法区分模式，避免调用方自行拼接文案。

**备选方案**: 统一维持黑方/白方文案（代入感差，讨论中已否决）；在 `MainWindow` 内拼文案（展示逻辑散落，复用性差）。
