package dev.tapeline.ianus.presentation.controllers

import gg.jte.TemplateEngine
import gg.jte.springframework.boot.autoconfigure.JteView
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.DispatcherServlet
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.view.ViewResolverComposite

@Controller
class FrontendController(
    private val templateEngine: TemplateEngine,
    private val viewResolvers: List<ViewResolver>
) {

    @GetMapping("/entrypoint/login")
    fun loginPage() = "pages/login"

    @GetMapping("/entrypoint/register")
    fun registerPage() = "pages/register"

    @GetMapping("/entrypoint/login/challenge")
    fun loginChallengePage() = "pages/loginChallenge"

    @GetMapping("/my")
    fun myAccount() = "pages/my/dashboard"

    @GetMapping("/my/data")
    fun myAccountData() = "pages/my/data"

    @GetMapping("/my/security")
    fun myAccountSecurity() = "pages/my/security"

    @GetMapping("/my/apps")
    fun myAccountApps() = "pages/my/apps"

}
