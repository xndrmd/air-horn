package pe.edu.uesan.airhorn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_welcome.*
import pe.edu.uesan.airhorn.utilities.SHARED_PREFERENCES_NAME
import pe.edu.uesan.airhorn.viewmodels.WelcomeViewModel

@AndroidEntryPoint
class WelcomeFragment : Fragment() {
    private val viewModel: WelcomeViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.shouldNavigateToHome.observe(viewLifecycleOwner) {
            if (it) navigateToHome()
        }

        viewModel.shouldNavigateToLogin.observe(viewLifecycleOwner) {
            if (it) navigateToLogin()
        }

        nextBtn.setOnClickListener {
            val username = name.text.toString().trim()

            if (username.isEmpty()) {
                Toast.makeText(requireActivity(), "Es necesario que ingrese su nombre para continuar", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.updateStringPreference(SHARED_PREFERENCES_NAME, username)
            }
        }
    }

    private fun navigateToHome() {
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToHomeFragment()
        requireView().findNavController().navigate(action)
    }

    private fun navigateToLogin() {
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToHomeFragment()
        requireView().findNavController().navigate(action)
    }
}