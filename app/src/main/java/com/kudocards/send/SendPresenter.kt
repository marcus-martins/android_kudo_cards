package com.kudocards.send

import com.kudocards.data.models.Card
import com.kudocards.data.models.Description
import com.kudocards.data.remote.FirebaseConnection
import com.kudocards.util.Constants.Companion.DESCRIPTION
import com.kudocards.util.UserValues
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener

/**
 * Created by marcus on 26/12/17.
 */
class SendPresenter(
        private val firebaseConnection: FirebaseConnection,
        private val sendView: SendContract.View): SendContract.Presenter {

    private val cardToSend = Card()

    init {
        sendView.presenter = this
    }

    override fun start() {
        sendView.showLoading()
        getTeamUsers()
        getCardsType()
    }

    override fun getTeamUsers() {

        firebaseConnection.getTeamUsers(UserValues.teamName)?.addValueEventListener(
                object : ValueEventListener {
                    val teamUsersList: MutableList<Description> = mutableListOf()

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        teamUsersList.clear()
                        dataSnapshot.children.mapNotNull {
                            if (it.key != UserValues.userId) {
                                teamUsersList.add(Description(it.key, it.child(DESCRIPTION).value as String))
                            }
                        }

                        sendView.loadUsersSpinner(teamUsersList)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        sendView.showErrorDialog()
                    }
                }
        )

    }

    override fun getCardsType() {

        firebaseConnection.getCardsTypes()?.addValueEventListener(
                object : ValueEventListener {
                    val cardTypes: MutableList<Description> = mutableListOf()

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        cardTypes.clear()
                        dataSnapshot.children.mapNotNull {
                            cardTypes.add(Description(it.key, it.child(DESCRIPTION).value as String))
                        }

                        sendView.hideLoading()
                        sendView.loadCardTypesSpinner(cardTypes)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        sendView.showErrorDialog()
                    }
                }
        )

    }

    override fun destinationSelected(destination: Description) {
        cardToSend.destinationId = destination.descriptionId
        cardToSend.destinationDisplayName = destination.description
    }

    override fun typeSelected(type: Description) {
        cardToSend.type = type.descriptionId
        cardToSend.title = type.description
    }

    override fun sendKudoCard(cardMessage: String) {
        cardToSend.message = cardMessage
        cardToSend.senderId = UserValues.userId
        cardToSend.senderDisplayName = UserValues.userDisplayName
        cardToSend.team = UserValues.teamName
        cardToSend.datetime = ServerValue.TIMESTAMP

        firebaseConnection.saveKudoCard()
                ?.setValue(cardToSend)
                ?.addOnCompleteListener({ task ->
                    if (task.isSuccessful) {
                        sendView.showSentDialog()
                    } else {
                        sendView.showErrorDialog()
                    }
                })
    }
}