@template.base.dashboard-page(
title = "My account on Ianus",
body = @`
    <h1>Welcome, <span id="greeting-username"></span></h1>
    <hr/>
    <a href="/my/reset-password">Reset password</a>
    <a href="/my/change-email">Reset password</a>
`,
extendScripts = @`
    <script>
        $("body").on("logged-in", () => {
            $("#greeting-username").text(user.username)
        })
    </script>
`
)
