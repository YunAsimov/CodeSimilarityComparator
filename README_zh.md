# 代码相似度比较工具

一个轻量级 Java Swing 桌面应用，用于快速比较两个 Java 源文件之间的“相似度”。

## 核心思路
通过统计两份代码中：
1. Java 关键字出现频次
2. 用户自定义标识符出现频次（非关键字、以字母开头的 token）
3. 基于频次差的欧氏距离，并用 `sim = 1 / (1 + distance)` 归一化为 (0,1] 范围的相似度

## 功能特性
- 关键字 / 标识符频率统计
- 自定义链地址哈希表（教学示例）
- 用户标识符与关键字区分
- 相似度值稳定在 (0,1]，相同频率分布为 1
- 结果可导出（statistics.txt / similarity.txt）
- GUI 配色优化、统一字体
- 频次结果支持升序 / 降序切换

## 界面说明
- 选择文件1 / 文件2：依次加载两个待比较的 .java 文件
- 左右两侧显示：各自关键字频率 + 用户标识符频率
- 开始对比：计算两个类别的相似度并生成定性结论
- 导出统计：输出两文件的关键字与标识符频率
- 导出相似度：输出两个相似度值 + 结论
- 切换排序：切换显示频率列表的升序 / 降序

## 项目结构
```
src/
  entity/
    CodeHashMap.java     # 链地址哈希表(含关键字预置)
    SymbolTable.java     # 单链表符号表
    Node.java            # 节点(key, count)
  gui/
    StartFrame.java      # 启动界面
    MainFrame.java       # 主功能界面（选择/统计/导出/对比）
    InstructionFrame.java# 使用说明窗口
```

## 构建与运行
### 环境要求
- JDK 8 及以上（推荐 JDK 17）

### 编译
```bash
javac -encoding UTF-8 -d bin src/entity/*.java src/gui/*.java
```

### 运行
```bash
java -cp bin gui.StartFrame
```

## 相似度计算公式
对关键字与标识符各自：
1. 统计每个词频 f1, f2
2. 计算平方差和：`S = Σ (f1 - f2)^2`
3. 取距离：`distance = sqrt(S)`
4. 相似度：`similarity = 1 / (1 + distance)`

解释：差异越小 distance 越接近 0，相似度越接近 1。

## 相似度结论规则（启发式）
| 条件 | 结论 |
|------|------|
| s1 == 1 且 s2 == 1 | 判定为同一文件 |
| s1 == 1 且 s2 != 1 | 关键字完全一致，高度相似 |
| s1 > 0.5 或 s2 > 0.1 | 整体高度相似 |
| s1 > 0.2 或 s2 > 0.05 | 部分结构相似 |
| 其它 | 相似度较低 |

> s1：关键字相似度；s2：用户标识符相似度。

## 导出文件说明
| 文件 | 内容 |
|------|------|
| statistics.txt | 两文件关键字 / 标识符频率（按当前排序） |
| similarity.txt | 两个相似度值 + 结论 |

## 改进方向建议
- 使用标准 `HashMap` 替换自定义结构减少重复代码
- 过滤注释与字符串字面量（词法分析或 JavaParser）
- 引入余弦相似度 / TF-IDF / 词频归一化
- 指纹算法：Rabin-Karp / Winnowing
- 基于 AST 的结构相似（树哈希、编辑距离）
- 变量重命名规避检测（α-重命名统一化）
- 导出 JSON / CSV / HTML 报告
- 批量比对（生成相似度矩阵）
- 高亮可疑片段范围

## 免责声明
本工具输出仅为启发式参考，不作为学术诚信或法律判定的唯一依据。请结合人工审查与更专业的分析。

## 贡献
欢迎提交 Issue / PR，或 Fork 后扩展更高级的检测算法与可视化功能。

## License
如需开源，请在根目录添加 LICENSE（推荐 MIT）。

## Git 推送示例
```
git init
git add .
git commit -m "Init"
git branch -M main
git remote add origin https://github.com/<用户名>/CodeSimilarityComparator.git
git push -u origin main
```

英文版请见：README.md
