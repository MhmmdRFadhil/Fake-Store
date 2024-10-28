package com.ryz.fakestore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ryz.fakestore.data.local.dao.CartProductDao
import com.ryz.fakestore.data.local.model.CartProductEntity

@Database(entities = [CartProductEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartProductDao(): CartProductDao
}