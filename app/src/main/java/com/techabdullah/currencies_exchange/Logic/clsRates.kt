package com.techabdullah.currencies_exchange.Logic

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.getString
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.techabdullah.currencies_exchange.data.DBHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale


class clsRates(private val dbHelper: DBHelper) {

    var conversionRates: MutableMap<String, Double> = mutableMapOf()

    fun checkAndUpdateDate(context: Context): Boolean
    {
        val sharedPrefs = context.getSharedPreferences("appPrefs", Context.MODE_PRIVATE)
        val lastDateStr = sharedPrefs.getString("lastConversionDate", null)

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = Date()

        if (lastDateStr != null) {
            try {
                val lastDate = sdf.parse(lastDateStr)
                val diff = currentDate.time - lastDate.time
                val daysPassed = diff / (1000 * 60 * 60 * 24)

                if (daysPassed < 7)
                {
                    return false
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val currentDateStr = sdf.format(currentDate)
        sharedPrefs.edit().putString("lastConversionDate", currentDateStr).apply()
        return true
    }


    suspend fun fetchRates() {

        val savedRates = dbHelper.getAll()
        if (savedRates.isNotEmpty() )
        {
            conversionRates.clear()
            savedRates.forEach { entry -> conversionRates[entry.currency] = entry.amount }
        }
        else
        {
            fetchRatesFromInternet()
            conversionRates.forEach { (currency, amount) -> dbHelper.add(currency, amount) }
        }
    }

    private suspend fun fetchRatesFromInternet() {
        withContext(Dispatchers.IO) {
            val urlStr = "https://v6.exchangerate-api.com/v6/19ed1657e22a016bda03d9a3/latest/USD"
            val url = URL(urlStr)
            val request = url.openConnection() as HttpURLConnection
            request.connect()

            val reader = InputStreamReader(request.inputStream)
            val root = JsonParser.parseReader(reader)
            val jsonObj: JsonObject = root.asJsonObject
            val conversionJson = jsonObj.getAsJsonObject("conversion_rates")

            val map = mutableMapOf<String, Double>()
            for ((key, value) in conversionJson.entrySet()) map[key] = value.asDouble

            conversionRates.clear()
            conversionRates.putAll(map)
        }
    }

    fun getCurrencyAmount(value: String): Double?
    {
        return conversionRates[value]
    }
    fun convertCurrency(fromCurrency: String, toCurrency: String, amount: Double): Double
    {
        val fromRate = conversionRates[fromCurrency]?: 0.0
        val toRate = conversionRates[toCurrency]?: 0.0
        val amountInBase = amount / fromRate
        return amountInBase * toRate
    }
}