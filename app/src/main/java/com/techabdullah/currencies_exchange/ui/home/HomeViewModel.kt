package com.techabdullah.currencies_exchange.ui.home
import android.app.Application
import androidx.lifecycle.*
import com.techabdullah.currencies_exchange.Logic.clsRates
import com.techabdullah.currencies_exchange.data.DBHelper
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = DBHelper(application)
    private val rate = clsRates(dbHelper)

    private val _rates = MutableLiveData<Map<String, Double>>()
    val rates: LiveData<Map<String, Double>> get() = _rates

    private val _convertedAmount = MutableLiveData<Double>()
    val convertedAmount: LiveData<Double> get() = _convertedAmount

    fun loadRates()
    {
        viewModelScope.launch{
            try
            {
                rate.fetchRates()
                _rates.value = rate.conversionRates
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
        }
    }

    fun convert(from: String, to: String, amount: Double)

    {
        _convertedAmount.value = rate.convertCurrency(from, to, amount)
    }
}
