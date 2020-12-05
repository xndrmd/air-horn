package pe.edu.uesan.airhorn.ui.security

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import pe.edu.uesan.airhorn.R
import pe.edu.uesan.airhorn.utilities.SHARED_PREFERENCES_PIN
import pe.edu.uesan.airhorn.viewmodels.SecurityViewModel

@AndroidEntryPoint
class ConfirmPinFragment : Fragment() {
    private val args: ConfirmPinFragmentArgs by navArgs()

    private val viewModel: SecurityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_pin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cancel: MaterialButton = view.findViewById(R.id.btn_cancel)
        val confirm: MaterialButton = view.findViewById(R.id.btn_confirm)

        val pin: TextInputEditText = view.findViewById(R.id.pin)

        cancel.setOnClickListener {
            val startDestination = view.findNavController().graph.startDestination
            view.findNavController().popBackStack(startDestination, false)
        }

        confirm.setOnClickListener {
            val pinNumber = pin.text.toString()
            pin.setText("")

            when {
                pinNumber.isEmpty() -> {
                    Toast.makeText(requireActivity(), "Es necesario que ingrese un PIN para continuar", Toast.LENGTH_SHORT).show()
                }
                pinNumber.length < 4 -> {
                    Toast.makeText(requireActivity(), "El PIN debe ser del al menos 4 dígitos", Toast.LENGTH_SHORT).show()
                }
                pinNumber != args.newPin -> {
                    Toast.makeText(requireActivity(), "El PIN ingresado no coincide", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    viewModel.updateStringPreference(SHARED_PREFERENCES_PIN, pinNumber)

                    val startDestination = view.findNavController().graph.startDestination
                    view.findNavController().popBackStack(startDestination, false)
                }
            }
        }
    }
}