package pe.edu.uesan.airhorn.ui.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pe.edu.uesan.airhorn.R

class ContactViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.name);
    private val phoneNumber: TextView = view.findViewById(R.id.phone_number);
    private val email: TextView = view.findViewById(R.id.email);


    fun bin(contact: Contact) {
        name.text = contact.name
        phoneNumber.text = contact.phoneNumber
        email.text = contact.email
    }
}

val diffCallback = object: DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}

class ContactAdapter: ListAdapter<Contact, ContactViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bin(getItem(position))
    }
}
