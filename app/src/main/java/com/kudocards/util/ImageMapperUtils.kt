package com.kudocards.util

import com.kudocards.R
import java.util.*

/**
 * Created by vitorp on 28/12/17.
 */

object ImageMapperUtils {
    internal val MAP: MutableMap<String, Int> = HashMap()

    init {
        MAP.put("congratulations", R.drawable.congratulations)
        MAP.put("greatjob", R.drawable.greatjob)
        MAP.put("manythanks", R.drawable.manythanks)
        MAP.put("thankyou", R.drawable.thankyou)
        MAP.put("totallyawesome", R.drawable.totallyawesome)
        MAP.put("veryhappy", R.drawable.veryhappy)
        MAP.put("welldone", R.drawable.welldone)
    }

    fun getImageResource(key: String): Int? {
        return if (!MAP.containsKey(key)) {
            null
        } else MAP[key]
    }
}