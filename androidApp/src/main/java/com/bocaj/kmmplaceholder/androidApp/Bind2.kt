package com.bocaj.kmmplaceholder.androidApp

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Created by Radoslav Yankov on 29.06.2018
 * radoslavyankov@gmail.com
 *
 * Modified to use ViewBinding which is cleaner and safer than Android experimental extensions.
 * After some initial use in dev hopefully the other version can be removed.
 */

/**
 * Dynamic list bind function. It should be followed by one or multiple .map calls.
 * @param items - Generic list of the items to be displayed in the list
 */
fun <T> RecyclerView.bind2(items: List<T>): ViewBinderListAdapter<T> {
    layoutManager = LinearLayoutManager(context)
    return ViewBinderListAdapter(items.toMutableList(), this)
}

/**
 * Simple list bind function.
 * @param items - Generic list of the items to be displayed in the list
 * @param singleLayout - The layout that will be used in the list
 * @param singleBind - The "binding" function between the item and the layout. This is the standard "bind" function in traditional ViewHolder classes. It uses Kotlin Extensions
 * so you can just use the XML names of the views inside your layout to address them.
 */
fun <T> RecyclerView.bind2(items: List<T>, singleLayout: (parent: ViewGroup, type: Int) -> ViewBinding, singleBind: (ViewBinding.(item: T) -> Unit)): ViewBinderListAdapter<T> {
    layoutManager = LinearLayoutManager(context)
    return ViewBinderListAdapter(items.toMutableList(), this
    ).map(singleLayout, {item: T, idx: Int ->  true }, singleBind)
}

/**
 * Updates the list using DiffUtils.
 * @param newItems the new list which is to replace the old one.
 *
 * NOTICE: The comparator currently checks if items are literally the same. You can change that if you want,
 * by changing the lambda in the function
 */
fun <T> RecyclerView.update2(newItems: List<T>) {
    (adapter as? ViewBinderListAdapter<T>)?.update(newItems) { o, n, _ -> o == n }
}

open class ViewBinderListAdapter<T>(private var items: MutableList<T>, private var list: RecyclerView?=null)
    : RecyclerView.Adapter<ViewBinderListViewHolder<T>>() {

    private inner class BindMap(val lf: (parent: ViewGroup, type: Int) -> ViewBinding, var type: Int = 0, val bind: ViewBinding.(item: T) -> Unit, val predicate: (item: T, idx : Int) -> Boolean)
    private var bindMap = mutableListOf<BindMap>()
    private var typeCounter = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewBinderListViewHolder<T> {
        return bindMap.first { it.type == viewType }.let {
            return ViewBinderListViewHolder(it.lf(parent, viewType), viewType)
        }
    }

    override fun onBindViewHolder(holder: ViewBinderListViewHolder<T>, position: Int) {
        val item = items.get(position)
        holder.bind(item, bindMap.first { it.type == holder.holderType }.bind)
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = try {
        bindMap.first { it.predicate(items[position], position) }.type
    } catch (e: Exception) {
        0
    }

    /**
     * The function used for mapping types to layouts
     * @param layoutFactory - factory that creates the view for this adapter
     * @param predicate - Function used to sort the items. For example, a Type field inside your items class with different values for different types.
     * @param bind - The "binding" function between the item and the layout. This is the standard "bind" function in traditional ViewHolder classes. It uses Kotlin Extensions
     * so you can just use the XML names of the views inside your layout to address them.
     */
    fun map(layoutFactory: (parent: ViewGroup, type: Int) -> ViewBinding, predicate: (item: T, idx : Int) -> Boolean, bind: ViewBinding.(item: T) -> Unit): ViewBinderListAdapter<T> {
        bindMap.add(BindMap(layoutFactory, typeCounter++, bind, predicate))
        list?.adapter = this
        return this
    }

    /**
     * Sets up a layout manager for the recycler view.
     */
    fun layoutManager(manager: RecyclerView.LayoutManager): ViewBinderListAdapter<T> {
        list!!.layoutManager = manager
        return this
    }

    fun update(newList: List<T>, compare: (T, T, Boolean) -> Boolean) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return compare(items[oldItemPosition], newList[newItemPosition], false)
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return compare(items[oldItemPosition], newList[newItemPosition], true)
            }

            override fun getOldListSize() = items.size

            override fun getNewListSize() = newList.size
        })
        if (newList is MutableList)
            items = newList
        else
            items = newList.toMutableList()
        diff.dispatchUpdatesTo(this)
    }

}

class ViewBinderListViewHolder<T>(val binding: ViewBinding, val holderType: Int) : RecyclerView.ViewHolder(binding.root)
{
    fun bind(entry: T, func: ViewBinding.(item: T) -> Unit) {
        binding.func(entry)
    }
}