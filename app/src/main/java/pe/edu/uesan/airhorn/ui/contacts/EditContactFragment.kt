package pe.edu.uesan.airhorn.ui.contacts

import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import pe.edu.uesan.airhorn.R
import pe.edu.uesan.airhorn.contentresolver.ContactContentResolver
import pe.edu.uesan.airhorn.models.ContactInfo
import pe.edu.uesan.airhorn.viewmodels.EditContactViewModel
import javax.inject.Inject

@AndroidEntryPoint
class EditContactFragment : Fragment() {
    private val args: EditContactFragmentArgs by navArgs()

    @Inject
    lateinit var editContactViewModelFactory: EditContactViewModel.AssistedFactory

    private val viewModel: EditContactViewModel by viewModels {
        EditContactViewModel.provideFactory(
            editContactViewModelFactory,
            args.contactContentId
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.contact.observe(viewLifecycleOwner){ applyInfo(it) }
    }

    private fun applyInfo(contactInfo: ContactInfo) {
        println("being called :eyes:")
        val name: TextView = requireView().findViewById(R.id.name)
        val picture: ImageView = requireView().findViewById(R.id.picture)

        name.text = contactInfo.name

        if (contactInfo.photoUri != null) {
            picture.visibility = View.VISIBLE
            picture.setImageBitmap(ContactContentResolver.photo(requireContext(), contactInfo.name, contactInfo.photoUri))
        }

        addDynamicInfo(contactInfo)
    }

    private fun addDynamicInfo(contactInfo: ContactInfo) {
        val phoneInfoLayout: LinearLayout = requireView().findViewById(R.id.phone_info_layout)
        phoneInfoLayout.removeAllViews()

        contactInfo.phoneNumbers.forEach { phoneNumber ->
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)

            val layout = LinearLayout(requireView().context)
            layout.orientation = LinearLayout.HORIZONTAL

            val textView = TextView(context)
            textView.text = phoneNumber.number
            textView.setTextAppearance(R.style.TextAppearance_MaterialComponents_Body2)

            layout.addView(textView, params)

            if (phoneNumber.id == null) {
                val button = MaterialButton(ContextThemeWrapper(requireContext(), R.style.Widget_MaterialComponents_Button_Icon))
                button.text = "Agregar"
                button.setIconResource(R.drawable.ic_add_24)
                button.setOnClickListener {
                    viewModel.create(phoneNumber.contentId, contactInfo.id)
                }

                layout.addView(button)
            } else {
                val button = MaterialButton(ContextThemeWrapper(requireContext(), R.style.Widget_MaterialComponents_Button_TextButton_Icon))
                button.text = "Eliminar"
                button.setIconResource(R.drawable.ic_remove_24)
                button.setOnClickListener {
                    viewModel.delete(phoneNumber)
                }

                layout.addView(button)
            }

            phoneInfoLayout.addView(layout, params)
        }
    }
}