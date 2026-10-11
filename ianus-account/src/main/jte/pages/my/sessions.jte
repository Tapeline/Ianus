@template.base.dashboard-page(
title = "Sessions",
body = @`
    <div class="demo-content">
        <ul id="session-list">

        </ul>
    </div>
`,
extendScripts = @`
    <script>
        $("body").on("logged-in", () => {
            callGetApi("/api/v1/my/sessions").then(response => response.json().then(data => {
                $("#session-list").html("")
                for (var session of data) {
                    $("#session-list").append("<li>" + session.id + " from " + session.origin + "</li>")
                }
            }))
        })
    </script>
`
)
