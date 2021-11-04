package com.example.sqlite.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class DBHelper constructor(private var context: Context) :
    SQLiteOpenHelper(context, dbName, null, dbVersion) {

    companion object {
        const val dbName = "db.db"
        const val dbVersion = 1

        const val usersTable = "profile"
        const val attachmentsTable = "attachments"
        const val messagesTable = "messages"
        const val topicsTable = "topic"
        const val usersTopicsTable = "profile_topic"

        const val idField = "id"
        const val userIdField = "user_id"
        const val userNameField = "user_name"
        const val userAboutMyselfField = "about_myself"
        const val profileIdField = "profile_id"

        const val userAvatarField = "avatar"
        const val messageIdField = "message_id"

        const val textField = "text"

        const val topicIdField = "topic_id"
        const val topicTitleField = "topic_title"
    }

    private var dbPath = ""

    init {
        dbPath = context.applicationInfo.dataDir + "/databases/"

        copyDB()
    }

    private fun copyDB() {
        val dir = File(dbPath)
        if (!dir.exists()) {
            dir.mkdir()

            File(dbPath + dbName)
                .createNewFile()
        }

        val inputStream: InputStream = context.assets.open(dbName)
        val outputStream: OutputStream = FileOutputStream(dbPath + dbName)

        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0)
            outputStream.write(buffer, 0, length)

        outputStream.flush()
        outputStream.close()

        inputStream.close()
    }

    override fun onCreate(p0: SQLiteDatabase?) {
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}