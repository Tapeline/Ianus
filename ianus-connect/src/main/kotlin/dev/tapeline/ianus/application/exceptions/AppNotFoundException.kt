package dev.tapeline.ianus.application.exceptions

import dev.tapeline.ianus.common.IanusException

class AppNotFoundException :
    IanusException("APP_NOT_FOUND", "Application with this app_id does not exist")
