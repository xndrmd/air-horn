package pe.edu.uesan.airhorn.ui.security

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import pe.edu.uesan.airhorn.R

class ChoosePinFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_pin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cancel: MaterialButton = view.findViewById(R.id.btn_cancel)
        val confirm: MaterialButton = view.findViewById(R.id.btn_confirm)

        cancel.setOnClickListener {
            val startDestination = view.findNavController().graph.startDestination
            view.findNavController().popBackStack(startDestination, false)
        }

        confirm.setOnClickListener {
            val action = ChoosePinFragmentDirections.actionChoosePinFragmentToConfirmPinFragment()
            view.findNavController().navigate(action)
        }
    }
}