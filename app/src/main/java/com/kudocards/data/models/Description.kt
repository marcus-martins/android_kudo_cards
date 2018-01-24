package com.kudocards.data.models

import com.google.firebase.database.Exclude

/**
 * Created by marcus on 26/12/17.
 */
class Description(
        @Exclude val descriptionId: String = "",
        val description: String = ""
)

{
    override fun toString(): String {
        return description
    }
}