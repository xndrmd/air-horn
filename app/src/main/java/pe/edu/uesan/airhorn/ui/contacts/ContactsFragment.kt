package pe.edu.uesan.airhorn.ui.contacts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.edu.uesan.airhorn.R

class ContactsFragment : Fragment() {
    private val adapter = ContactAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contactsList: RecyclerView = view.findViewById(R.id.contact_list)
        contactsList.layoutManager = LinearLayoutManager(view.context)
        contactsList.adapter = adapter

        val data = listOf(
            Contact("Alex", "+51 945 070511", "alxndr.md@gmail.com"),
            Contact("Renzo", "+51 945 070511", "alxndr.md@gmail.com"),
            Contact("Angel", "+51 945 070511", "alxndr.md@gmail.com"),
            Contact("Diego", "+51 945 070511", "alxndr.md@gmail.com"),
            Contact("Roy", "+51 945 070511", "alxndr.md@gmail.com"),
            Contact("Xander", "+51 945 070511", "alxndr.md@gmail.com"),
            Contact("Paolo", "+51 945 070511", "alxndr.md@gmail.com")
        )

        adapter.submitList(data)
    }
}