package com.example.allahnamesquran.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.allahnamesquran.data.local.dao.AyahDao
import com.example.allahnamesquran.data.local.entity.AyahEntity

@Database(entities = [AyahEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ayahDao(): AyahDao
}

