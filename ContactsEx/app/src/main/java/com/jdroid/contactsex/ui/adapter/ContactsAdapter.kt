package com.jdroid.contactsex.ui.adapter

import android.telephony.PhoneNumberUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.jdroid.contactsex.data.ContactsData
import com.jdroid.contactsex.databinding.ViewContactsListBinding
import java.util.*

class ContactsAdapter(private val contactsList: ArrayList<ContactsData>, private val onItemClickListener: OnItemClickListener, private val checkedList: MutableSet<Int>)
    : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClickListener(position: Int)
        fun onItemLongClickListener(position: Int)
    }

    private var isDeleteMode = false
    private var longPosition = -1

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

            if (isDeleteMode) {
                binding.checkbox.isChecked = checkedList.any { adapterPosition == it }
            } else {
                checkedList.clear()
                binding.checkbox.isChecked = false
                longPosition = -1
            }

            if (longPosition == adapterPosition) {
                binding.checkbox.isChecked = true
                checkedList.add(adapterPosition)
            }

            itemView.setOnClickListener {
                if (isDeleteMode) {
                    binding.checkbox.performClick()
                    if (binding.checkbox.isChecked) {
                        checkedList.add(adapterPosition)
                    } else {
                        checkedList.remove(adapterPosition)
                    }
                } else {
                    onItemClickListener.onItemClickListener(adapterPosition)
                }
            }
            itemView.setOnLongClickListener {
                checkedList.add(adapterPosition)
                onItemClickListener.onItemLongClickListener(adapterPosition)
                return@setOnLongClickListener false
            }
        }
    }
}