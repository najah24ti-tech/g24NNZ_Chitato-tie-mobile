
package com.example.nnz_chitato

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

data class HomeMenu(
    val name: String,
    val icon: String
)

class HomeMenuAdapter(
    private val context: Context,
    private val menuList: List<HomeMenu>
) : BaseAdapter() {

    override fun getCount(): Int = menuList.size

    override fun getItem(position: Int): HomeMenu {
        return menuList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_home_menu, parent, false)

        val txtIcon = view.findViewById<TextView>(R.id.txtMenuIcon)
        val txtName = view.findViewById<TextView>(R.id.txtMenuName)

        val menu = getItem(position)

        txtIcon.text = menu.icon
        txtName.text = menu.name

        return view
    }
}
