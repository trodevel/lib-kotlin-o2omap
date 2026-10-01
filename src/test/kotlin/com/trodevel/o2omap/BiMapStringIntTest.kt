package com.trodevel.o2omap

import com.trodevel.uniqueidgenerator.IUniqueIdGenerator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class BiMapStringIntTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private class TestIdGenerator(private var startId: Int = 1) : IUniqueIdGenerator {
        override fun getNextId(): Int = startId++
    }

    @Test
    fun testAddAndFind() {
        val file = tempFolder.newFile("test.map")
        val generator = TestIdGenerator(10)
        val biMap = BiMapStringInt(generator, file)

        val id1 = biMap.findIdOrAdd("key1")
        assertEquals(10, id1)

        val id2 = biMap.findIdOrAdd("key2")
        assertEquals(11, id2)

        // Existing key returns existing id
        assertEquals(10, biMap.findIdOrAdd("key1"))

        assertEquals(10, biMap.findId("key1"))
        assertEquals("key1", biMap.findOriginalId(10))
        assertEquals("key2", biMap.findOriginalId(11))
    }

    @Test
    fun testSaveAndLoad() {
        val file = tempFolder.newFile("test_save.map")
        val generator = TestIdGenerator(1)
        val biMap = BiMapStringInt(generator, file)

        biMap.findIdOrAdd("alpha")
        biMap.findIdOrAdd("beta")
        biMap.save()

        // Load into new instance
        val loadedBiMap = BiMapStringInt(TestIdGenerator(100), file)
        assertEquals(1, loadedBiMap.findId("alpha"))
        assertEquals(2, loadedBiMap.findId("beta"))
        assertEquals("alpha", loadedBiMap.findOriginalId(1))
        assertEquals("beta", loadedBiMap.findOriginalId(2))
    }

    @Test
    fun testFindNotFound() {
        val file = tempFolder.newFile("test_not_found.map")
        val biMap = BiMapStringInt(TestIdGenerator(1), file)

        assertEquals(0, biMap.findId("nonexistent"))
        assertThrows(NoSuchElementException::class.java) {
            biMap.findOriginalId(999)
        }
    }
}
