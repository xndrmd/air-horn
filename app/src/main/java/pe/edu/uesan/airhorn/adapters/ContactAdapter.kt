package pe.edu.uesan.airhorn.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import pe.edu.uesan.airhorn.R
import pe.edu.uesan.airhorn.contentresolver.ContactContentResolver
import pe.edu.uesan.airhorn.models.ContactInfo
import java.util.*

class ContactViewHolder(private val view: View, private val navigation: (contactInfo: ContactInfo) -> Unit): RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.name);
    private val picture: ShapeableImageView = view.findViewById(R.id.picture)

    fun bin(contactInfo: ContactInfo) {
        name.text = contactInfo.name
        picture.setImageBitmap(ContactContentResolver.photo(view.context, contactInfo.name, contactInfo.photoThumbnailUri))

        view.setOnClickListener { navigation(contactInfo) }
    }
}

val diffCallback = object: DiffUtil.ItemCallback<ContactInfo>() {
    override fun areItemsTheSame(oldItem: ContactInfo, newItem: ContactInfo): Boolean {
        return oldItem.contentId == newItem.contentId
    }

    override fun areContentsTheSame(oldItem: ContactInfo, newItem: ContactInfo): Boolean {
        return oldItem == newItem
    }
}

class ContactAdapter(private val navigation: (contactInfo: ContactInfo) -> Unit): ListAdapter<ContactInfo, ContactViewHolder>(diffCallback), Filterable {
    private val listAll: MutableList<ContactInfo> = mutableListOf()

    fun setList(list: List<ContactInfo>) {
        listAll.clear()
        listAll.addAll(list)

        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view, navigation)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bin(getItem(position))
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val searchTerm = constraint.toString().toLowerCase(Locale.ROOT)
                val filteredList: MutableList<ContactInfo> = mutableListOf()

                if (searchTerm.isEmpty()) {
                    filteredList.addAll(listAll)
                } else {
                    filteredList.addAll(listAll.filter { it.name.toLowerCase(Locale.ROOT).contains(searchTerm) })
                }

                results.values = filteredList

                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null) submitList(results.values as MutableList<ContactInfo>)
            }
        }
    }
}
