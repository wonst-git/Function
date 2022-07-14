package com.jdroid.contactsex.ui

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.jdroid.contactsex.data.ContactsData
import com.jdroid.contactsex.databinding.ActivityMainBinding
import com.jdroid.contactsex.ui.adapter.ContactsAdapter

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val PERMISSION_REQUEST_CODE = 100
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var contactsList = ArrayList<ContactsData>()
    private var checkedList: Array<Boolean>? = null
    private var preferences: SharedPreferences? = null
    private var isDeleteMode = false
    private var contactsAdapter: ContactsAdapter? = null

    private val onItemClickListener = object : ContactsAdapter.OnItemClickListener {
        override fun onItemClickListener(position: Int) {
            if (isDeleteMode) {

            } else {
                val intent = Intent(this@MainActivity, ContactsAddEditActivity::class.java)
                    .putExtra("contactsData", contactsList[position])
                startActivity(intent)
            }
        }

        override fun onItemLongClickListener(position: Int) {
            isDeleteMode = true
            setModeUI()
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnAddContacts -> {

            }
            binding.btnPermission -> {
                requestPermission()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferences = getSharedPreferences("contacts", MODE_PRIVATE)

        initLayout()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        onCheckContactsPermission()
    }

    private fun initLayout() {
        setContentView(binding.root)
        binding.includeTitle.txtTitle.text = "주소록"
    }

    private fun initListener() {
        binding.btnPermission.setOnClickListener(this)
        binding.btnAddContacts.setOnClickListener(this)
        binding.includeTitle.btnDelete.setOnClickListener(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (shouldShowRequestPermissionRationale(permissions[0])) {
            Toast.makeText(this, "shouldshow", Toast.LENGTH_SHORT).show()
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("권한")
                setMessage("권한을 허용하기 위해서 설정으로 이동합니다.")
                setPositiveButton("확인") { _, _ ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", packageName, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    startActivity(intent)
                }
                setNegativeButton("거절") { dialog, _ ->
                    dialog.dismiss()
                }
                show()
            }
        }

        onCheckContactsPermission()
    }

    override fun onBackPressed() {
        if (isDeleteMode) {
            isDeleteMode = false
            setModeUI()
        } else {
            super.onBackPressed()
        }
    }

    private fun onCheckContactsPermission() {
        val permissionDenied = checkSelfPermission(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_DENIED
                || checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED

        binding.btnPermission.isVisible = permissionDenied
        binding.txtDescription.isVisible = permissionDenied
        binding.btnAddContacts.isVisible = !permissionDenied
        binding.contactsList.isVisible = !permissionDenied
        if (permissionDenied) {
            binding.txtDescription.text = "권한을 허용하셔야 이용하실 수 있습니다."
        } else {
            getContactsList()
            setContacts()
        }
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS), PERMISSION_REQUEST_CODE)
    }

    private fun getContactsList() {
        val contacts = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        val list = ArrayList<ContactsData>()
        contacts?.let {
            while (it.moveToNext()) {
                val contactsId = contacts.getInt(contacts.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                val name = contacts.getString(contacts.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val number = contacts.getString(contacts.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                list.add(ContactsData(contactsId, name, number))
            }
        }
        contactsList = list
        setContacts()
    }

    private fun setContacts() {
        checkedList = Array(contactsList.size) { false }
        contactsAdapter = ContactsAdapter(contactsList, onItemClickListener, checkedList)
        binding.contactsList.adapter = contactsAdapter
    }

    private fun setModeUI() {
        binding.includeTitle.btnDelete.isVisible = isDeleteMode
        binding.btnAddContacts.isVisible = !isDeleteMode
        contactsAdapter?.setDeleteMode(isDeleteMode)
    }
}