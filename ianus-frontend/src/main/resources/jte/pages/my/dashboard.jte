@template.base.dashboard-page(
title = "My account on Ianus",
body = @`
    <div style="width: 100%; height: 100%; display: flex; justify-content: center; align-items: center;">
    <h1>Welcome, <span id="greeting-username"></span></h1>
    </div>
`,
extendScripts = @`
    <script>
        $("body").on("logged-in", () => {
            $("#greeting-username").text(user.username)
        })
    </script>
`
)
