package org.indexer

import java.io.File

// HashMap to store hashed tokens
var tokenMap = HashMap<String, MutableList<TokenInfo>>()

// Master function for inserting tokens
fun insertToken(tokenInfo: TokenInfo, trie: Trie) {
    // Insert into the Trie
    trie.insert(tokenInfo.t)

    // Insert into the HashMap
    if (!tokenMap.containsKey(tokenInfo.h)) {
        tokenMap[tokenInfo.h] = mutableListOf()
    }
    tokenMap[tokenInfo.h]?.add(tokenInfo)
}

// Main function
fun main(args: Array<String>) {

    println("RUN!")

    // No arguments provided
    if (args.isEmpty()) {
        println("Please provide a command-line argument")
        return
    }

    // Create an instance of the trie
    val trie = Trie()

    // Initialize arguments
    var folderPath = ""
    var force = false
    var query = false
    var searchString = ""

    // Parse arguments
    for (i in args.indices){
        if ((args[i] == "-p" || args[i] == "-path") && i+1<args.size){
            folderPath = args[i+1]
        }
        if(args[i] == "-f" || args[i] == "-force"){
            force = true
        }
        if ((args[i] == "-q" || args[i] == "-query") && i+1<args.size){
            query = true
            searchString = args[i+1]
        }
    }

    // Execution logic based on parsed arguments
    if(folderPath != ""){
        // File object representing the folder
        val folder = File(folderPath)

        // Check if the folder exists
        if (!folder.exists() || !folder.isDirectory) {
            println("Invalid folder path or folder does not exist.")
            return
        }

        // Check if the query string exists
        if (searchString.isEmpty()) {
            println("Query string not provided.")
            return
        }

        // File paths for the tokenMap.json and trie.json
        val tokenMapFilePath = ".\\indexedFolders\\" + folderPath + "_tokenMap.json"
        val trieFilePath = ".\\indexedFolders\\" + folderPath + "_trie.json"

        // Logic if force = false
        if(!force){
            // Check if they already exist
            if (File(tokenMapFilePath).exists() && File(trieFilePath).exists()) {

                if(query){
                    tokenMap = loadTokenMapFromJson(tokenMapFilePath)
                    if (tokenMap.isNotEmpty()) {
                        println("tokenMap loaded from $tokenMapFilePath successfully.")
                    }
                    trie.root = loadTrieFromJson(trieFilePath).root
                    if (trie.root.child.isNotEmpty()) {
                        println("trie loaded from $trieFilePath successfully.")
                    }
                }
                else{
                    println("tokenMap for $tokenMapFilePath already exists.")
                    println("trie for $trieFilePath already exists.")
                }

            } else {
                // Loops over the folder and its subfolders and tokenizes the words in files
                val allowedExtensions = readAllowedExtensionsFromFile()
                loopFilesAndSubfolders(folder, trie, allowedExtensions)

                // Save tokenMap to JSON file
                saveTokenMapToJson(tokenMapFilePath)

                // Save trie to JSON file
                saveTrieToJson(trie.root, trieFilePath)
            }

            // Starts prompting the user for query's
            if(query){
                // Search using the trie structure
                val searchResults = trie.searchSubstrings(searchString)

                // Print search results
                if (searchResults.isNotEmpty()) {
                    println("Search Results:")
                    searchResults.forEach { token ->
                        // Retrieve token information from the HashMap
                        val tokenInfoList = tokenMap[token.hashCode().toString()]
                        tokenInfoList?.forEach { tokenInfo ->
                            println("String '$token' found in file '${tokenInfo.fp}' at position ${tokenInfo.p}")
                        }
                        if (tokenInfoList != null) {
                            println("Found ${tokenInfoList.size} occurrences in the indexed folder")
                        }
                    }
                } else {
                    println("No matches found for '$searchString'.")
                }
            }
            else{
                return
            }
        }
        else{
            // Loops over the folder and its subfolders and tokenizes the words in files
            val allowedExtensions = readAllowedExtensionsFromFile()
            loopFilesAndSubfolders(folder, trie, allowedExtensions)

            // Save tokenMap to JSON file
            saveTokenMapToJson(tokenMapFilePath)

            // Save trie to JSON file
            saveTrieToJson(trie.root, trieFilePath)

            // Starts prompting the user for query's
            if(query){
                // Search using the trie structure
                val searchResults = trie.searchSubstrings(searchString)

                // Print search results
                if (searchResults.isNotEmpty()) {
                    println("Search Results:")
                    searchResults.forEach { token ->
                        // Retrieve token information from the HashMap
                        val tokenInfoList = tokenMap[token.hashCode().toString()]
                        tokenInfoList?.forEach { tokenInfo ->
                                println("String '$token' found in file '${tokenInfo.fp}' at position ${tokenInfo.p}")
                        }
                        if (tokenInfoList != null) {
                            println("Found ${tokenInfoList.size} occurrences in the indexed folder")
                        }
                    }
                } else {
                    println("No matches found for '$searchString'.")
                }
            }
            else{
                return
            }
        }
    }
    else{
        println("No folder path argument!")
    }
}
