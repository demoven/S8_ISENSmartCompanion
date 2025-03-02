package fr.isen.fernando.isensmartcompanion.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.isen.fernando.isensmartcompanion.database.AppDatabase
import fr.isen.fernando.isensmartcompanion.database.PairQuestionAnswer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PairViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val pairDao = db.pairDAO()

    fun getAllPairs(): LiveData<List<PairQuestionAnswer>> {
        val liveData = MutableLiveData<List<PairQuestionAnswer>>()
        viewModelScope.launch(Dispatchers.IO) {
            val pairs = pairDao.getAll()
            liveData.postValue(pairs)
        }
        return liveData
    }

    fun insertPair(pair: PairQuestionAnswer) {
        viewModelScope.launch(Dispatchers.IO) {
            pairDao.insert(pair)
        }
    }
}