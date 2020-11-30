package br.com.devsrsouza.chucknorrisfacts.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

typealias InflateBindingCallback<BINDING> = (LayoutInflater, ViewGroup, viewType: Int) -> BINDING
typealias GetViewFromBindingCallback<BINDING> = (BINDING) -> View
typealias OnBindCallback<MODEL, BINDING> = (MODEL, BINDING, position: Int) -> Unit
typealias DataClassComparator<MODEL> = (oldItem: MODEL, newItem: MODEL) -> Boolean
typealias ItemViewType<MODEL> = (MODEL) -> Int

inline fun <MODEL : Any, BINDING : ViewBinding> KotlinListAdapter(
    noinline inflateBinding: InflateBindingCallback<BINDING>,
    noinline getViewFromBinding: GetViewFromBindingCallback<BINDING>,
    noinline onBind: OnBindCallback<MODEL, BINDING>,
    noinline isDataClassItemsTheSame: DataClassComparator<MODEL>,
    noinline itemViewType: (MODEL) -> Int = { 0 }
) = KotlinListAdapter(
    inflateBinding,
    getViewFromBinding,
    onBind,
    isDataClassItemsTheSame,
    { old, new -> old.equals(new)},
    itemViewType
)

class KotlinListAdapter<MODEL, BINDING : ViewBinding>(
    val inflateBinding: InflateBindingCallback<BINDING>,
    val getViewFromBinding: GetViewFromBindingCallback<BINDING>,
    val onBind: OnBindCallback<MODEL, BINDING>,
    val isDataClassItemsTheSame: DataClassComparator<MODEL>,
    val isDataClassContentTheSame: DataClassComparator<MODEL>,
    val itemViewType: ItemViewType<MODEL>
) : ListAdapter<MODEL, KotlinListAdapter.ViewHolder<MODEL, BINDING>>(
    KotlinDiffCallback<MODEL>(
        isDataClassItemsTheSame, isDataClassContentTheSame
    )
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<MODEL, BINDING> {
        return ViewHolder.from(
            viewType,
            parent,
            inflateBinding,
            getViewFromBinding,
            onBind
        )
    }

    override fun getItemViewType(position: Int): Int {
        return itemViewType(getItem(position))
    }

    override fun onBindViewHolder(
        holder: ViewHolder<MODEL, BINDING>,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class ViewHolder<MODEL, BINDING> private constructor(
        val binding: BINDING,
        val getViewFromBinding: GetViewFromBindingCallback<BINDING>,
        val onBind: OnBindCallback<MODEL, BINDING>
    ) : RecyclerView.ViewHolder(getViewFromBinding(binding)) {

        fun bind(dataClass: MODEL) {
            onBind(dataClass, binding, adapterPosition)
        }

        companion object {
            fun <MODEL, BINDING> from(
                viewType: Int,
                parent: ViewGroup,
                inflateBinding: InflateBindingCallback<BINDING>,
                getViewFromBinding: GetViewFromBindingCallback<BINDING>,
                onBind: OnBindCallback<MODEL, BINDING>
            ): ViewHolder<MODEL, BINDING> {
                val inflater = LayoutInflater.from(parent.context)
                val binding = inflateBinding(inflater, parent, viewType)

                return ViewHolder(
                    binding,
                    getViewFromBinding,
                    onBind
                )
            }
        }
    }

    class KotlinDiffCallback<MODEL>(
        val isDataClassItemsTheSame: DataClassComparator<MODEL>,
        val isDataClassContentTheSame: DataClassComparator<MODEL>
    ) : DiffUtil.ItemCallback<MODEL>() {
        override fun areItemsTheSame(
            oldItem: MODEL,
            newItem: MODEL
        ) = isDataClassItemsTheSame(oldItem, newItem)

        override fun areContentsTheSame(
            oldItem: MODEL,
            newItem: MODEL
        ) = isDataClassContentTheSame(oldItem, newItem)
    }
}