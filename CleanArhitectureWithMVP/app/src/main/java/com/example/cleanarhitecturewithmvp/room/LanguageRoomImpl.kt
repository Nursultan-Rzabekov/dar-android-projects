package com.example.cleanarhitecturewithmvp.room


import com.example.cleanarhitecturewithmvp.data.model.LanguageEntity
import com.example.cleanarhitecturewithmvp.data.repository.LanguageRoom
import com.example.cleanarhitecturewithmvp.exceptions.RemoteDataNotFoundException
import com.example.cleanarhitecturewithmvp.exceptions.ReposRefreshError
import com.example.cleanarhitecturewithmvp.room.dao.LanguageDao
import com.example.cleanarhitecturewithmvp.room.mapper.RoomModelConverter
import com.example.cleanarhitecturewithmvp.room.model.LanguageRoomDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class LanguageRoomImpl(private val dataSource: LanguageDao, private val roomModelConverter: RoomModelConverter): LanguageRoom {

    override suspend fun getByLanguageID(position: Int): String? {
        return try {
            withContext(Dispatchers.IO) {
                dataSource.getLanguageById(position).languageName
            }
        }catch (error: RemoteDataNotFoundException){
            throw  ReposRefreshError(error)
        }
    }

    override suspend fun getAllLanguage() : List<LanguageEntity> {
        return try {
            withContext(Dispatchers.IO) {
                dataSource.getAllLanguage().map(roomModelConverter::modelToDomain)
            }
        }catch (error: RemoteDataNotFoundException){
            throw  ReposRefreshError(error)
        }
    }

    override suspend fun storeLanguage(store: String) {
        try {
            withContext(Dispatchers.IO){
                val language = LanguageRoomDB(store)
                dataSource.insertLanguage(language)
            }
        }catch (error: RemoteDataNotFoundException){
            throw ReposRefreshError(error)
        }
    }

    override suspend fun storeAllLanguage(languageList: List<LanguageEntity>?) {
        try {
            withContext(Dispatchers.Default){
                dataSource.insertAllLanguage(languageList?.map(roomModelConverter::apiToModel))
            }
        } catch (error: RemoteDataNotFoundException){
            throw ReposRefreshError(error)
        }
    }

    override suspend fun updateLanguageName(position: Int, language: String) {
        try {
            withContext(Dispatchers.IO){
                val languageRoomDB = LanguageRoomDB(position + 1, language)
                dataSource.updateLanguage(languageRoomDB)
            }
        }catch (error: RemoteDataNotFoundException){
            throw ReposRefreshError(error)
        }
    }

    override suspend fun deleteLanguageID(position: Int) {
        try {
            withContext(Dispatchers.IO){
                dataSource.deleteLanguageByID(position+1)
            }
        }catch (error: RemoteDataNotFoundException){
            throw ReposRefreshError(error)
        }
    }

    override suspend fun deleteAllLanguageById() {
        try {
            withContext(Dispatchers.Default){
                dataSource.deleteAllLanguage()
            }
        }
        catch (error: RemoteDataNotFoundException){
            throw ReposRefreshError(error)
        }
    }



    override suspend fun isCached(): Boolean {
        return dataSource.getAllLanguage().isEmpty()
    }
}