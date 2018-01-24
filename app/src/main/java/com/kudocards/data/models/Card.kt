package com.kudocards.data.models

import com.google.firebase.database.Exclude

/**
 * Created by marcus on 26/12/17.
 */
data class Card(
        var datetime: Map<String, String> = mutableMapOf(),
        @Exclude var creationDate: String = "",
        var destinationDisplayName: String = "",
        var destinationId: String = "",
        var message: String = "",
        var senderDisplayName: String = "",
        var senderId: String = "",
        var team: String = "",
        var title: String = "",
        var type: String = ""
) {
    constructor(
            datetime: Map<String, String>,
            destinationDisplayName: String,
            destinationId: String,
            message: String,
            senderDisplayName: String,
            senderId: String,
            team: String,
            title: String,
            type: String
    ) : this(datetime, "", destinationDisplayName, destinationId, message, senderDisplayName,
            senderId, team, title, type)

    constructor(
            creationDate: String,
            destinationDisplayName: String,
            destinationId: String,
            message: String,
            senderDisplayName: String,
            senderId: String,
            team: String,
            title: String,
            type: String
    ) : this(mutableMapOf(), creationDate, destinationDisplayName, destinationId, message, senderDisplayName,
            senderId, team, title, type)
}