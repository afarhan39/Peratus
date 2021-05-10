package my.farhan.peratus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import my.farhan.peratus.databinding.ViewPeratusItemBinding

class PeratusAdapter :
    RecyclerView.Adapter<PeratusAdapter.ViewHolder>(), BindableAdapterList<Peratus> {
    private lateinit var peratusList: List<Peratus>
    private var onClickAction: (Peratus, PeratusType) -> Unit = { _, _ -> }

    override fun setData(data: List<Peratus>?) {
        peratusList = data ?: return
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeratusAdapter.ViewHolder {
        return ViewHolder(
            ViewPeratusItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PeratusAdapter.ViewHolder, position: Int) {
        holder.bind(peratusList[position])
    }

    override fun getItemCount(): Int {
        return peratusList.size
    }

    fun setOnClickListener(onClickAction: (Peratus, PeratusType) -> Unit) {
        this.onClickAction = onClickAction
    }

    inner class ViewHolder(
        private val binding: ViewPeratusItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Peratus) = with(binding) {
            binding.item = item
            executePendingBindings()
            binding.tvResultProfit.setOnClickListener {
                onClickAction.invoke(item, PeratusType.Profit)
            }
            binding.tvResultLoss.setOnClickListener {
                onClickAction.invoke(item, PeratusType.Loss)
            }
        }
    }
}
