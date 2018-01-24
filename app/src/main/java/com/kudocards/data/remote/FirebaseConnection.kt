package com.kudocards.data.remote

import com.google.firebase.database.DatabaseReference

/**
 * Created by marcus on 26/12/17.
 */
interface FirebaseConnection {
    fun getUserInformation(): DatabaseReference?

    fun saveUserInformation(userKey: String): DatabaseReference?

    fun getTeamUsers(teamName: String): DatabaseReference?

    fun getCardsTypes(): DatabaseReference?

    fun saveKudoCard(): DatabaseReference?

    fun getTeamCards(teamName: String): DatabaseReference?

    fun getUserReceivedCards(userKey: String): DatabaseReference?

    fun getUserSentCards(userKey: String): DatabaseReference?
}