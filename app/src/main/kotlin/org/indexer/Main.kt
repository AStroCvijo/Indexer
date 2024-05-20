package org.indexer

import java.io.File
import org.fusesource.jansi.Ansi
import org.fusesource.jansi.AnsiConsole
import org.fusesource.jansi.Ansi.Color

// Define the TokenInfo class
data class TokenInfo(
    val t: String,  // token
    val fp: String, // path
    val p: Int,     // position
    val h: String,  // hash
    val l: Int      // line number
)

// HashMap to store hashed tokens
var tokenMap = HashMap<String, MutableList<TokenInfo>>()

// Master function for inserting tokens
fun insertToken(tokenInfo: TokenInfo) {

    // Insert into the HashMap
    if (!tokenMap.containsKey(tokenInfo.h)) {
        tokenMap[tokenInfo.h] = mutableListOf()
    }
    tokenMap[tokenInfo.h]?.add(tokenInfo)
}

// Main function
fun main(args: Array<String>) {
    AnsiConsole.systemInstall()
    println("RUN!")

    // No arguments provided
    if (args.isEmpty()) {
        println("Please provide a command-line argument")
        return
    }

    // Initialize arguments
    var folderPath = ""
    var force = false
    var query = false
    var searchString = ""
    var case = false
    var highlight = false

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
        if(args[i] == "-c" || args[i] == "-case"){
            case = true
        }
        if (args[i] == "-h" || args[i] == "-highlight") {
            highlight = true
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
        if (query && searchString.isEmpty()) {
            println("Query string not provided.")
            return
        }

        // File path for the tokenMap.json
        var tokenMapFilePath = ".\\indexedFolders\\" + folderPath + "_tokenMap.json"
        if(case){
            tokenMapFilePath = ".\\indexedFolders\\" + folderPath + "CASE_tokenMap.json"
        }

        // Logic if force = false
        if(!force){
            // Check if they already exist
            if (File(tokenMapFilePath).exists()) {

                if(query){
                    tokenMap = loadTokenMapFromJson(tokenMapFilePath)
                    if (tokenMap.isNotEmpty()) {
                        println("tokenMap loaded from $tokenMapFilePath successfully.")
                    }
                }
                else{
                    println("tokenMap for $tokenMapFilePath already exists.")
                }

            } else {
                // Loops over the folder and its subfolders and tokenizes the words in files
                val allowedExtensions = readAllowedExtensionsFromFile()
                loopFilesAndSubfolders(folder, allowedExtensions, case)

                // Save tokenMap to JSON file
                saveTokenMapToJson(tokenMapFilePath)

            }

            // If query is true print all the occurrences of searchString
            if(query){
                // Retrieve token information from the HashMap
                var tokenInfoList = tokenMap[searchString.lowercase().hashCode().toString()]
                if(case){
                    tokenInfoList = tokenMap[searchString.hashCode().toString()]
                }

                // Print search results
                if (tokenInfoList != null) {
                    if (tokenInfoList.isNotEmpty()) {
                        println("Search Results:")
                        tokenInfoList.forEach { tokenInfo ->
                            println("String '$searchString' found in '${tokenInfo.fp}', line ${tokenInfo.l}, position ${tokenInfo.p}")
                            if (highlight){
                                val file = File(tokenInfo.fp)
                                var line = file.readText()
                                var lineStart = tokenInfo.p
                                var lineEnd = tokenInfo.p
                                while (line[lineStart] != '\n'){
                                    lineStart -= 1
                                }
                                lineStart += 1
                                while (line[lineStart] == ' '){
                                    lineStart += 1
                                }
                                while (line[lineEnd]!='\n'){
                                    lineEnd += 1
                                }
                                line = line.substring(lineStart, lineEnd)
                                val start = line.substring(0, tokenInfo.p - lineStart)
                                var token = line.substring(tokenInfo.p - lineStart, tokenInfo.p - lineStart + searchString.length)
                                token = Ansi.ansi().fg(Color.GREEN).a(token).reset().toString()
                                val end = line.substring(tokenInfo.p - lineStart + searchString.length, line.length)
                                println("$start$token$end\n")
                            }
                        }
                        println("Found ${tokenInfoList.size} occurrences in the indexed folder")
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
            loopFilesAndSubfolders(folder, allowedExtensions, case)

            // Save tokenMap to JSON file
            saveTokenMapToJson(tokenMapFilePath)

            // If query is true print all the occurrences of searchString
            if(query){
                // Retrieve token information from the HashMap
                var tokenInfoList = tokenMap[searchString.lowercase().hashCode().toString()]
                if(case){
                    tokenInfoList = tokenMap[searchString.hashCode().toString()]
                }

                // Print search results
                if (tokenInfoList != null) {
                    if (tokenInfoList.isNotEmpty()) {
                        println("Search Results:")
                        tokenInfoList.forEach { tokenInfo ->
                            println("String '$searchString' found in '${tokenInfo.fp}', line ${tokenInfo.l}, position ${tokenInfo.p}")
                            if (highlight){
                                val file = File(tokenInfo.fp)
                                var line = file.readText()
                                var lineStart = tokenInfo.p
                                var lineEnd = tokenInfo.p
                                while (line[lineStart] != '\n'){
                                    lineStart -= 1
                                }
                                lineStart += 1
                                while (line[lineStart] == ' '){
                                    lineStart += 1
                                }
                                while (line[lineEnd]!='\n'){
                                    lineEnd += 1
                                }
                                line = line.substring(lineStart, lineEnd)
                                val start = line.substring(0, tokenInfo.p - lineStart)
                                var token = line.substring(tokenInfo.p - lineStart, tokenInfo.p - lineStart + searchString.length)
                                token = Ansi.ansi().fg(Color.GREEN).a(token).reset().toString()
                                val end = line.substring(tokenInfo.p - lineStart + searchString.length, line.length)
                                println("$start$token$end\n")
                            }
                        }
                        println("Found ${tokenInfoList.size} occurrences in the indexed folder")
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
