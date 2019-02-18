//package com.theandroiddev.mywins.utils
//
//import android.view.View
//import android.widget.CheckBox
//import android.widget.TextView
//
//class CheckboxItem(
//    private val categoryCheckboxItemModel: CategoryCheckboxItemModel
//) : AbstractFlexibleItem<CategoryCheckboxItem.ViewHolder>(), IHolder<CategoryCheckboxItemModel> {
//
//    override fun getModel(): CategoryCheckboxItemModel = categoryCheckboxItemModel
//
//    override fun getLayoutRes(): Int = R.layout.recycler_holder_category_checkbox_item
//
//    override fun equals(other: Any?): Boolean =
//        other is CategoryCheckboxItem && other.model.categoryName == this.model.categoryName
//
//    override fun hashCode(): Int = this.model.categoryName.hashCode()
//
//    override fun createViewHolder(
//        view: View,
//        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
//    ): ViewHolder = ViewHolder(view, adapter)
//
//    override fun bindViewHolder(
//        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>,
//        holder: ViewHolder,
//        position: Int,
//        payloads: MutableList<Any>?
//    ) {
//        holder.bind(model)
//    }
//
//    inner class ViewHolder(
//        val view: View,
//        val adapter: FlexibleAdapter<out IFlexible<*>>
//    ) : FlexibleViewHolder(view, adapter) {
//
//        private val checkbox: CheckBox = view.recycler_holder_category_checkbox_checkbox
//        private val categoryName: TextView = view.recycler_holder_category_checkbox_category_name
//
//        fun bind(model: CategoryCheckboxItemModel) {
//            checkbox.isChecked = model.selected
//            categoryName.text = model.categoryName
//            view.setOnClickListener {
//                val selected = !checkbox.isChecked
//                checkbox.isChecked = selected
//                model.selected = selected
//            }
//        }
//
//    }
//}
