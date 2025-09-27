package com.techabdullah.currencies_exchange.ui.currencies
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techabdullah.currencies_exchange.R
import kotlin.getValue

class CurrenciesFragment : Fragment()
{
    private  val  viewModel: CurrenciesViewModel by viewModels()
    lateinit var  rcyCurrencies : RecyclerView
    lateinit var  pbLoading : ProgressBar
    private lateinit var adapter: CurrencyAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        val root = inflater.inflate(R.layout.fragment_currencies, container, false)

        rcyCurrencies = root.findViewById(R.id.lstCurrencies)
        pbLoading = root.findViewById(R.id.pbLoading)

        _Intlz()

        return root
    }

    fun _Intlz()
    {


        viewModel.loadRates()

        rcyCurrencies.layoutManager = LinearLayoutManager(requireContext())
        adapter = CurrencyAdapter(viewModel.ratesMap.value)
        rcyCurrencies.adapter = adapter

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            pbLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

}
