package pe.edu.uesan.airhorn.ui.contacts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import pe.edu.uesan.airhorn.R
import pe.edu.uesan.airhorn.adapters.ContactAdapter
import pe.edu.uesan.airhorn.viewmodels.ContactListAllViewModel

@AndroidEntryPoint
class AddContactFragment : Fragment() {
    private val viewModel: ContactListAllViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ContactAdapter {
            val action = AddContactFragmentDirections.actionAddContactFragmentToEditContactFragment(it.contentId)
            view.findNavController().navigate(action)
        }

        val contactsList: RecyclerView = view.findViewById(R.id.contact_list)
        contactsList.layoutManager = LinearLayoutManager(view.context)
        contactsList.adapter = adapter

        subscribe(adapter)
    }

    private fun subscribe(adapter: ContactAdapter) {
        viewModel.contacts.observe(viewLifecycleOwner) { adapter.setList(it) }
    }
}