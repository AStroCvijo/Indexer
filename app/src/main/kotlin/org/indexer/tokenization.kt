package org.indexer

import java.io.File

// Tokenize the text and return a list of TokenInfo objects
fun tokenize(code: String, path: String, startIndex: Int): Pair<List<TokenInfo>, Int> {
    // Delimiters for tokenization
    val delimiters = Regex("\\s+|[{}()\\].,;+\\-/*%!=<>|&^~]+\n")
    val tokens = mutableListOf<TokenInfo>()
    var index = 0

    // Tokenize
    delimiters.findAll(code).forEach { matchResult ->
        val dIndex = matchResult.range.first
        if (dIndex > index) {
            val token = code.substring(index, dIndex)
            for (i in token.indices) {
                for (j in i + 1..token.length) {
                    tokens.add(
                        TokenInfo(
                            token.substring(i, j),
                            path,
                            startIndex + index + i,
                            token.substring(i, j).hashCode().toString()
                        )
                    )
                }
            }
        }
        index = dIndex + 1
    }

    return Pair(tokens, startIndex + code.length)
}

// Loop over all the files in the folder
fun loopFilesAndSubfolders(folder: File, trie: Trie, allowedExtensions: List<String>) {
    folder.listFiles()?.forEach { file ->
        if (file.isDirectory) {
            loopFilesAndSubfolders(file, trie, allowedExtensions)
        } else {
            // If the extension is allowed, convert file to string and pass it to tokenize function
            val fileExtension = file.extension
            if (allowedExtensions.contains(fileExtension)) {
                val text = file.readText()
                val (tokens, _) = tokenize("$text ", file.path, 0)
                tokens.forEach { tokenInfo ->
                    insertToken(tokenInfo, trie)
                    trie.insert(tokenInfo.t)
                }
            }
        }
    }
}