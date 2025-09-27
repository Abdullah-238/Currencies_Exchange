package com.techabdullah.currencies_exchange.ui.currencies
import android.app.Application
import androidx.lifecycle.*
import com.techabdullah.currencies_exchange.Logic.clsRates
import com.techabdullah.currencies_exchange.data.DBHelper
import kotlinx.coroutines.launch

class CurrenciesViewModel(application: Application) : AndroidViewModel(application)  {

    private val dbHelper = DBHelper(application)
    private val rate = clsRates(dbHelper)

    private val _ratesMap = MutableLiveData<Map<String, Double>>()
    val ratesMap: LiveData<Map<String, Double>> get() = _ratesMap

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun loadRates() {
        viewModelScope.launch {
            _loading.value = true
            try {
                rate.fetchRates()
                _ratesMap.value = rate.conversionRates
            } catch (e: Exception) {
                e.printStackTrace()
            }
            finally {
                _loading.value = false
            }
        }
    }
}
