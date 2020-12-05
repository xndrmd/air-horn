package pe.edu.uesan.airhorn

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import pe.edu.uesan.airhorn.models.AlertModeEvent
import pe.edu.uesan.airhorn.models.CountdownEvent
import pe.edu.uesan.airhorn.services.AlertModeService
import pe.edu.uesan.airhorn.services.CountdownService
import pe.edu.uesan.airhorn.services.ShakeService
import pe.edu.uesan.airhorn.utilities.*
import pe.edu.uesan.airhorn.viewmodels.HomeViewModel

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addShakeEventObserver()
        addTitleObserver()
        addMillisObserver()
    }

    private fun addShakeEventObserver() {
        viewModel.isShakeEventAction.observe(viewLifecycleOwner) {
            if (it) {
                ServiceUtil.sendCommand(requireActivity(), ShakeService::class.java, ACTION_START_SERVICE)

                noEventCard.visibility = View.GONE
            } else {
                noEventCard.visibility = View.VISIBLE
            }
        }
    }

    private fun addTitleObserver() {
        viewModel.title.observe(viewLifecycleOwner) {
            alertCard.visibility = View.VISIBLE

            when (it) {
                is CountdownEvent.START -> {
                    title.text = "Modo alerta inicia en"
                    stop.setOnClickListener {
                        ServiceUtil.sendCommand(requireActivity(), CountdownService::class.java, ACTION_STOP_SERVICE)
                    }
                }
                is AlertModeEvent.START -> {
                    title.text = "En modo alerta"
                    stop.setOnClickListener {
                        ServiceUtil.sendCommand(requireActivity(), AlertModeService::class.java, ACTION_STOP_SERVICE)
                    }
                }
                else -> {
                    alertCard.visibility = View.GONE
                    stop.setOnClickListener(null)
                }
            }
        }
    }

    private fun addMillisObserver() {
        viewModel.millis.observe(viewLifecycleOwner) {
            millis.text = TimerUtil.getFormattedTime(it, false)
        }
    }
}