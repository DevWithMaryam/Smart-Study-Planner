package com.maryam.smartstudyplanner.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

// @Module + @InstallIn(SingletonComponent::class): Hilt ko batata hai
// ke is object mein defined dependencies poore app ke lifetime tak
// ek hi instance (singleton) ke tor par available rahengi.
// Room database aur Repositories Phase 2 mein yahan provide karenge.
@Module
@InstallIn(SingletonComponent::class)
object AppModule