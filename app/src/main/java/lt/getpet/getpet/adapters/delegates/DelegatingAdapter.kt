package lt.getpet.getpet.adapters.delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

open class DelegatingAdapter(
        private vararg val adapters: DelegateAdapter<out Any, out RecyclerView.ViewHolder>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected var items = listOf<Any>()
        set(value) {
            val diffResult = calculateDiff(field, value)

            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    private fun calculateDiff(oldItems: List<Any>, newItems: List<Any>): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return adapters[getItemViewType(oldItems[oldItemPosition])].areItemsSame(
                        oldItems[oldItemPosition],
                        newItems[newItemPosition]
                )
            }

            override fun getOldListSize(): Int = oldItems.size

            override fun getNewListSize(): Int = newItems.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    oldItems[oldItemPosition] == newItems[newItemPosition]

        })
    }

    private fun getItemViewType(item: Any): Int {
        val index = adapters.indexOfFirst { adapter -> adapter.isForViewType(item) }

        if (index == -1) {
            throw IllegalArgumentException("Unable to find adapter for $item")
        }

        return index
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return adapters[viewType].onCreateViewHolder(parent)
    }

    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        adapters[getItemViewType(position)].onBindViewHolderItem(holder, items[position])
    }

    final override fun getItemViewType(position: Int) = getItemViewType(items[position])

    override fun getItemCount(): Int {
        return items.size
    }
}

@Suppress("AddVarianceModifier")
abstract class DelegateAdapter<ItemType, ViewHolderType : RecyclerView.ViewHolder>(
        private val clazz: Class<ItemType>) {

    open fun isForViewType(item: Any): Boolean = clazz.isInstance(item)

    fun areItemsSame(oldItem: Any, newItem: Any): Boolean {
        if (clazz.isInstance(oldItem) && clazz.isInstance(newItem)) {
            @Suppress("UNCHECKED_CAST")
            return areItemsTheSame(oldItem as ItemType, newItem as ItemType)
        }
        return false
    }

    abstract fun areItemsTheSame(oldItem: ItemType, newItem: ItemType): Boolean

    abstract fun onCreateViewHolder(parent: ViewGroup): ViewHolderType

    abstract fun onBindViewHolder(holder: ViewHolderType, item: ItemType)

    internal fun onBindViewHolderItem(holder: RecyclerView.ViewHolder, item: Any) {
        @Suppress("UNCHECKED_CAST")
        onBindViewHolder(holder as ViewHolderType, item as ItemType)
    }
}


