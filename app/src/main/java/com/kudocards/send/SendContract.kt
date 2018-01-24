package com.kudocards.send

import com.kudocards.BasePresenter
import com.kudocards.BaseView
import com.kudocards.data.models.Description

/**
 * Created by marcus on 26/12/17.
 */
interface SendContract {

    interface View : BaseView<Presenter> {

        fun showLoading()

        fun hideLoading()

        fun showSentDialog()

        fun showErrorDialog()

        fun loadUsersSpinner(teamUsersList: MutableList<Description>)

        fun loadCardTypesSpinner(cardTypes: MutableList<Description>)

    }

    interface Presenter : BasePresenter {

        fun getTeamUsers()

        fun getCardsType()

        fun sendKudoCard(cardMessage: String)

        fun destinationSelected(destination: Description)

        fun typeSelected(type: Description)

    }
}