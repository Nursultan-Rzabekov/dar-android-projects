package com.example.cleanarhitecturewithmvp.mvp.roomLanguages


import android.app.Application
import com.example.cleanarhitecturewithmvp.ApplicationClass
import com.example.cleanarhitecturewithmvp.data.dao.LanguageDao
import com.example.cleanarhitecturewithmvp.domain.model.Language
import com.example.cleanarhitecturewithmvp.domain.usecase.delete.DeleteAllLanguageUseCase
import com.example.cleanarhitecturewithmvp.domain.usecase.delete.DeleteLanguageUseCase
import com.example.cleanarhitecturewithmvp.domain.usecase.get.GetLanguageByIdUseCase
import com.example.cleanarhitecturewithmvp.domain.usecase.get.GetLanguageUseCase
import com.example.cleanarhitecturewithmvp.domain.usecase.insert.InsertAllLanguageUseCase
import com.example.cleanarhitecturewithmvp.domain.usecase.insert.InsertLanguageUseCase
import com.example.cleanarhitecturewithmvp.domain.usecase.update.UpdateLanguageUseCase
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class RoomLanguagesPresenterImpl(var postViewI: IRoomLanguagesView, applicationComponent: Application): IRoomLanguagesPresenter, CoroutineScope{

    @Inject
    lateinit var provideGetLanguageByIdUseCase: GetLanguageByIdUseCase

    @Inject
    lateinit var provideGetLanguageUseCase: GetLanguageUseCase

    @Inject
    lateinit var provideDeleteAllLanguageUseCase: DeleteAllLanguageUseCase

    @Inject
    lateinit var provideDeleteLanguageUseCase: DeleteLanguageUseCase

    @Inject
    lateinit var provideInsertAllLanguageUseCase: InsertAllLanguageUseCase

    @Inject
    lateinit var provideInsertLanguageUseCase: InsertLanguageUseCase

    @Inject
    lateinit var provideUpdateLanguageUseCase: UpdateLanguageUseCase




    init {
        (applicationComponent as ApplicationClass).applicationComponent.inject(this)
    }

    private var viewModelJob = Job()
    override val coroutineContext: CoroutineContext
        get() = viewModelJob + Dispatchers.Main

    override fun getByLanguageID(position: Int) {
        this.launch(coroutineContext){
            provideGetLanguageByIdUseCase.execute(position)
        }
    }

    override fun getAllLanguage() =
        this.launch(coroutineContext){
            postViewI.showAllLanguage(provideGetLanguageUseCase.execute())
        }

    override fun storeLanguageAll(languageList: ArrayList<Language>?) {
        this.launch(coroutineContext){
            provideInsertAllLanguageUseCase.execute(languageList)
        }
    }

    override fun storeLanguage(language_name: String) {
        this.launch(coroutineContext){
            val language = Language(language_name)
            provideInsertLanguageUseCase.execute(language_name)
            postViewI.storeLanguage(language)
        }
    }

    override fun updateLanguageName(position: Int,language_name: String) {
        this.launch(coroutineContext){
            val language = Language(language_name)
            provideUpdateLanguageUseCase.execute(position,language_name)
            postViewI.updateLanguage(position,language)
        }
    }

    override fun deleteAllLanguage() {
        this.launch(coroutineContext){
            provideDeleteAllLanguageUseCase.execute()
            postViewI.deleteLanguage()
        }
    }

    override fun deleteLanguageByID(position: Int) {
        this.launch(coroutineContext){
            provideDeleteLanguageUseCase.execute(position)
            postViewI.deleteLanguageByLanguageID(position)
        }
    }

    internal fun onDestroy(){
        coroutineContext.cancel()
    }
}

