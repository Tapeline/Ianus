@template.base.dashboard-page(
title = "My account on Ianus",
body = @`
    <h1>Welcome, <span id="greeting-username"></span></h1>
    <hr/>
    <a href="/my/reset-password">Reset password</a>
    <hr/>
    <input type="email" name="email" id="change-email-email"/>
    <button id="change-email-button">Change email</button>
    <pre id="email-report"></pre>
    <input type="email" name="email" id="change-email-code"/>
    <button id="change-email-confirm-button">Confirm code</button>

`,
extendScripts = @`
    <script>
        $("body").on("logged-in", () => {
            $("#greeting-username").text(user.username)
        })
        $("#change-email-button").click(() => {
            callPostApi("/api/v1/my/email/change", {
                email: $("#change-email-email").val()
            }).then(response => response.json().then(data => {
                $("#email-report").text("Verification sent, check your inbox\n\n" + JSON.stringify(data))
            }))
        })
        $("#change-email-confirm-button").click(() => {
            callPutApi("/api/v1/my/email/confirm", {
                email: $("#change-email-email").val(),
                code: $("#change-email-code").val()
            }).then(response => response.json().then(data => {
                $("#email-report").text(JSON.stringify(data))
            }))
        })
    </script>
`
)
