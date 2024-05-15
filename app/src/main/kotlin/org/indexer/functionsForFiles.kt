package org.indexer

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter

// Function to read allowed extensions from allowedExtensions.txt
fun readAllowedExtensionsFromFile(): List<String> {
    val allowedExtensionsFile = File(".\\..\\allowedExtensions.txt")
    if (!allowedExtensionsFile.exists()) {
        println("allowedExtensions.txt not found.")
        return emptyList()
    }

    return try {
        allowedExtensionsFile.readLines().map { it.trim() }
    } catch (e: Exception) {
        println("Error reading allowedExtensions.txt: ${e.message}")
        emptyList()
    }
}

// Function to save tokenMap to a JSON file
fun saveTokenMapToJson(path: String) {
    val gson = GsonBuilder().setPrettyPrinting().create()

    try {
        FileWriter(path).use { fileWriter ->
            JsonWriter(fileWriter).use { jsonWriter ->
                jsonWriter.beginObject()

                // Iterate over the tokenMap
                tokenMap.forEach { (hashValue, tokenInfoList) ->
                    jsonWriter.name(hashValue).beginArray()

                    // Write TokenInfo as a JSON
                    tokenInfoList.forEach { tokenInfo ->
                        gson.toJson(tokenInfo, TokenInfo::class.java, jsonWriter)
                    }

                    jsonWriter.endArray()
                }

                jsonWriter.endObject()
            }
        }
        println("Token map saved to $path successfully.")
    } catch (e: Exception) {
        println("Error occurred while saving token map to $path: ${e.message}")
    }
}

// Function to load tokenMap data from a JSON file
fun loadTokenMapFromJson(path: String): HashMap<String, MutableList<TokenInfo>> {
    val gson = Gson()
    val file = File(path)

    return try {
        val tokenMap = HashMap<String, MutableList<TokenInfo>>()

        FileReader(file).use { fileReader ->
            JsonReader(fileReader).use { jsonReader ->
                jsonReader.beginObject()

                // Read TokenInfo objects
                while (jsonReader.hasNext()) {
                    val hashValue = jsonReader.nextName()
                    val typeToken = object : TypeToken<List<TokenInfo>>() {}.type
                    val tokenInfoList: List<TokenInfo> = gson.fromJson(jsonReader, typeToken)
                    tokenMap[hashValue] = tokenInfoList.toMutableList()
                }

                jsonReader.endObject()
            }
        }
        tokenMap
    } catch (e: Exception) {
        println("Error occurred while loading token map from $path: ${e.message}")
        hashMapOf()
    }
}
