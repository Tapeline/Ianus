package dev.tapeline.ianus.application.interfaces.algorithms

import dev.tapeline.ianus.domain.intent.Intent
import dev.tapeline.ianus.domain.intent.IntentToken

interface IntentEngine {

    fun createIntentToken(intent: Intent): IntentToken
    fun extractIntent(intentToken: IntentToken): Intent

}
