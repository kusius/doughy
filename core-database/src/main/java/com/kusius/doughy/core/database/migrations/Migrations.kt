package com.kusius.doughy.core.database.migrations

import android.content.ContentValues
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kusius.doughy.core.database.RecipeEntity

val MIGRATION_1_2 = object : Migration(1, 2) {
    private fun addRecipe(database: SupportSQLiteDatabase, recipeEntity: RecipeEntity) {
        val content = ContentValues()
            content.put("name", recipeEntity.name)
            content.put("oilPercent", recipeEntity.oilPercent)
            content.put("saltPercent", recipeEntity.saltPercent)
            content.put("sugarsPercent", recipeEntity.sugarsPercent)
            content.put("yeastPercent", recipeEntity.yeastPercent)
            content.put("yeastType", recipeEntity.yeastType.name)
            content.put("prefermentPercent", recipeEntity.prefermentPercent)
            content.put("prefermentHydrationPercent", recipeEntity.prefermentHydrationPercent)
            content.put("prefermentUsesYeast", recipeEntity.prefermentUsesYeast)
            content.put("prefermentRestHours", recipeEntity.prefermentRestHours)
            content.put("bulkRestHours", recipeEntity.bulkRestHours)
            content.put("ballsRestHours", recipeEntity.ballsRestHours)
            content.put("description", recipeEntity.description)
            content.put("isCustom", recipeEntity.isCustom)
        database.insert("recipe", android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE,
        content)
    }

    override fun migrate(database: SupportSQLiteDatabase) {
        database.beginTransaction()
        try {
            database.delete("recipe", null, null)
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN hydrationPercent REAL"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN oilPercent REAL"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN saltPercent REAL"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN sugarsPercent REAL"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN yeastPercent REAL"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN yeastType STRING"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN prefermentPercent REAL"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN prefermentHydrationPercent REAL"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN prefermentUsesYeast INTEGER"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN prefermentRestHours INTEGER"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN bulkRestHours INTEGER"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN ballsRestHours INTEGER"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN description TEXT"
            )
            database.execSQL(
                "ALTER TABLE recipe ADD COLUMN isCustom INTEGER"
            )
            predefinedRecipes.forEach {
                addRecipe(database, it)
            }
            database.setTransactionSuccessful()
        } finally {
            database.endTransaction()
        }
    }
}
