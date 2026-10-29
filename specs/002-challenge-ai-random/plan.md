# 实施计划：人机对战模式（随机策略 AI）

**分支**: `002-challenge-ai-random` | **日期**: 2026-10-28 | **规格**: [spec.md](spec.md)
**输入**: 来自 `/specs/002-challenge-ai-random/spec.md` 的功能规格

## 概要

为五子棋桌面应用新增人机对战模式：玩家在"挑战 AI"入口选择禁手规则与执子颜色（默认黑棋），AI 以纯随机合法策略对弈（约 500ms 延迟落子），支持执白后行、悔棋撤两步、终局玩家视角文案，且不影响既有研学模式。技术方案沿用现有三层架构（model/game/ui）与 Swing Timer 调度模型，新增 game 层 `AiPlayer` 抽象与随机实现。

## 技术上下文

**语言/版本**: Kotlin 1.6 / JVM（JDK 8+ 字节码，运行于 JDK 21）
**主要依赖**: OpenJFX（窗口/渲染）、AWT/Swing（`javax.swing.Timer` 动画与调度）
**存储**: N/A（纯内存对局状态）
**测试**: JUnit 5 + Mockito-Kotlin（`src/test/kotlin/`，`*Test.kt` 命名）
**目标平台**: 桌面 JVM（macOS/Windows/Linux）
**项目类型**: single（单模块桌面应用）
**性能目标**: AI 落子在玩家落子后约 500ms 内出现；动画维持现有帧率表现
**约束**: 全部交互在 EDT 单线程模型下调度，不引入多线程；AI 随机落子不得命中禁手点或已占位
**规模/范围**: 单窗口桌面应用；本功能新增约 370 行代码（1 个接口、1 个实现、覆盖层扩展、主窗口调度）

## Constitution 检查

*门禁: Phase 0 研究前必须通过，Phase 1 设计后复查。*

`.specify/memory/constitution.md` 为未填充的模板占位符，无生效的项目原则与门禁约束。本计划以仓库 `AGENTS.md` 的既定规范作为事实约束：沟通与文档中文、代码注释英文、新增/修改代码须配套单元测试并编译运行、Conventional Commits、Kotlin 禁用完全限定包名。**门禁状态: 通过（无违规）。**

## 项目结构

### 文档（本功能）

```text
specs/002-challenge-ai-random/
├── plan.md              # 本文件（/speckit.plan 输出）
├── research.md          # Phase 0 输出（/speckit.plan）
├── data-model.md        # Phase 1 输出（/speckit.plan）
├── quickstart.md        # Phase 1 输出（/speckit.plan）
├── contracts/           # Phase 1 输出（/speckit.plan）
│   └── ai-player-and-ui-events.md
└── tasks.md             # Phase 2 输出（/speckit.tasks，非本命令创建）
```

### 源码（仓库根）

```text
src/main/kotlin/org/bymc/gomoku/
├── game/
│   ├── abstraction/
│   │   ├── Game.kt                              # 既有，不变
│   │   └── AiPlayer.kt                          # 新增: AI 玩家接口
│   └── impl/
│       ├── GameImpl.kt                          # 既有，不变
│       └── RandomAiPlayer.kt                    # 新增: 随机策略实现
├── model/                                        # 既有，本功能不改动
└── ui/
    ├── animation/                                # 既有，本功能不改动
    ├── view/
    │   ├── RuleSelectionOverlayView.kt           # 扩展: 执子选择行
    │   ├── RuleSelectionOverlayViewEventListener.kt # 扩展: onStartClicked 增参
    │   ├── SidePanelView.kt                      # 扩展: 玩家视角胜负文案
    │   └── BoardIntegrationView.kt               # 既有，复用禁用/启用
    └── window/
        ├── MainWindow.kt                         # 扩展: 挑战 AI 入口、AI 调度、悔棋语义
        ├── UiConfig.kt                           # 扩展: AI 延迟等新配置项
        └── DefaultUiConfig.kt                    # 扩展: 配置默认值

src/test/kotlin/org/bymc/gomoku/game/impl/
    └── RandomAiPlayerTest.kt                     # 新增: 随机策略单元测试
```

**结构决策**: 沿用单模块三层结构。`AiPlayer` 归属 game 层（依赖 model 层的 `BoardViewModel`/`Rule`/`Stone`，与 `Game` 同级且需单元测试）；UI 层仅做编排，不引入新模块。

## 复杂度追踪

> **仅在 Constitution 检查存在需辩护的违规时填写**

无违规，本表为空。
