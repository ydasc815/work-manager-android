package com.aditya.workmanager.workmanager

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.aditya.workmanager.BuildConfig
import com.aditya.workmanager.ContactItem
import java.util.*
import kotlin.collections.HashSet

class WorkerTask(context: Context, workerParams: WorkerParameters): Worker(context, workerParams) {

    override fun doWork(): Result {
        println("Task Worker Called >>> ")
        fetchContactData()
        Log.d("worker >> ", "background task executions complete !!")
        return Result.success()
    }

    private fun fetchContactData() {
        val numbers: HashSet<String> = HashSet()
        val names: HashSet<String> = HashSet()
        val contactList: ArrayList<ContactItem> = ArrayList<ContactItem>()
        val phones: Cursor? = applicationContext.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, "upper(${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME})ASC LIMIT " +
                "100000 OFFSET 0")
        while (true) {
            if (BuildConfig.DEBUG && phones == null) {
                error("Assertion failed")
            }
            if (!phones!!.moveToNext()) break
            val name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            var phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            phoneNumber = phoneNumber.replace("\\s".toRegex(), "")
            phoneNumber = phoneNumber.replace("-".toRegex(), "")
            if (numbers.contains(phoneNumber) && names.contains(name)) continue
            numbers.add(phoneNumber)
            names.add(name)
            contactList.add(ContactItem(0, name, phoneNumber))
        }
        for(item : ContactItem in contactList) {
            Log.d("contact item"," @name = : ${item.contactName}")
        }
    }


}