package lt.getpet.getpet.adapters.delegates

import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.label.view.*
import lt.getpet.getpet.R
import lt.getpet.getpet.extensions.inflate

class LabelDelegateAdapter : DelegateAdapter<LabelDelegateAdapter.LabelItem,
        LabelDelegateAdapter.LabelViewHolder>(LabelItem::class.java) {

    override fun onCreateViewHolder(parent: ViewGroup) = LabelViewHolder(parent)

    override fun onBindViewHolder(holder: LabelViewHolder, item: LabelItem) {
        holder.bind(item)
    }

    override fun areItemsTheSame(oldItem: LabelItem, newItem: LabelItem): Boolean {
        return oldItem == newItem
    }

    data class LabelItem(@StringRes val stringRes: Int)

    class LabelViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.label)) {

        fun bind(item: LabelItem) = itemView.run {
            label.setText(item.stringRes)
        }
    }
}
