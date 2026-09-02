package com.kusius.doughy.core.database.migrations

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kusius.doughy.core.database.RecipeEntity
import com.kusius.doughy.core.database.asEntity
import com.kusius.doughy.core.model.predefinedRecipes

internal fun SupportSQLiteDatabase.addRecipe(recipeEntity: RecipeEntity) {
    val content = ContentValues()
    content.put("name", recipeEntity.name)
    content.put("hydrationPercent", recipeEntity.hydrationPercent)
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
    insert("recipe", SQLiteDatabase.CONFLICT_IGNORE, content)
}

internal fun SupportSQLiteDatabase.addPredefinedRecipes() =
    predefinedRecipes.forEach { addRecipe(it.asEntity()) }
