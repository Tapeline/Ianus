package dev.tapeline.ianus.application.exceptions

import dev.tapeline.ianus.common.IanusException

class ExpiredAuthorisationCodeException :
    IanusException("AUTHORISATION_CODE_EXPIRED", "Authorisation code expired")
