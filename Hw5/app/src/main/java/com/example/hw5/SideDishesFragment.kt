package com.example.hw5

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class SideDishesFragment : Fragment(R.layout.fragment_side_dishes) {
    private val vm: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cbFries = view.findViewById<CheckBox>(R.id.cbFries)
        val cbSalad = view.findViewById<CheckBox>(R.id.cbSalad)
        val cbCornCup = view.findViewById<CheckBox>(R.id.cbCornCup)
        val btnDone = view.findViewById<Button>(R.id.btnDone)

        val bindCheckBox = { cb: CheckBox ->
            cb.setOnCheckedChangeListener { _, isChecked ->
                vm.toggleSideDish(cb.text.toString(), isChecked)
            }
        }

        bindCheckBox(cbFries)
        bindCheckBox(cbSalad)
        bindCheckBox(cbCornCup)

        btnDone.setOnClickListener {
            (activity as? MainActivity)?.navigateToTab(2)
        }
    }
}
