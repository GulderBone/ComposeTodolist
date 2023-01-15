package com.gulderbone.todolist.di

import android.app.Application
import androidx.room.Room
import com.gulderbone.todolist.data.TodoDatabase
import com.gulderbone.todolist.data.TodoRepository
import com.gulderbone.todolist.data.TodoRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesTodoDatabase(app: Application): TodoDatabase =
        Room.databaseBuilder(
            app,
            TodoDatabase::class.java,
            "todo_db",
        ).build()

    @Provides
    @Singleton
    fun provideTodoRepository(db: TodoDatabase): TodoRepository =
        TodoRepositoryImpl(db.dao)
}