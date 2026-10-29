# 契约：AI 玩家接口与 UI 事件签名变更

**日期**: 2026-10-28 | **规格**: [spec.md](spec.md)

本项目为桌面应用，无对外网络 API。本文件记录本功能引入/变更的 Kotlin 接口契约，供实现与测试对照。

## 契约 1: AiPlayer（新增）

**位置**: `game/abstraction/AiPlayer.kt`

```kotlin
interface AiPlayer {
    fun pickDropLocation(boardViewModel: BoardViewModel, rule: Rule, stone: Stone): Location2D?
}
```

**语义**:

- 调用方保证在游戏进行中（`state == PLAYING`）且轮到 `stone` 方时调用。
- 返回值必须为空位，且当 `stone` 为黑棋时必须通过 `rule.checkDropLegality` 校验（`LEGAL`）。
- 棋盘无空位或无任何合法点时返回 `null`，调用方据此按和局处理。
- 纯查询，不得产生落子等副作用。
- 线程约定: 在 EDT 上调用；实现不得自行开启线程。

**实现**: `RandomAiPlayer`（`game/impl/RandomAiPlayer.kt`），构造器接受可选随机种子；策略为从合法空位集合中均匀随机取点。

## 契约 2: RuleSelectionOverlayViewEventListener.onStartClicked（签名变更）

**位置**: `ui/view/RuleSelectionOverlayViewEventListener.kt`

```kotlin
// 旧
fun onStartClicked(ttnhEnabled: Boolean, ffnhEnabled: Boolean, olnhEnabled: Boolean)
// 新
fun onStartClicked(ttnhEnabled: Boolean, ffnhEnabled: Boolean, olnhEnabled: Boolean, userStone: Stone)
```

**语义**:

- `userStone` 为设置界面当前选中的执子颜色；执子行隐藏时恒为黑棋（默认值），研学模式调用方忽略该参数。
- 唯一实现方 `MainWindow` 同步更新；无其他实现。

## 契约 3: RuleSelectionOverlayView.setStoneSelectionVisible（新增）

**位置**: `ui/view/RuleSelectionOverlayView.kt`

```kotlin
fun setStoneSelectionVisible(visible: Boolean)
```

**语义**:

- `visible == true` 显示执子行（挑战 AI 入口）；`false` 隐藏（研学入口），隐藏时整体布局与现状一致。
- 与 `resetDefaults()` 独立: `resetDefaults()` 恒定将执子选择重置为黑棋。

## 契约 4: SidePanelView 胜负展示（扩展）

**位置**: `ui/view/SidePanelView.kt`

```kotlin
// 既有（保留，研学模式使用）
fun showVictory(winner: Stone)
// 新增（挑战 AI 模式使用）
fun showVictory(winner: Stone, userStone: Stone)
```

**语义**:

- 双参重载按玩家视角呈现: `winner == userStone` 时显示"你赢了!"，否则显示"你输了!"。
- 颜色样式沿用既有胜负文案的红色系。
- 和局沿用既有 `showDraw()`（"和局!"），不区分模式。

## 契约 5: UiConfig 新增配置项（新增）

**位置**: `ui/window/UiConfig.kt` + `ui/window/DefaultUiConfig.kt`

```kotlin
fun getAiDropDelay(): Duration              // AI 落子延迟，默认 500ms
```

**语义**: 玩家落子后与执白开局后调度 AI 前的等待时长；实现方 `MainWindow` 读取后构建单次 Swing Timer。
