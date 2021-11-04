package com.example.sqlite.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sqlite.common.adapters.TopicsAdapter
import com.example.sqlite.databinding.ActivityMainBinding
import com.example.sqlite.model.Topic
import com.example.sqlite.utils.DBHelper
import com.example.sqlite.utils.DataManager
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var topicsList = mutableListOf<Topic>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.messagesWithAttachments.setOnClickListener {
            getMessagesWithAttachments()
        }

        binding.usersWithTopics.setOnClickListener {
            getTopicsList()
        }

        binding.messagesWithTimeInterval.setOnClickListener {
            getMessagesWithTimeInterval()
        }

        binding.usersWithEmptyProfiles.setOnClickListener {
            getUserWithEmptyProfiles()
        }
    }

    private fun getMessagesWithAttachments() {
        val sqlRequest = """
            SELECT DISTINCT ${DBHelper.userNameField} FROM ${DBHelper.usersTable}
            INNER JOIN ${DBHelper.messagesTable}
            INNER JOIN ${DBHelper.attachmentsTable}
            ON ${DBHelper.attachmentsTable}.${DBHelper.messageIdField} = ${DBHelper.messagesTable}.${DBHelper.idField} 
            AND ${DBHelper.messagesTable}.${DBHelper.userIdField} = ${DBHelper.usersTable}.${DBHelper.userIdField}
        """.trimIndent()

        DataManager.instance.getDB(this)
            .rawQuery(sqlRequest, null)
            .apply {
                if (count != 0) {
                    moveToFirst()
                    addLineToTextView(
                        getString(getColumnIndexOrThrow(DBHelper.userNameField)),
                        true
                    )

                    while (moveToNext())
                        addLineToTextView(
                            getString(getColumnIndexOrThrow(DBHelper.userNameField)),
                            false
                        )

                    addLineToTextView(count.toString(), false)
                }

                close()
            }
    }

    private fun getTopicsList() {
        val sqlRequest = "SELECT DISTINCT ${DBHelper.topicTitleField} FROM ${DBHelper.topicsTable}"

        DataManager.instance.getDB(this)
            .rawQuery(sqlRequest, null)
            .apply {
                if (count != 0) {
                    moveToFirst()
                    topicsList.add(
                        Topic(
                            getString(getColumnIndexOrThrow(DBHelper.topicTitleField))
                        )
                    )

                    while (moveToNext())
                        topicsList.add(
                            Topic(
                                getString(getColumnIndexOrThrow(DBHelper.topicTitleField))
                            )
                        )
                }

                close()
            }

        binding.topicsRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )

            adapter = TopicsAdapter(
                this@MainActivity,
                topicsList
            ) {
                topicsList[it].isSelected = !topicsList[it].isSelected

                getUsersWithTopics()

                adapter!!.notifyItemChanged(it)
            }
        }
    }

    private fun getUsersWithTopics() {
        var sqlRequest = """
            SELECT DISTINCT ${DBHelper.userNameField} FROM ${DBHelper.usersTable}
            INNER JOIN ${DBHelper.topicsTable}
            INNER JOIN ${DBHelper.usersTopicsTable}
            ON ${DBHelper.topicsTable}.${DBHelper.idField} = ${DBHelper.usersTopicsTable}.${DBHelper.topicIdField}
            AND ${DBHelper.usersTable}.${DBHelper.idField} = ${DBHelper.usersTopicsTable}.${DBHelper.profileIdField}
        """.trimIndent()

        if (topicsList.isNotEmpty()) {
            val topicsArrayString = StringBuilder()
            val selectedTopicsList = topicsList
                .filter { it.isSelected }

            selectedTopicsList.forEach {
                topicsArrayString.append(
                    if (selectedTopicsList.last() != it)
                        "\"${it.topicTitle}\","
                    else
                        "\"${it.topicTitle}\""
                )
            }

            sqlRequest += " WHERE ${DBHelper.topicsTable}.${DBHelper.topicTitleField} IN ($topicsArrayString)"
        }

        DataManager.instance.getDB(this)
            .rawQuery(sqlRequest, null)
            .apply {
                if (count != 0) {
                    moveToFirst()
                    addLineToTextView(
                        getString(getColumnIndexOrThrow(DBHelper.userNameField)),
                        true
                    )

                    while (moveToNext())
                        addLineToTextView(
                            getString(getColumnIndexOrThrow(DBHelper.userNameField)),
                            false
                        )
                }

                close()
            }
    }

    private fun getMessagesWithTimeInterval() {
        val sqlRequest = """
            SELECT DISTINCT text FROM ${DBHelper.messagesTable}
            WHERE ${DBHelper.textField} IS NOT NULL AND ${DBHelper.textField} != "NULL"
        """.trimIndent()

        DataManager.instance.getDB(this)
            .rawQuery(sqlRequest, null)
            .apply {
                if (count != 0) {
                    moveToFirst()
                    addLineToTextView(
                        getString(getColumnIndexOrThrow(DBHelper.textField)),
                        true
                    )

                    while (moveToNext())
                        addLineToTextView(
                            getString(getColumnIndexOrThrow(DBHelper.textField)),
                            false
                        )

                    close()
                }
            }
    }

    private fun getUserWithEmptyProfiles() {
        val sqlRequest = """
            SELECT DISTINCT ${DBHelper.idField} FROM ${DBHelper.usersTable} 
            WHERE (${DBHelper.userNameField} IS "NULL" OR ${DBHelper.userNameField} IS NULL) 
            AND (${DBHelper.userAboutMyselfField} IS "NULL" OR ${DBHelper.userAboutMyselfField} IS NULL)
            AND (${DBHelper.userAvatarField} IS "NULL" OR ${DBHelper.userAvatarField} IS NULL)
        """.trimIndent()

        DataManager.instance.getDB(this)
            .rawQuery(sqlRequest, null)
            .apply {
                if (count != 0) {
                    moveToFirst()
                    addLineToTextView(
                        getString(getColumnIndexOrThrow(DBHelper.idField)),
                        true
                    )

                    while (moveToNext())
                        addLineToTextView(
                            getString(getColumnIndexOrThrow(DBHelper.idField)),
                            false
                        )

                    addLineToTextView(count.toString(), false)
                }

                close()
            }
    }

    private fun addLineToTextView(lineText: String, withClear: Boolean) {
        if (withClear)
            binding.mainTextView.text = null

        binding.mainTextView.text = String.format(
            Locale.getDefault(),
            "%s\n%s",
            binding.mainTextView.text,
            lineText
        )
    }
}