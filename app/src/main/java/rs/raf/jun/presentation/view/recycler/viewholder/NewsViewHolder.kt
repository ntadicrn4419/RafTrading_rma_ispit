package rs.raf.jun.presentation.view.recycler.viewholder
import androidx.recyclerview.widget.RecyclerView
import coil.load
import rs.raf.jun.data.models.news.News
import rs.raf.jun.databinding.LayoutItemNewsBinding

class NewsViewHolder (
    private val itemBinding: LayoutItemNewsBinding,
    private val funClickListener: (position: Int) -> Unit
) : RecyclerView.ViewHolder(itemBinding.root) {

    init{
        itemBinding.cardView.setOnClickListener{
            funClickListener(layoutPosition)
        }
    }

    fun bind(news: News){
        itemBinding.newsTitle.text = news.title
        itemBinding.newsDate.text = parseDate(news.date)
        itemBinding.newsImg.load(news.image)
    }

    private fun parseDate(inputDate: String): String{
        return inputDate.subSequence(0,10).toString()
    }
}