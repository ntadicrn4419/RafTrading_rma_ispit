package rs.raf.jun.presentation.view.recycler.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import rs.raf.jun.data.models.news.News
import rs.raf.jun.databinding.LayoutItemNewsBinding
import rs.raf.jun.presentation.view.recycler.diff.NewsDiffCallback
import rs.raf.jun.presentation.view.recycler.viewholder.NewsViewHolder

class NewsAdapter (
    private val funClickListener: (news: News) -> Unit
): ListAdapter<News, NewsViewHolder>(NewsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemBinding = LayoutItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(itemBinding, {this.funClickListener(getItem(it))})
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}