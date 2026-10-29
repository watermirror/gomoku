# AGENTS.md

This file provides guidance to AI coding agents when working with code in this repository.

## 沟通

- 始终使用中文与用户沟通，代码注释始终使用英文。

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

The codebase has four major layers:

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

### Algo Layer (`algo/`)
Drop-decision algorithms on top of Model, fully decoupled from game flow. `GomokuAlgorithm` is the pluggable interface; `RandomGomokuAlgorithm` (uniform random legal move) is the current implementation. The UI layer instantiates and drives it.

### Game Layer (`game/`)
Turn-based game flow built on top of Model. `GameImpl` manages actor turns, move validation, and game situation tracking. Instantiated via `GameFactory`.

### UI Layer (`ui/` and `uifx/`)
- `uifx/` — Custom UI framework on top of JavaFX/AWT: view hierarchy, event system, animation engine with easing functions, hit testing, canvas rendering
- `ui/` — Application-specific views: `MainWindow`, `BoardIntegrationView` (board rendering), `BoardSensorView` (input), `TitlePanelView`, `CoveringLayerView`

## Testing

- **Framework**: JUnit 5 + Mockito-Kotlin
- **Test location**: `src/test/kotlin/` with `*Test.kt` naming
- **Helper**: `TestBoardViewModel` for setting up board states in tests
- 新增或修改代码时必须新增或更新对应单元测试。
- 修改代码或单元测试后必须编译并运行测试。

## Kotlin

- Kotlin 代码不得使用完全限定包名；类型名冲突时使用 `import ... as ...`。

## Markdown 文档

- Markdown 段落不得硬换行；每个段落使用单行，由渲染器处理换行。
- 120 字符限制只适用于源代码，不适用于 Markdown 正文。
- 仅在列表、标题、代码块、表格行或显式 `<br>` 等具有语义的位置换行。

## 提交信息

- 使用 Conventional Commits：新功能用 `feat:`/`feat(scope):`，修复用 `fix:`/`fix(scope):`，重构用 `refactor:`/`refactor(scope):`，测试用 `test:`/`test(scope):`，文档用 `docs:`。
- 提交信息不得包含 AI 或 agent 归因。

## Git 推送

- 未经用户明确要求不得执行 `git push`。
- 用户要求推送时仅执行普通 push；除非用户明确要求，禁止使用 `--force` 或 `-f`。

## Speckit

- 所有 Speckit 生成文件，包括 `spec.md`、`plan.md` 和 `tasks.md`，必须使用中文。

## 文档分类

- `docs/` 下的文档按类型分类，每类一个子目录，文件名使用 `<type>_YYYYMMDD_HHMMSS_<topic>.md` 格式；新建文档时必须用 `date +%Y%m%d_%H%M%S` 获取当前时间戳。
- `docs/issues/` 例外，使用 `issue_YYYYMMDD_<topic>.md` 格式（仅日期，无时间）。

| 类型 | 目录 | 文件名前缀 | 用途 |
| --- | --- | --- | --- |
| Design | `docs/design/` | `design_` | 功能详细设计方案。 |
| Draft | `docs/draft/` | 无固定前缀 | 草稿与临时分析，成熟后迁入其他分类。 |
| Guide | `docs/guides/` | `guide_` | 面向使用者的操作指南。 |
| Issue | `docs/issues/` | `issue_YYYYMMDD_` | 已知缺陷或待解决的开放问题。 |
| Misc | `docs/misc/` | `misc_` | 概览、分享等不便归入其他类型的杂项文档。 |
| Proposal | `docs/proposals/` | `proposal_` | 推动决策的设计提案。 |
| Protocol | `docs/protocols/` | `protocol_` | 与外部系统的协议/接口文档。 |
| Report | `docs/reports/` | `report_` | 针对某次运行、实验或发现的事后分析。 |
| Whitepaper | `docs/whitepapers/` | `whitepaper_` | 解释既有子系统工作原理的深度技术背景。 |

- 按文档的主要角色选择分类；在 proposal 与 whitepaper 之间拿不准时：proposal 驱动决策，whitepaper 解释既有设计。

## Active Technologies
- Kotlin 1.6 / JVM + JavaFX (OpenJFX), AWT/Swing (for animation timers and Canvas) (001-self-study-mode)
- N/A (in-memory game state only) (001-self-study-mode)

## Recent Changes
- 001-self-study-mode: Added Kotlin 1.6 / JVM + JavaFX (OpenJFX), AWT/Swing (for animation timers and Canvas)
