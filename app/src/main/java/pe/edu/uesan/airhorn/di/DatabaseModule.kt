package pe.edu.uesan.airhorn.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pe.edu.uesan.airhorn.data.AppDatabase
import pe.edu.uesan.airhorn.data.ContactDao
import pe.edu.uesan.airhorn.data.PhoneDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideContactDao(appDatabase: AppDatabase): ContactDao {
        return appDatabase.contactDao()
    }

    @Provides
    fun providePhoneDao(appDatabase: AppDatabase): PhoneDao {
        return appDatabase.phoneDao()
    }
}