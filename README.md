
# File Indexer - CLI tool

A CLI tool that indexes a given folder and allows efficient searching for string occurrences.

## Instructions

1. Download the repository
2. Open terminal and navigate to the repository
3. Make sure to have [gradle](https://gradle.org/install/) installed on your machine
4. Use the command `gradle build` to build the app
5. Use the command `gradlew run --args=""` to run the app
6. Setup `allowedExtensions.txt` file int the root directory
   e.g.
   `txt` <br>
   `kt` <br>
   `py`

### Arguments guide 

`-path or -p` followed by path to the folder you want to index e.g. `.\\Data`
This will index the `.\\Data` folder and save it to `.\\indexedFolders` (the starting location of .\\\ is inside the app folder, so either move to folder you want to index there or navigate to it)

`-force or -f`
This will force the app to index the folder again and save it to `.\\indexedFolders` (if you don't force a re-index, and the folder has already been indexed, the app will just load it)

`-query or -q` followed by the string you want to search for e.g. `print`
This will print all the occurrences of `print` in the given folder

### Example usage
`gradlew run --args="-p .\\Data -f -q print"`
This will index `.\\Data folder` and print all the occurrences of `print`

