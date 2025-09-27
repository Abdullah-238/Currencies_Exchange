package com.techabdullah.currencies_exchange.data
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


data class CurrencyEntry(val currency: String,val amount: Double )

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE_NAME ($CURRENCY_COL TEXT PRIMARY KEY, $AMOUNT_COL REAL)".trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)
    {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun add(currency: String, amount: Double) {
        val values = ContentValues().apply {
            put(CURRENCY_COL, currency)
            put(AMOUNT_COL, amount)
        }
        writableDatabase.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun getAll(): List<CurrencyEntry> {
        val currencies = mutableListOf<CurrencyEntry>()
        readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null).use { cursor ->
            val currencyCol = cursor.getColumnIndexOrThrow(CURRENCY_COL)
            val amountCol = cursor.getColumnIndexOrThrow(AMOUNT_COL)
            while (cursor.moveToNext())
            {
                val currency = cursor.getString(currencyCol)
                val amount = cursor.getDouble(amountCol)
                currencies.add(CurrencyEntry(currency, amount))
            }
        }
        return currencies
    }

    companion object {
        private const val DATABASE_NAME = "wallet_database"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "wallet_table"
        const val AMOUNT_COL = "amount"
        const val CURRENCY_COL = "currency"
    }
}
