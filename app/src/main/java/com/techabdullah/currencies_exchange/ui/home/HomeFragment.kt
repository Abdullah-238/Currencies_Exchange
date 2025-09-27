package com.techabdullah.currencies_exchange.ui.home
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.techabdullah.currencies_exchange.R

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    lateinit var spCurrenciesFrom: Spinner
    lateinit var spCurrenciesTo: Spinner
    lateinit var txtAmount: EditText
    lateinit var lblConvertedAmount: TextView
    lateinit var btnConvert: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        spCurrenciesFrom = root.findViewById(R.id.spCurrenciesFrom)
        spCurrenciesTo = root.findViewById(R.id.spCurrenciesTo)
        txtAmount = root.findViewById(R.id.txtAmount)
        lblConvertedAmount = root.findViewById(R.id.lbl_ConvertedAmount)
        btnConvert = root.findViewById(R.id.btnConvert)

        observeViewModel()
        viewModel.loadRates()

        setupSpinners()
        setupButton()

        return root
    }

    private fun observeViewModel() {

        viewModel.rates.observe(viewLifecycleOwner) { rates ->
            val list = rates?.keys?.toList() ?: emptyList()
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, list)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spCurrenciesFrom.adapter = adapter
            spCurrenciesTo.adapter = adapter
        }

        viewModel.convertedAmount.observe(viewLifecycleOwner) { result ->
            lblConvertedAmount.text = result.toString()
        }
    }

    private fun setupSpinners() {
        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                convertValue()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        spCurrenciesFrom.onItemSelectedListener = listener
        spCurrenciesTo.onItemSelectedListener = listener
    }

    private fun setupButton() {
        btnConvert.setOnClickListener { convertValue() }
    }

    private fun convertValue() {
        val from = spCurrenciesFrom.selectedItem as? String ?: return
        val to = spCurrenciesTo.selectedItem as? String ?: return
        val amount = txtAmount.text.toString().toDoubleOrNull() ?: 0.0
        viewModel.convert(from, to, amount)
    }
}
