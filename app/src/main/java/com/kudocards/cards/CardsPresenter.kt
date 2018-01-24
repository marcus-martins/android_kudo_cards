package com.kudocards.cards

import com.kudocards.data.models.Card
import com.kudocards.data.models.User
import com.kudocards.data.remote.FirebaseConnection
import com.kudocards.util.Constants
import com.kudocards.util.Constants.Companion.TEAM_NAME
import com.kudocards.util.UserValues
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by marcus on 28/12/17.
 */
class CardsPresenter (
        private val firebaseConnection: FirebaseConnection,
        private val firebaseUser: FirebaseUser?,
        private val deviceToken: String,
        private val cardsView: CardsContract.View): CardsContract.Presenter {

    private lateinit var teamCardsListener : ValueEventListener
    private lateinit var receivedCardsListener : ValueEventListener
    private lateinit var sentCardsListener : ValueEventListener

    init {
        cardsView.presenter = this
    }

    override fun start() {
        validadeUserInformation()
    }

    override fun validadeUserInformation() {
        UserValues.userId = firebaseUser!!.uid
        UserValues.userDisplayName = firebaseUser.displayName!!

        firebaseConnection.getUserInformation()?.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (!snapshot.hasChild(UserValues.userId) && UserValues.teamName.isEmpty()) {
                            cardsView.showChangeTeamDialog()
                        } else {
                            if (UserValues.teamName.isEmpty()) {
                                UserValues.teamName = snapshot.child(UserValues.userId).child(TEAM_NAME).value.toString()
                            }

                            val user = User(deviceToken, UserValues.userDisplayName, UserValues.teamName)
                            firebaseConnection.saveUserInformation(firebaseUser.uid)?.setValue(user)!!
                                    .addOnCompleteListener({ cardsView.reloadCards() })
                        }
                    }

                    override fun onCancelled(p0: DatabaseError?) {
                        cardsView.showErrorDialog()
                    }
                }
        )
    }

    override fun changeUserTeam(teamName: String) {
        UserValues.teamName = teamName
        validadeUserInformation()
    }

    override fun getTeamKudoCards() {

        teamCardsListener = object : ValueEventListener {
            val teamCards: MutableList<Card> = mutableListOf()

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                teamCards.clear()
                dataSnapshot.children.mapNotNull {
                    teamCards.add(
                            Card(
                                    getDateTime(it.child(Constants.DATE_TIME).value as Long),
                                    it.child(Constants.DESTINATION_DYSPLAY_NAME).value as String,
                                    it.child(Constants.DESTINATION_ID).value as String,
                                    it.child(Constants.MESSAGE).value as String,
                                    it.child(Constants.SENDER_DISPLAY_NAME).value as String,
                                    it.child(Constants.SENDER_ID).value as String,
                                    it.child(Constants.TEAM).value as String,
                                    it.child(Constants.TITLE).value as String,
                                    it.child(Constants.TYPE).value as String
                            )
                    )
                }

                teamCards.reverse()
                cardsView.loadTeamCards(teamCards)
                cardsView.hideLoading()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                cardsView.showErrorDialog()
            }
        }


        cardsView.showLoading()
        firebaseConnection.getTeamCards(UserValues.teamName)?.removeEventListener(teamCardsListener)
        firebaseConnection.getTeamCards(UserValues.teamName)?.addValueEventListener(teamCardsListener)
    }

    override fun getReceivedKudoCards() {
        receivedCardsListener = object : ValueEventListener {
            val receivedCards: MutableList<Card> = mutableListOf()

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                receivedCards.clear()
                dataSnapshot.children.mapNotNull {
                    receivedCards.add(
                            Card(
                                    getDateTime(it.child(Constants.DATE_TIME).value as Long),
                                    it.child(Constants.DESTINATION_DYSPLAY_NAME).value as String,
                                    it.child(Constants.DESTINATION_ID).value as String,
                                    it.child(Constants.MESSAGE).value as String,
                                    it.child(Constants.SENDER_DISPLAY_NAME).value as String,
                                    it.child(Constants.SENDER_ID).value as String,
                                    it.child(Constants.TEAM).value as String,
                                    it.child(Constants.TITLE).value as String,
                                    it.child(Constants.TYPE).value as String
                            )
                    )
                }

                receivedCards.reverse()
                cardsView.loadReceivedCards(receivedCards)
                cardsView.hideLoading()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                cardsView.showErrorDialog()
            }
        }

        cardsView.showLoading()
        firebaseConnection.getUserReceivedCards(UserValues.userId)?.removeEventListener(receivedCardsListener)
        firebaseConnection.getUserReceivedCards(UserValues.userId)?.addValueEventListener(receivedCardsListener)
    }

    override fun getSentKudoCards() {
        sentCardsListener = object : ValueEventListener {
            val sentCards: MutableList<Card> = mutableListOf()

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                sentCards.clear()
                dataSnapshot.children.mapNotNull {
                    sentCards.add(
                            Card(
                                    getDateTime(it.child(Constants.DATE_TIME).value as Long),
                                    it.child(Constants.DESTINATION_DYSPLAY_NAME).value as String,
                                    it.child(Constants.DESTINATION_ID).value as String,
                                    it.child(Constants.MESSAGE).value as String,
                                    it.child(Constants.SENDER_DISPLAY_NAME).value as String,
                                    it.child(Constants.SENDER_ID).value as String,
                                    it.child(Constants.TEAM).value as String,
                                    it.child(Constants.TITLE).value as String,
                                    it.child(Constants.TYPE).value as String
                            )
                    )
                }

                sentCards.reverse()
                cardsView.loadSentCards(sentCards)
                cardsView.hideLoading()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                cardsView.showErrorDialog()
            }
        }

        cardsView.showLoading()
        firebaseConnection.getUserSentCards(UserValues.userId)?.removeEventListener(sentCardsListener)
        firebaseConnection.getUserSentCards(UserValues.userId)?.addValueEventListener(sentCardsListener)
    }

    private fun getDateTime(datetime: Long): String {
        return try {
            val sdf = SimpleDateFormat(Constants.DATE_TEMPLATE, Locale.ENGLISH)
            val date = Date(datetime)
            sdf.format(date)
        } catch (e: Exception) {
            e.toString()
        }
    }
}