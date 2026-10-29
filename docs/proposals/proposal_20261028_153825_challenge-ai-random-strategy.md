# Proposal: 人机对战模式（随机策略 AI）

- **状态**: 待评审
- **日期**: 2026-10-28
- **范围**: ui / game 层

## 背景与目标

当前应用仅研学模式（自研棋局）可玩，主界面的"挑战 AI"按钮存在但点击无响应。本提案旨在打通人机对战流程：玩家在进入对战前选择规则与执子颜色，AI 采用最简单的随机合法落子策略先行或后行，为后续接入更强 AI 建立可扩展的框架。

## 需求描述

1. 点击"挑战 AI"后弹出规则选择覆盖层，除三条禁手规则开关外，新增"执子"选择（黑棋/白棋互斥，默认黑棋）。
2. 玩家执黑先行、执白后行；执白时 AI 以黑棋先行。
3. AI 策略为随机策略：在所有空位中随机选择一个符合规则的落子点（黑棋需避开禁手点，白棋无禁手）。
4. AI 落子延迟 500ms（含执白时 AI 的第一手），保证节奏自然。
5. 悔棋一次撤两步（AI 一子 + 玩家一子），撤完仍轮到玩家落子。
6. 研学模式不受影响：覆盖层不显示执子选择行，行为与现状一致。

## 现状分析

- `MainWindow.onClicked` 中 `challengeAiButtonView` 无处理分支，为入口接入点。
- `RuleSelectionOverlayView` 已实现规则开关 + 开始/取消按钮，事件经 `RuleSelectionOverlayViewEventListener.onStartClicked(ttnh, ffnh, olnh)` 上报；覆盖层尺寸与行布局由 `UiConfig` 配置驱动。
- `Game` 接口提供 `getGameSituation().roundActor`（当前执子方）、`dropStoneAt`（落子并返回合法性）、`retractLast`（撤最后一步）；`GameSituation` 含 `state`/`roundActor`/`dropCount` 等字段。
- `Rule.checkDropLegality(boardViewModel, drop)` 可在不落子的前提下校验合法性，供 AI 过滤禁手点。
- 动画框架基于 `javax.swing.Timer`（回调在 EDT），AI 延迟落子复用同机制即可，无需引入新线程模型。

## 设计方案

### AI 玩家抽象（game 层）

新建 `game/abstraction/AiPlayer` 接口与 `game/impl/RandomAiPlayer` 实现：

```
interface AiPlayer {
    fun pickDropLocation(boardViewModel: BoardViewModel, rule: Rule, stone: Stone): Location2D?
}
```

- `RandomAiPlayer` 收集全部空位，经 `rule.checkDropLegality` 过滤非法点后随机返回其一；无合法点时返回 `null`（调用方停止调度）。
- 构造器接受可选随机种子以便测试；未来替换强 AI 时新增实现类即可，`MainWindow` 仅依赖接口。
- 归属 game 层的理由：依赖 model 层的 `BoardViewModel`/`Rule`/`Stone`，与 `Game` 同级，且需要单元测试覆盖。

### 覆盖层扩展（ui 层）

- `RuleSelectionOverlayView` 在禁手开关与底部按钮之间新增"执子"行：标签 + "黑棋"/"白棋"两个互斥选择按钮，默认黑棋。
- 新增 `setStoneSelectionVisible(visible: Boolean)`：挑战 AI 时显示，研学模式隐藏；行隐藏时整体布局保持现状。
- 覆盖层高度不足时调整 `UiConfig.getRuleSelectionOverlaySize()` 并同步行布局参数。
- 事件接口 `onStartClicked` 增加参数 `userStone: Stone`（研学模式下恒传黑棋，调用方忽略）。
- `resetDefaults()` 同步重置执子选择为黑棋。

### MainWindow 调度（ui 层）

新增状态 `aiStone: Stone?`（`null` 表示研学模式）与 `aiTimer: javax.swing.Timer`（单次触发）：

1. **入口分流**: "挑战 AI"点击 → 覆盖层 `setStoneSelectionVisible(true)` 后弹出；"研学模式"点击 → `setStoneSelectionVisible(false)` 后弹出。
2. **开局**: `onStartClicked` 记录 `aiStone = userStone 的对手色`，创建游戏并播放进入动画（复用现有流程）。
3. **AI 先行**: `onFinished(EnterGameAnimation)` 中若 `aiStone == BLACK`，延迟 500ms 触发 AI 落子。
4. **回合驱动**: 将 `onCellClicked` 中落子成功后的刷新与胜负判定逻辑抽取为私有方法 `applyDrop(location)`，玩家落子与 AI 落子共用。玩家落子后若 `state == PLAYING && roundActor == aiStone`，则 `boardView.disable()` 并启动 500ms 单次 Timer 调用 AI；AI 落子后若仍轮到玩家则 `boardView.enable()`。
5. **悔棋**: AI 模式下 `setRetractionEnabled(dropCount >= 2)`；触发时连续两次 `retractLast`（AI 一子 + 玩家一子）后按现有逻辑刷新。终局后悔棋同样撤两步并恢复棋盘可用。研学模式保持现有"撤一步"行为。
6. **竞态防护**: `onReturnClicked` 与退出动画开始时停止 `aiTimer`，避免返回标题后 AI 仍然落子；AI 回合期间棋盘禁用，玩家无法抢下。

### 交互体验细节

- **AI 落点辨识**: 棋盘已有最后一手反色方块标记（`BoardGridView.drawLastDrop`，黑子上白块/白子上黑块），AI 落子后自动高亮，无需新增渲染。
- **胜负文案代入感**: 现有 `showVictory` 固定显示“黑方胜利!/白方胜利!”。AI 模式下改为“你赢了!/你输了!”（由 `SidePanelView` 新增带执子方参数的展示方法，研学模式文案不变）。
- **AI 思考中提示（可选）**: AI 落子前的 500ms 延迟内，侧面板回合标签临时显示“AI 思考中...”，落子后恢复实际回合文案；延迟较短，是否值得加待评审定夺。
- **AI 无合法点**: 极端局面（黑棋所有空位均为禁手）下 `pickDropLocation` 返回 `null`，视为和局（`showDraw`）并终局。

### 不变项

- 三层架构与既有接口签名（除 `onStartClicked` 增参外）不变。
- 研学模式全部行为不变。
- 胜负判定、胜利连线、侧面板提示复用现有逻辑。

## 测试计划

- 新增 `RandomAiPlayerTest`（game 层）：
  - 空棋盘返回非空且在枰内的合法位置；
  - 返回位置必为空位（多次随机验证）；
  - 构造 3-3 禁手局面，黑棋多次随机从不返回禁手点；
  - 白棋无禁手约束（禁手点对白棋为合法落子点）；
  - 无空位或无合法点时返回 `null`；
  - 固定种子下结果可复现。
- `MainWindow` 等其余 UI 类维持无单测的现状（依赖 JavaFX/AWT 环境）。
- 人工验收清单：执黑/执白各完整对局一局、悔棋（含终局悔棋）、对局中返回标题、AI 回合内点击棋盘无响应、AI 落点标记正确、AI 模式胜负文案为“你赢了/你输了”、研学模式回归。

## 影响范围与风险

- `RuleSelectionOverlayViewEventListener` 签名变更影响 `MainWindow`（唯一实现），属可控重构。
- 覆盖层加行涉及尺寸/布局调整，需人工核对视觉效果。
- AI Timer 与退出动画存在时序耦合，以"返回即取消 Timer"兜底；极端情况下 Timer 已触发但动画未结束，因 EDT 串行执行也不会产生并发问题。
- `MainWindow` 已承担较多职责，本提案通过抽取 `applyDrop` 控制膨胀，若后续 AI 流程继续复杂化可考虑拆分控制器。

## 工作量估算

| 模块 | 内容 | 预估 |
| --- | --- | --- |
| game 层 | `AiPlayer` 接口、`RandomAiPlayer`、单元测试 | ~150 行 |
| ui 层 | 覆盖层执子行、事件签名、布局配置 | ~120 行 |
| ui 层 | `MainWindow` 入口分流、AI 调度、悔棋、竞态 | ~100 行 |

合计约 370 行新增/修改，一次提交或按 game/ui 拆两次提交均可。

## 决策请求

1. 是否接受 `onStartClicked` 直接增加参数（而非新增独立回调）？
2. AI 玩家归属 game 层（`game/abstraction` + `game/impl`）是否认可？
3. 覆盖层执子行的控件形态（两个互斥按钮）是否认可，或倾向开关/下拉？
4. AI 模式胜负文案改为“你赢了!/你输了!”是否认可？
5. 500ms 延迟内是否需要“AI 思考中...”提示（成本极低，但延迟较短时可能意义不大）？
