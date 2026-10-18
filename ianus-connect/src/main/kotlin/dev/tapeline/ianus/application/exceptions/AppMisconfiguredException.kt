package dev.tapeline.ianus.application.exceptions

import dev.tapeline.ianus.common.IanusException

class AppMisconfiguredException :
    IanusException("APP_MISCONFIGURED", "Application is misconfigured")
