package dev.tapeline.ianus.presentation.controllers

import gg.jte.TemplateEngine
import gg.jte.output.StringOutput
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Controller
class FrontendController {

    @GetMapping("/entrypoint/login")
    fun loginPage(): String = "pages/login"

    @GetMapping("/entrypoint/register")
    fun registerPage() = "pages/register"

    @GetMapping("/my")
    fun myAccount() = "pages/my/dashboard"

    @GetMapping("/my/sessions")
    fun myAccountSessions() = "pages/my/sessions"

}
