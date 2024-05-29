
# File Indexer - CLI tool

A CLI tool that indexes a given folder and allows efficient searching for string occurrences. <br /><br />
Date of creation: May, 2024 <br/>
## Instructions

1. Clone the repository `git clone https://github.com/AStroCvijo/Indexer`
2. Make sure you have [gradle](https://gradle.org/install/) installed on your machine
3. Add Gradle to the PATH variable
4. Use the command `gradle build` to build the app
5. Set up `allowedExtensions.txt` file in the root directory e.g.
```txt

  txt
  kt
  py

```
6. Use the command `gradlew run --args=""` to run the app


## Arguments guide 

`-path or -p` followed by path to the folder you want to index e.g. `.\\Data`
This will index the `.\\Data` folder and save it to `.\\indexedFolders` (the starting location of .\\\ is inside the app folder, so either move to folder you want to index there or navigate to it)

`-force or -f`
This will force the app to index the folder again and save it to `.\\indexedFolders` (if you don't force a re-index, and the folder has already been indexed, the app will just load it)

`-query or -q` followed by the string you want to search for e.g. `print`
This will print all the occurrences of `print` in the given folder

`-case or -c`
This will indicate that you want the search to be case-sensitive

`-highlight or -h`
This will print the line in which the string is located and highlight it

## Examples

| Command | Description |
|---------|-------------|
| `gradlew run --args="-p .\\Data"` | Index a Folder Without Re-indexing or Querying |
| `gradlew run --args="-p .\\Data -f"` | Index a Folder and Force Re-indexing |
| `gradlew run --args="-p .\\Data -q print"` | Index a Folder and Query Without Case Sensitivity |
| `gradlew run --args="-p .\\Data -q print -h"` | Index a Folder, Query Without Case Sensitivity and Highlight|
| `gradlew run --args="-p .\\Data -f -q print"` | Index a Folder, Force Re-indexing, and Query Without Case Sensitivity |
| `gradlew run --args="-p .\\Data -q print -c"` | Index a Folder, Query with Case Sensitivity |
| `gradlew run --args="-p .\\Data -f -q print -c"` | Index a Folder, Force Re-indexing, and Query with Case Sensitivity |
| `gradlew run --args="-p .\\Documents -q print"` | Index a Different Folder and Query Without Case Sensitivity |
| `gradlew run --args="-p .\\AnotherFolder -f -q print -c"` | Index a Folder, Force Re-indexing, and Perform Case-sensitive Search |

