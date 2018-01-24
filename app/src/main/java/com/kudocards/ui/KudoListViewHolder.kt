package com.kudocards.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import com.kudocards.R
import com.kudocards.data.models.Card
import com.kudocards.util.ImageMapperUtils
import kotlinx.android.synthetic.main.layout_kudo_cards.view.*

/**
 * Created by vitorp on 27/12/17.
 */

class KudoListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(card: Card, listener: (Card) -> Unit) = with(itemView) {
        with(card){
            textView.text = title
            ImageMapperUtils.getImageResource(type)?.let { imageView.setImageResource(it) }
            tv_from.text = itemView.context.getString(R.string.from, senderDisplayName)
            tv_to.text = itemView.context.getString(R.string.to, destinationDisplayName)
            setOnClickListener { listener(card) }
        }
    }
}