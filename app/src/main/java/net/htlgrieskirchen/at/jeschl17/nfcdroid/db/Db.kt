package net.htlgrieskirchen.at.jeschl17.nfcdroid.db

import android.content.Context
import android.nfc.NdefMessage
import androidx.room.*
import java.io.Serializable


@Entity(tableName = "nfc_tag")
class NfcTag(
    @PrimaryKey val name: String,
    @ColumnInfo(name = "ndef_message", typeAffinity = ColumnInfo.BLOB) val ndefMessage: NdefMessage?,
    @ColumnInfo(name = "tag_id") val tagId: String?,
    @ColumnInfo(name = "technologies") val technologies: String?,
    @ColumnInfo(name = "data_format") val dataFormat: String?,
    @ColumnInfo(name = "memory_space") val memorySpace: String?,
    @ColumnInfo(name = "atqa") val atqa: String?,
    @ColumnInfo(name = "sak") val sak: String?,
    @ColumnInfo(name = "editable") val editable: String?,
    @ColumnInfo(name = "can_make_read_only") val canMakeReadOnly: String?) : Serializable

@Dao
interface NfcTagDao {
    @Query("""
        SELECT
          *
        FROM
          nfc_tag""")
    fun getAll(): List<NfcTag>

    @Query("""
        SELECT
          *
        FROM
          nfc_tag
        WHERE
          name = :name""")
    fun nfcTagByName(name: String): NfcTag?

    @Insert
    fun insertAll(vararg nfcTag: NfcTag)

    @Query("""
        DELETE FROM
          nfc_tag
        WHERE
          name = :name""")
    fun deleteByName(name: String)
}

class Converters {
    @TypeConverter
    fun ndefMessageToByteArray(ndefMessage: NdefMessage?): ByteArray? {
        return ndefMessage?.toByteArray()
    }

    @TypeConverter
    fun byteArrayToNdefMessage(byteArray: ByteArray?): NdefMessage? {
        return byteArray?.let {
            NdefMessage(it)
        }
    }
}

@Database(entities = [NfcTag::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val nfcTagDao: NfcTagDao?

    companion object {
        private const val DB_NAME = "nfcDroidDatabase.db"

        @Volatile
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = create(context)
            }
            return instance!!
        }

        private fun create(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DB_NAME)
                .allowMainThreadQueries()
                .build()
        }
    }
}