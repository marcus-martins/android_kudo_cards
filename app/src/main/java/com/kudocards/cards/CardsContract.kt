package com.kudocards.cards

import com.kudocards.BasePresenter
import com.kudocards.BaseView
import com.kudocards.data.models.Card

/**
 * Created by marcus on 28/12/17.
 */
interface CardsContract {
    interface View : BaseView<Presenter> {

        fun showLoading()

        fun hideLoading()

        fun showErrorDialog()

        fun loadTeamCards(teamCards: MutableList<Card>)

        fun loadReceivedCards(receivedCards: MutableList<Card>)

        fun loadSentCards(sentCards: MutableList<Card>)

        fun showChangeTeamDialog()

        fun logoutDialog()

        fun reloadCards()

    }

    interface Presenter : BasePresenter {

        fun validadeUserInformation()

        fun changeUserTeam(teamName: String)

        fun getTeamKudoCards()

        fun getReceivedKudoCards()

        fun getSentKudoCards()

    }
}