package org.indexer

// Define the TokenInfo class
data class TokenInfo(
    val t: String, // token
    val fp: String, // path
    val p: Int,    // position
    val h: String  //hash
)

// Trie Node class
class TrieNode {
    val child = HashMap<Char, TrieNode>()
    var end: Boolean = false
}

// Trie class
class Trie {
    var root = TrieNode()

    // Insert token into trie
    fun insert(token: String) {
        var node = root
        for (char in token) {
            if (!node.child.containsKey(char)) {
                node.child[char] = TrieNode()
            }
            node = node.child[char]!!
        }
        node.end = true
    }

    // Search the trie for a query
    fun searchSubstrings(query: String): List<String> {
        val results = mutableListOf<String>()
        var node = root

        for (char in query) {
            if (!node.child.containsKey(char)) {
                return emptyList()
            }
            node = node.child[char]!!
        }

        if (node.end) {
            results.add(query)
        }

        return results
    }
}