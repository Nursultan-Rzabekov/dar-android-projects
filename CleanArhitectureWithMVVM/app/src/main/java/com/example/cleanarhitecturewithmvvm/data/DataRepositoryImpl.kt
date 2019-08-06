package com.example.cleanarhitecturewithmvvm.data


import com.example.cleanarhitecturewithmvvm.exceptions.RemoteDataNotFoundException
import com.example.cleanarhitecturewithmvvm.exceptions.ReposRefreshError
import com.example.cleanarhitecturewithmvvm.data.mapper.LanguageModelConverter
import com.example.cleanarhitecturewithmvvm.data.source.LanguageDataStoreFactory
import com.example.cleanarhitecturewithmvvm.domain.model.Language
import com.example.cleanarhitecturewithmvvm.domain.repository.IRoomRepository
import kotlinx.coroutines.*

class DataRepositoryImpl(private val languageDataStoreFactory: LanguageDataStoreFactory,
                         private val languageModelConverter: LanguageModelConverter): IRoomRepository {


    override suspend fun getAllLanguage() : List<Language> {
        return try {
            withContext(Dispatchers.IO) {
                val dataStore = languageDataStoreFactory.retrieveDataStore()
                dataStore.getAllLanguage().map(languageModelConverter::modelToDomain)
            }
        }catch (error: RemoteDataNotFoundException){
            throw  ReposRefreshError(error)
        }
    }

    override suspend fun storeLanguage(store: String) {
        try {
            withContext(Dispatchers.IO){
                languageDataStoreFactory.retrieveRoomDataStore().storeLanguage(store)
            }
        }catch (error: RemoteDataNotFoundException){
            throw ReposRefreshError(error)
        }
    }

    override suspend fun storeAllLanguage(languageList: List<Language>?) {
        try {
            withContext(Dispatchers.Default){
                languageDataStoreFactory.retrieveRoomDataStore().insertAll(languageList?.map(languageModelConverter::apiToModel)!!)
            }
        } catch (error: RemoteDataNotFoundException){
            throw ReposRefreshError(error)
        }
    }

    override suspend fun updateLanguageName(position: Int, language: String) {
        try {
            withContext(Dispatchers.IO){
                languageDataStoreFactory.retrieveRoomDataStore().updateLanguageName(position+1,language)
            }
        }catch (error: RemoteDataNotFoundException){
            throw ReposRefreshError(error)
        }
    }

    override suspend fun deleteLanguageID(position: Int) {
        try {
            withContext(Dispatchers.IO){
                languageDataStoreFactory.retrieveRoomDataStore().deleteLanguageID(position+1)
            }
        }catch (error: RemoteDataNotFoundException){
            throw ReposRefreshError(error)
        }
    }

}

