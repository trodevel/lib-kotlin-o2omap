# o2omap (One-to-One Map Library)

`o2omap` provides `BiMapStringInt`, a 1:1 bidirectional mapping between string keys and integer IDs with file persistence and dirty flag tracking.

## Features

- **Bidirectional lookup**: String-to-Int (`findId`, `findIdOrAdd`) and Int-to-String (`findOriginalId`).
- **Auto ID generation**: Assigns unique integer IDs using `IUniqueIdGenerator`.
- **Dirty Flag Optimization**: `isDirty` property tracks unsaved in-memory mutations. `save()` is a no-op if `isDirty` is `false`, avoiding unnecessary disk I/O operations.
- **Persistence**: Save to and load from text storage file with `O2OMAP` header validation.
- **Validation**: Enforces 1:1 mapping consistency.

## Dependencies

- **`uniqueidgenerator`** (`com.trodevel.uniqueidgenerator`): Provides `IUniqueIdGenerator` for generating unique integer IDs.
- **`stringcodec`** (`com.trodevel.stringcodec`): Provides `StringCodec` for safe single-line string encoding during persistence.

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

// Assign or retrieve ID (sets isDirty = true if a new key is added)
val id = biMap.findIdOrAdd("my_unique_string_key")

// Check if unsaved changes exist
if (biMap.isDirty) {
    // Save to disk (resets isDirty = false upon success)
    biMap.save()
}
```
