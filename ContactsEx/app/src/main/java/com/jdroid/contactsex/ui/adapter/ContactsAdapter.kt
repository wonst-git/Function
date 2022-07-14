package com.jdroid.contactsex.ui.adapter

import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.jdroid.contactsex.data.ContactsData
import com.jdroid.contactsex.databinding.ViewContactsListBinding
import java.util.*

class ContactsAdapter(private val contactsList: ArrayList<ContactsData>, private val onItemClickListener: OnItemClickListener, private val checkedList: Array<Boolean>?) : RecyclerView
.Adapter<ContactsAdapter
.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClickListener(position: Int)
        fun onItemLongClickListener(position: Int)
    }

    private var isDeleteMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ViewContactsListBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = contactsList.size

    fun setDeleteMode(deleteMode: Boolean) {
        isDeleteMode = deleteMode
        notifyItemRangeChanged(0, itemCount)
    }

    inner class ViewHolder(private val binding: ViewContactsListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.txtName.text = contactsList[adapterPosition].name
            binding.txtPhoneNumber.text = PhoneNumberUtils.formatNumber(contactsList[adapterPosition].number, Locale.getDefault().country)

            binding.checkbox.isVisible = isDeleteMode

            itemView.setOnClickListener {
                if (isDeleteMode) {
                    binding.checkbox.performClick()
                    checkedList?.get(adapterPosition)?.let {
                        checkedList[adapterPosition] = binding.checkbox.isChecked
                    }
                } else {
                    onItemClickListener.onItemClickListener(adapterPosition)
                }
            }
            itemView.setOnLongClickListener {
                onItemClickListener.onItemLongClickListener(adapterPosition)
                return@setOnLongClickListener false
            }
        }

    }
}