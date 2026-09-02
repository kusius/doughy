package com.kusius.doughy.core.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.beginTransaction()
        try {
            database.delete("recipe", null, null)
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN hydrationPercent REAL NOT NULL DEFAULT undefined"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN oilPercent REAL NOT NULL DEFAULT undefined"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN saltPercent REAL NOT NULL DEFAULT undefined"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN sugarsPercent REAL NOT NULL DEFAULT undefined"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN yeastPercent REAL NOT NULL DEFAULT undefined"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN yeastType TEXT NOT NULL DEFAULT undefined"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN prefermentPercent REAL NOT NULL DEFAULT undefined"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN prefermentHydrationPercent REAL NOT NULL DEFAULT undefined"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN prefermentUsesYeast INTEGER NOT NULL DEFAULT undefined"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN prefermentRestHours INTEGER NOT NULL DEFAULT undefined"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN bulkRestHours INTEGER NOT NULL DEFAULT undefined"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN ballsRestHours INTEGER NOT NULL DEFAULT undefined"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN description TEXT NOT NULL DEFAULT undefined"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN isCustom INTEGER NOT NULL DEFAULT undefined"
            )
            database.addPredefinedRecipes()
            database.setTransactionSuccessful()
        } finally {
            database.endTransaction()
        }
    }
}
