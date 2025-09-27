package com.techabdullah.currencies_exchange.ui.get_price
import android.app.Application
import androidx.lifecycle.*
import com.techabdullah.currencies_exchange.Logic.clsRates
import com.techabdullah.currencies_exchange.data.DBHelper
import kotlinx.coroutines.launch

class GetPriceViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = DBHelper(application)
    private val rates = clsRates(dbHelper)

    private val _ratesMap = MutableLiveData<Map<String, Double>>()
    val ratesMap: LiveData<Map<String, Double>> get() = _ratesMap

    private val _selectedAmount = MutableLiveData<Double>()
    val selectedAmount: LiveData<Double> get() = _selectedAmount


    fun loadRates()
    {
        viewModelScope.launch{
            try
            {
                rates.fetchRates()
                _ratesMap.value = rates.conversionRates
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
        }
    }


    fun onCurrencySelected(currency: String)
    {
        _selectedAmount.value = rates.getCurrencyAmount(currency)
    }
}
