package dev.tapeline.ianus.application.exceptions

import dev.tapeline.ianus.common.IanusException

class AppUnauthenticatedException :
    IanusException("APP_UNAUTHENTICATED", "App not authenticated")
