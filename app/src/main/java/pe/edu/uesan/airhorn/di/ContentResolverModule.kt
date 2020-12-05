package pe.edu.uesan.airhorn.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import pe.edu.uesan.airhorn.contentresolver.ContactInfoSource

@InstallIn(SingletonComponent::class)
@Module
class ContentResolverModule {
    @Provides
    fun providesContactInfoSource(@ApplicationContext context: Context): ContactInfoSource {
        return ContactInfoSource(context.contentResolver, PhoneNumberUtil.createInstance(context))
    }
}