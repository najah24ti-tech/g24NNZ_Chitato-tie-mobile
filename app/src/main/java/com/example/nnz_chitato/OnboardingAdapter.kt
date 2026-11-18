package com.example.nnz_chitato

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OnboardingAdapter(
    private val items: List<OnboardingItem>
) : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    class OnboardingViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val icon: TextView =
            itemView.findViewById(R.id.txtOnboardingIcon)

        val title: TextView =
            itemView.findViewById(R.id.txtOnboardingTitle)

        val description: TextView =
            itemView.findViewById(R.id.txtOnboardingDescription)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding, parent, false)

        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: OnboardingViewHolder,
        position: Int
    ) {
        val item = items[position]

        holder.icon.text = item.icon
        holder.title.text = item.title
        holder.description.text = item.description
    }

    override fun getItemCount(): Int = items.size
}
