package rs.raf.jun.presentation.view.recycler.diff

import androidx.recyclerview.widget.DiffUtil
import rs.raf.jun.data.models.news.News

class NewsDiffCallback: DiffUtil.ItemCallback<News>()  {

    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem.date == newItem.date && oldItem.title == newItem.title &&
                oldItem.image == newItem.image && oldItem.link == newItem.link && oldItem.content == newItem.content
    }

    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem.date == newItem.date && oldItem.title == newItem.title &&
                oldItem.image == newItem.image && oldItem.link == newItem.link && oldItem.content == newItem.content
    }

}