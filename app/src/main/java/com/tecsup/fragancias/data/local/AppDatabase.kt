package com.tecsup.fragancias.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [ItemEntity::class, FavoritoEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var instancia: AppDatabase? = null

        private val MIGRACION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `favoritos_usuario` (`usuarioId` TEXT NOT NULL, `itemId` INTEGER NOT NULL, PRIMARY KEY(`usuarioId`, `itemId`))"
                )
            }
        }

        fun obtener(context: Context): AppDatabase {
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fragancias.db"
                )
                    .addMigrations(MIGRACION_1_2)
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { instancia = it }
            }
        }
    }
}
