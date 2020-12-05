package pe.edu.uesan.airhorn.ui.security

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_security.*
import pe.edu.uesan.airhorn.R
import pe.edu.uesan.airhorn.utilities.SHARED_PREFERENCES_PIN
import pe.edu.uesan.airhorn.utilities.SHARED_PREFERENCES_USE_FINGERPRINT
import pe.edu.uesan.airhorn.viewmodels.SecurityViewModel

@AndroidEntryPoint
class SecurityFragment : Fragment() {
    private val viewModel: SecurityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_security, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        useFingerprintSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateBooleanPreference(SHARED_PREFERENCES_USE_FINGERPRINT, isChecked)
        }

        subscribeToPreferences()
    }

    private fun subscribeToPreferences() {
        viewModel.config.observe(viewLifecycleOwner) { preference ->
            when (preference.first) {
                SHARED_PREFERENCES_PIN -> {
                    val pin = (preference.second as String)
                    val hasPin = pin.trim().isNotEmpty()
                    updatePinNavigation(hasPin, pin)
                }
                SHARED_PREFERENCES_USE_FINGERPRINT -> {
                    useFingerprintSwitch.isChecked = preference.second as Boolean
                }
            }
        }
    }

    private fun updatePinNavigation(hasPin: Boolean, currentPin: String) {
        val action = when (hasPin) {
            true -> SecurityFragmentDirections.actionSecurityFragmentToInputPinFragment(currentPin)
            else -> SecurityFragmentDirections.actionSecurityFragmentToChoosePinFragment()
        }

        pinOption.setOnClickListener {
            requireView().findNavController().navigate(action)
        }
    }
}