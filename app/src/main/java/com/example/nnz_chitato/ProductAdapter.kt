
package com.example.nnz_chitato

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private var products: List<Product>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val icon: TextView =
            itemView.findViewById(R.id.txtProductIcon)

        val name: TextView =
            itemView.findViewById(R.id.txtProductName)

        val category: TextView =
            itemView.findViewById(R.id.txtProductCategory)

        val salesInfo: TextView =
            itemView.findViewById(R.id.txtProductPrice)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        val product = products[position]

        holder.icon.text = product.icon
        holder.name.text = product.name
        holder.category.text = product.category
        holder.salesInfo.text = product.price
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun updateData(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
