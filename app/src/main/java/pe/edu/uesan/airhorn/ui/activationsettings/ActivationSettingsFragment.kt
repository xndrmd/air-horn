package pe.edu.uesan.airhorn.ui.activationsettings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_activation_settings.*
import pe.edu.uesan.airhorn.R
import pe.edu.uesan.airhorn.services.ShakeService
import pe.edu.uesan.airhorn.utilities.*
import pe.edu.uesan.airhorn.viewmodels.ActivationSettingsViewModel

@AndroidEntryPoint
class ActivationSettingsFragment : Fragment() {
    private val viewModel: ActivationSettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activation_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribe()

        shakeEventSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateIntPreference(SHARED_PREFERENCES_EVENT_SHAKE, if (isChecked) 1 else 0)
        }

        eventsThresholdOption.setOnClickListener {
            openDialog(
                "Cantidad de eventos para comenzar modo alerta",
                SHARED_PREFERENCES_PARAMS_EVENTS_THRESHOLD,
                    eventsThreshold.text.toString().toInt()
            )
        }

        secondsThresholdOption.setOnClickListener {
            openDialog(
                "Cantidad de segundos antes de comenzar modo alerta",
                SHARED_PREFERENCES_PARAMS_SECONDS_THRESHOLD,
                    secondsThreshold.text.toString().toInt()
            )
        }
    }

    private fun subscribe() {
        viewModel.config.observe(viewLifecycleOwner) { preference ->
            when (preference.first) {
                SHARED_PREFERENCES_EVENT_SHAKE -> {
                    val isChecked = preference.second == 1
                    shakeEventSwitch.isChecked = isChecked

                    when (isChecked) {
                        true -> ServiceUtil.sendCommand(requireActivity(), ShakeService::class.java, ACTION_START_SERVICE)
                        else -> ServiceUtil.sendCommand(requireActivity(), ShakeService::class.java, ACTION_STOP_SERVICE)
                    }
                }
                SHARED_PREFERENCES_PARAMS_EVENTS_THRESHOLD -> eventsThreshold.text = preference.second.toString()
                SHARED_PREFERENCES_PARAMS_SECONDS_THRESHOLD -> secondsThreshold.text = preference.second.toString()
            }
        }
    }

    private fun openDialog(message: String, preference: String, threshold: Int) {
        val layout = layoutInflater.inflate(R.layout.fragment_param_dialog, null)
        val input: TextInputEditText = layout.findViewById(R.id.threshold)
        input.setText(threshold.toString())

        MaterialAlertDialogBuilder(requireContext())
            .setPositiveButton("Aceptar") { dialog, which ->
                viewModel.updateIntPreference(preference, input.text.toString().toInt())
            }
            .setNegativeButton("Cancelar", null)
            .setMessage(message)
            .setView(layout)
            .show()
    }
}