# Developer & AI Agent Guidelines

This document provides architectural guidance and technical standards for AI agents and developers modifying or extending the **`o2omap`** library codebase.

---

## Codebase Map

| File Path | Description |
|---|---|
| `src/main/kotlin/com/trodevel/o2omap/BiMapStringInt.kt` | Core bidirectional 1:1 `String` <-> `Int` map implementation with persistence and dirty flag state management. |
| `src/test/kotlin/com/trodevel/o2omap/BiMapStringIntTest.kt` | Unit test suite testing lookup, persistence, error handling, and dirty flag behavior. |
| `build.gradle.kts` | Subproject Gradle build configuration defining dependencies (`uniqueidgenerator`, `stringcodec`). |
| `README.md` | General overview and usage examples for the library. |

---

## Key Architectural Patterns & Guidelines

### 1. Dirty Flag State (`isDirty`)
- **Initialization:** Upon creation / `load()`, `isDirty` is set to `false`.
- **Mutation:** `findIdOrAdd(originalId)` sets `isDirty = true` ONLY when `originalId` is not present in the map and a new ID is generated. Querying existing keys does not change `isDirty`.
- **Persistence Bypass:** `save()` evaluates `if (!isDirty) return`. File I/O is skipped if no new keys have been added.
- **Reset:** `save()` resets `isDirty = false` after successfully writing all records to disk.

### 2. File Format & Persistence
- **Header Structure:**
  - Line 1: `O2OMAP` (Magic header string)
  - Line 2: `1` (Version integer)
  - Line 3: `<Count>` (Total number of records)
- **Record Serialization:**
  - Format: `<EncodedString> <IntID>`
  - Strings are encoded/decoded using `com.trodevel.stringcodec.StringCodec` to preserve single-line plaintext integrity (escaping spaces, newlines, and special characters).

### 3. Bidirectional Consistency
- Data is stored in two synchronized maps: `stringToInt` (`MutableMap<String, Int>`) and `intToString` (`MutableMap<Int, String>`).
- Internal `validate()` method ensures both maps have equal sizes and consistent inverse lookups after loading.

### 4. Build & Verification
- Prior to submitting changes, run unit tests to verify behavior:
  ```bash
  ./gradlew :o2omap:testDebugUnitTest
  ```
