package com.kudocards.data.remote

import com.kudocards.util.Constants.Companion.CARDS
import com.kudocards.util.Constants.Companion.CARDS_RECEIVED
import com.kudocards.util.Constants.Companion.CARDS_SENT
import com.kudocards.util.Constants.Companion.CARD_TYPES
import com.kudocards.util.Constants.Companion.TEAMS
import com.kudocards.util.Constants.Companion.TEAM_CARDS
import com.kudocards.util.Constants.Companion.USERS
import com.google.firebase.database.DatabaseReference

/**
 * Created by marcus on 26/12/17.
 */
class FirebaseConnectionImpl(val firebaseData: DatabaseReference?) : FirebaseConnection {

    override fun getUserInformation(): DatabaseReference? {
        return firebaseData?.child(USERS)
    }

    override fun saveUserInformation(userKey: String): DatabaseReference? {
        return firebaseData?.child(USERS)?.child(userKey)
    }

    override fun getTeamUsers(teamName: String) : DatabaseReference? {
        return firebaseData?.child(TEAMS)?.child(teamName)
    }

    override fun getCardsTypes() : DatabaseReference? {
        return firebaseData?.child(CARD_TYPES)
    }

    override fun saveKudoCard(): DatabaseReference? {
        val key = firebaseData?.child(CARDS)?.push()?.key
        return firebaseData?.child(CARDS)?.child(key)
    }

    override fun getTeamCards(teamName: String): DatabaseReference? {
        return firebaseData?.child(TEAM_CARDS)?.child(teamName)
    }

    override fun getUserReceivedCards(userKey: String): DatabaseReference? {
        return firebaseData?.child(CARDS_RECEIVED)?.child(userKey)
    }

    override fun getUserSentCards(userKey: String): DatabaseReference? {
        return firebaseData?.child(CARDS_SENT)?.child(userKey)
    }
}