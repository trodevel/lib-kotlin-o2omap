# o2omap (One-to-One Map Library)

`o2omap` provides `BiMapStringInt`, a thread-safe 1:1 bidirectional mapping between string keys and integer IDs with file persistence.

## Features

- **Bidirectional lookup**: String-to-Int and Int-to-String.
- **Auto ID generation**: Assigns unique integer IDs using `IUniqueIdGenerator`.
- **Persistence**: Save to and load from text storage file with version header validation.
- **Validation**: Enforces 1:1 mapping consistency.

## Usage

```kotlin
import com.trodevel.o2omap.BiMapStringInt
import com.trodevel.uniqueidgenerator.IUniqueIdGenerator
import java.io.File

val generator = object : IUniqueIdGenerator {
    private var nextId = 1
    override fun getNextId(): Int = nextId++
}

val file = File("mapping.map")
val biMap = BiMapStringInt(generator, file)

// Assign or retrieve ID
val id = biMap.findIdOrAdd("my_unique_string_key")

// Save to disk
biMap.save()
```
