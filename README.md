# Code Similarity Comparator

A lightweight Java Swing desktop application for quickly comparing the similarity between two Java source files.

The comparison is based on:
1. Java keyword frequency statistics
2. User-defined identifier frequency statistics (non-keyword tokens starting with alphabetic characters)
3. Euclidean distance transformed into a bounded similarity score `sim = 1 / (1 + distance)`

The UI allows you to:
- Select two `.java` files
- View per-file keyword & identifier frequency lists (sortable ascending/descending)
- Compute similarity scores and an interpreted conclusion
- Export raw statistics and similarity results to text files

## Features
- Custom lightweight hash map implementation (teaching purpose)
- Identifier & keyword separation
- Normalized similarity values in (0,1]
- Exportable reports (`statistics.txt`, `similarity.txt`)
- GUI beautified with consistent colors and fonts
- Frequency sorting toggle (ascending / descending)

## Screenshots
> (You can add screenshots in the `pictures/` folder and link them here.)

## Project Structure
```
src/
  entity/
    CodeHashMap.java     # Chained-hash based symbol frequency map
    SymbolTable.java     # Single linked-list symbol table (bucket)
    Node.java            # List node (key, count)
  gui/
    StartFrame.java      # Entry screen
    MainFrame.java       # Main comparison & export UI
    InstructionFrame.java# Help / usage dialog
```

## Build & Run
### Prerequisites
- JDK 8+ (JDK 17 recommended)

### Compile
```bash
javac -encoding UTF-8 -d bin src/entity/*.java src/gui/*.java
```

### Run
```bash
java -cp bin gui.StartFrame
```

## Similarity Metric
For each category (keywords / identifiers):
1. Build frequency vectors over the union set of terms.
2. Compute squared difference sum: `S = Σ (f1 - f2)^2`.
3. Let `distance = sqrt(S)`.
4. Transform to similarity: `similarity = 1 / (1 + distance)`.

This keeps the score within `(0,1]` where 1 means identical frequency distribution.

## Exported Files
- `statistics.txt`: Frequency listings for keywords & identifiers per file.
- `similarity.txt`: Similarity scores and qualitative conclusion.

## Qualitative Conclusion Rules (current heuristic)
| Condition | Conclusion |
|-----------|------------|
| s1 == 1 and s2 == 1 | 判定为同一文件 |
| s1 == 1 and s2 != 1 | 关键字完全一致，高度相似 |
| s1 > 0.5 or s2 > 0.1 | 整体高度相似 |
| s1 > 0.2 or s2 > 0.05 | 部分结构相似 |
| otherwise | 相似度较低 |

## Possible Improvements
- Switch custom hash map to `HashMap<String,Integer>` (production readiness)
- Add cosine similarity / TF-IDF weighting
- Filter comments & string literals with a lexer / JavaParser
- AST-based structural hashing or fingerprinting (Winnowing, Rabin-Karp)
- Detect renamed identifiers via alpha-normalization
- Highlight overlapping suspicious segments
- Export JSON / CSV
- Batch compare (similarity matrix)

## .gitignore
A suggested `.gitignore` is included to exclude build and IDE artifacts.

## Contributing
Feel free to fork and extend with more robust plagiarism detection algorithms or better visualization.

## License
Add a license of your choice (e.g., MIT) as `LICENSE` if you plan to open source.

## GitHub Push Instructions
If you have not created a repository yet:
1. Create a new empty repo on GitHub (e.g., `CodeSimilarityComparator`). Do **not** add a README (we already have one).
2. Run the following commands in the project root:
```bash
git init
git add .
git commit -m "Initial commit: Code Similarity Comparator"
git branch -M main
git remote add origin https://github.com/<your-username>/CodeSimilarityComparator.git
git push -u origin main
```
If you already initialized and want to change remote:
```bash
git remote set-url origin https://github.com/<your-username>/CodeSimilarityComparator.git
git push -u origin main
```

## Disclaimer
This tool provides heuristic similarity guidance only. For academic integrity or legal plagiarism investigations, always combine with manual code review and more robust structural analysis.
