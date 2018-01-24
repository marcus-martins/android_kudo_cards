package com.kudocards.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.kudocards.R
import com.kudocards.data.models.Card

/**
 * Created by vitorp on 27/12/17.
 */

class KudoListAdapter(val cards : List<Card>, val listener: (Card) -> Unit) : RecyclerView.Adapter<KudoListViewHolder>(){
    override fun getItemCount(): Int {
        return cards.size
    }

    override fun onBindViewHolder(holder: KudoListViewHolder, position: Int) {
        holder.bind(cards[position], listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KudoListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_kudo_cards, parent, false)
        return KudoListViewHolder(view)
    }

}