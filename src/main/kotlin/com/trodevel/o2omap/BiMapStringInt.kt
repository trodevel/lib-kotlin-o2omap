package com.trodevel.o2omap

import com.trodevel.generickeyvalueregistry.StringCodec
import java.io.File
import java.io.IOException

class BiMapStringInt(
    private val idGenerator: IUniqueIdGenerator,
    private val storageFile: File
) {
    private val stringToInt = mutableMapOf<String, Int>()
    private val intToString = mutableMapOf<Int, String>()

    private data class Record(val originalId: String, val intId: Int)

    init {
        load()
    }

    fun findIdOrAdd(originalId: String): Int {
        stringToInt[originalId]?.let { return it }
        
        val newId = idGenerator.getNextId()
        stringToInt[originalId] = newId
        intToString[newId] = originalId
        return newId
    }

    fun findOriginalId(intId: Int): String {
        return intToString[intId] ?: throw NoSuchElementException("ID $intId not found")
    }

    private fun add(record: Record) {
        stringToInt[record.originalId] = record.intId
        intToString[record.intId] = record.originalId
    }

    private fun toRecord(line: String): Record {
        val parts = line.split(" ")
        if (parts.size != 2) throw IOException("Invalid record format: $line")
        val originalId = StringCodec.decode(parts[0])
        val intId = parts[1].toInt()
        return Record(originalId, intId)
    }

    private fun load() {
        if (!storageFile.exists()) return
        try {
            val lines = storageFile.readLines()
            if (lines.isEmpty()) return
            
            if (lines[0] != "O2OMAP") {
                throw IOException("Invalid file format: Missing O2OMAP header")
            }
            if (lines.size < 3) return
            
            val version = lines[1].toInt()
            val size = lines[2].toInt()
            
            for (i in 0 until size) {
                if (i + 3 >= lines.size) break
                val line = lines[i + 3]
                try {
                    val record = toRecord(line)
                    add(record)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            validate()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun save() {
        try {
            storageFile.printWriter().use { out ->
                out.println("O2OMAP")
                out.println("1") // version
                out.println(stringToInt.size)
                stringToInt.forEach { (s, i) ->
                    out.println("${StringCodec.encode(s)} $i")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun validate() {
        if (stringToInt.size != intToString.size) {
            throw IllegalStateException("Mapping is not one-to-one: stringToInt size (${stringToInt.size}) != intToString size (${intToString.size})")
        }
        
        stringToInt.forEach { (s, i) ->
            if (intToString[i] != s) {
                throw IllegalStateException("Mapping mismatch for $s <-> $i")
            }
        }
    }
}
