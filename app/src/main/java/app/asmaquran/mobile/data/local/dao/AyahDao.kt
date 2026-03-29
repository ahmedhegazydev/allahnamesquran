package app.asmaquran.mobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.asmaquran.mobile.data.local.entity.AyahEntity

@Dao
interface AyahDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<AyahEntity>)

    @Query("SELECT COUNT(*) FROM ayahs")
    suspend fun countAyahs(): Int

    @Query("DELETE FROM ayahs")
    suspend fun clearAll()

    @Query("""
        SELECT * FROM ayahs
        WHERE textNormalized LIKE '%' || :normalizedName || '%'
        ORDER BY surahNumber, ayahNumberInSurah
    """)
    suspend fun searchAyahsByName(normalizedName: String): List<AyahEntity>
}