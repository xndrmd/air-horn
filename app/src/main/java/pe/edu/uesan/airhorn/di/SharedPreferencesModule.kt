package pe.edu.uesan.airhorn.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pe.edu.uesan.airhorn.utilities.SHARED_PREFERENCES_NAME

@InstallIn(SingletonComponent::class)
@Module
class SharedPreferencesModule () {
    @Provides
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
}