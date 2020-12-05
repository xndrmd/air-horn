package pe.edu.uesan.airhorn.ui.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import pe.edu.uesan.airhorn.R
import pe.edu.uesan.airhorn.adapters.ContactAdapter
import pe.edu.uesan.airhorn.utilities.REQUEST_READ_CONTACTS
import pe.edu.uesan.airhorn.viewmodels.ContactListViewModel

@AndroidEntryPoint
class ContactListFragment : Fragment() {
    private val viewModel: ContactListViewModel by viewModels()
    private lateinit var adapter:ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.contact_menu, menu)
        val search = menu.findItem(R.id.search)
        val searchView: SearchView = search.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ContactAdapter {
            val action = ContactListFragmentDirections.actionContactListFragmentToEditContactFragment(it.contentId)
            view.findNavController().navigate(action)
        }

        val contactsList: RecyclerView = view.findViewById(R.id.contact_list)
        contactsList.layoutManager = LinearLayoutManager(view.context)
        contactsList.adapter = adapter

        val addContact: View = view.findViewById(R.id.btn_add_contact)
        addContact.setOnClickListener {
            val action = ContactListFragmentDirections.actionContactListFragmentToAddContactFragment()
            view.findNavController().navigate(action)
        }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            subscribe()
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_READ_CONTACTS)
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                subscribe()
            } else {
                // do shomething else
            }
        }
    }

    private fun subscribe() {
        viewModel.contacts.observe(viewLifecycleOwner) { adapter.setList(it) }
    }
}