package com.example.hw5

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class MainMealFragment : Fragment(R.layout.fragment_main_meal) {
    private val vm: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rgMainMeal = view.findViewById<RadioGroup>(R.id.rgMainMeal)
        val btnDone = view.findViewById<Button>(R.id.btnDone)

        rgMainMeal.setOnCheckedChangeListener { _, checkedId ->
            val selected = view.findViewById<RadioButton>(checkedId)?.text?.toString()
            selected?.let { vm.setMainMeal(it) }
        }

        btnDone.setOnClickListener {
            (activity as? MainActivity)?.navigateToTab(1)
        }
    }
}
