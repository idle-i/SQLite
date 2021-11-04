package com.example.sqlite.common.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlite.R
import com.example.sqlite.databinding.TopicItemBinding
import com.example.sqlite.model.Topic

class TopicsAdapter(
    private val context: Context,
    private val topics: List<Topic>,
    private val onSelect: (position: Int) -> Unit
) : RecyclerView.Adapter<TopicsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater
            .from(context)
            .inflate(R.layout.topic_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = TopicItemBinding.bind(holder.itemView)
        val topic = topics[position]

        binding.root.apply {
            text = topic.topicTitle

            setTextColor(
                context.getColor(
                    if (topic.isSelected)
                        R.color.colorWhite
                    else
                        R.color.colorBlack
                )
            )

            background = AppCompatResources.getDrawable(
                context,
                if (topic.isSelected)
                    R.drawable.topic_selected
                else
                    R.drawable.topic_not_selected
            )

            setOnClickListener {
                onSelect(position)
            }
        }
    }

    override fun getItemCount() = topics.size
}