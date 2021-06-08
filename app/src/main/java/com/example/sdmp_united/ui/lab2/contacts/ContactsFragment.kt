package com.example.sdmp_united.ui.lab2.contacts

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sdmp_united.lab2.ContactData
import com.example.sdmp_united.lab2.ContactItemAdapter
import com.example.sdmp_united.R
import com.example.sdmp_united.ui.lab2.map.MapFragment

import kotlinx.android.synthetic.main.fragment_contacts.*

class ContactsFragment : Fragment() {

    private lateinit var contactsViewModel: ContactsViewModel
    var contactsPermission = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        contactsViewModel =
            ViewModelProvider(this).get(ContactsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_contacts, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!contactsPermission) {
            requestPermision()
            try {
                setupListofDataIntoRecyclerView()
            }
            catch (e : Exception)
            {
                val toast = Toast.makeText(context, "There is no permission to read contacts", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
        else
        {
            setupListofDataIntoRecyclerView()
        }
    }

    private fun requestPermision() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.READ_CONTACTS),
                ContactsFragment.CONTACTS_REQUEST_CODE
            )
        } else {
            contactsPermission = true
        }
    }

    /**
     * Function is used to show the list on UI of inserted data.
     */
    private fun setupListofDataIntoRecyclerView() {
        if (getContactList().size > 0) {

            contactItemsList.visibility = View.VISIBLE
            contactNoRecordsAvailable.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            contactItemsList.layoutManager = LinearLayoutManager(requireContext())
            // Adapter class is initialized and list is passed in the param.
            val contactItemAdapter = ContactItemAdapter(this, requireContext(), getContactList())
            // adapter instance is set to the recyclerview to inflate the items.
            contactItemsList.adapter = contactItemAdapter
        } else {

            contactItemsList.visibility = View.GONE
            contactNoRecordsAvailable.visibility = View.VISIBLE
        }
    }

    private fun getContactList() : ArrayList<ContactData> {
        val res = ArrayList<ContactData>()
        val cr = requireContext().contentResolver
        val cur: Cursor? = cr.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )
        if ((if (cur != null) cur.getCount() else 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                val id: String = cur.getString(
                    cur.getColumnIndex(ContactsContract.Contacts._ID)
                )
                val name: String = cur.getString(
                    cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME
                    )
                )
                if (cur.getInt(
                        cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER
                        )
                    ) > 0
                ) {
                    val pCur: Cursor? = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    if (pCur != null) {
                        while (pCur.moveToNext()) {
                            var phoneNo: String = pCur.getString(
                                pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER
                                )
                            )
                            phoneNo = phoneNo.replace(" ", "")
                            phoneNo = phoneNo.replace("(", "")
                            phoneNo = phoneNo.replace(")", "")
                            if (phoneNo.contains("00"))
                            {
                                val tmp = ContactData(name, phoneNo)
                                res.add(tmp)
                            }
                            Log.i(ContentValues.TAG, "Name: $name")
                            Log.i(ContentValues.TAG, "Phone Number: $phoneNo")
                        }
                    }
                    if (pCur != null) {
                        pCur.close()
                    }
                }
            }
        }
        if (cur != null) {
            cur.close()
        }
        return res
    }

    companion object {
        //to get location permissions.
        private const val CONTACTS_REQUEST_CODE = 23
    }
}