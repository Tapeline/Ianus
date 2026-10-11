package dev.tapeline.ianus.application.typeclasses

import dev.tapeline.ianus.application.interfaces.Clock
import dev.tapeline.ianus.application.interfaces.algorithms.TwoFactorVerificationEngine
import dev.tapeline.ianus.application.interfaces.EmailSender
import dev.tapeline.ianus.configuration.TwoFactorConfiguration
import dev.tapeline.ianus.domain.EmailMessage
import dev.tapeline.ianus.domain.session.SessionOrigin
import dev.tapeline.ianus.domain.twofactor.TwoFactorKeyClaims
import dev.tapeline.ianus.domain.user.User

interface WithSecondFactorBeginVerification {

    val twoFactorVerificationEngine: TwoFactorVerificationEngine
    val emailSender: EmailSender
    val clock: Clock
    val twoFactorConfig: TwoFactorConfiguration

}

fun WithSecondFactorBeginVerification.issueAndSendCodeViaEmail(
    user: User, origin: SessionOrigin, emailComposer: (String, String) -> EmailMessage
) =
    twoFactorVerificationEngine.createTwoFactorPair(
        TwoFactorKeyClaims(user.id, origin, clock.now(), twoFactorConfig.verificationTtl)
    )
        .also { (code, _) -> emailSender.sendEmail(emailComposer(user.email!!.value, code.value)) }
        .let { (_, key) -> key }
