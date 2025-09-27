package com.techabdullah.currencies_exchange.ui.get_price
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.techabdullah.currencies_exchange.R


class GetPriceFragment : Fragment() {

    private  val  viewModel: GetPriceViewModel by viewModels()
    lateinit var  spCurrencies : Spinner
    lateinit var  textNotifications : TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        val root = inflater.inflate(R.layout.fragment_get_price, container, false)

        spCurrencies = root.findViewById(R.id.spCurrencies)
        textNotifications = root.findViewById(R.id.text_notifications)

        _Intlz()
        _load()

        return root
    }


    fun _load()
    {
        spCurrencies.setOnItemSelectedListener { currency ->
            viewModel.onCurrencySelected(currency)
        }

        viewModel.selectedAmount.observe(viewLifecycleOwner, Observer { amount ->
            textNotifications.text = amount?.toString() ?: "0"
        })
    }

    fun _Intlz()
    {
        viewModel.loadRates()

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, viewModel.ratesMap.value.keys.toList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCurrencies.adapter = adapter
    }

    private fun Spinner.setOnItemSelectedListener(onSelect: (String) -> Unit)
    {
        this.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: View?, position: Int, id: Long)
            {
                val selected = parent.getItemAtPosition(position) as String
                onSelect(selected)
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
    }

}
