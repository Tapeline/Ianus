@template.base.dashboard-page(
title = "Personal data",
body = @`
    <div class="demo-content">
        <p id="data-container"></p>
        <hr/>
        <input type="email" name="email" id="change-email-email"/>
        <button id="change-email-button">Change email</button>
        <pre id="email-report"></pre>
        <input type="email" name="email" id="change-email-code"/>
        <button id="change-email-confirm-button">Confirm code</button>
    </div>
`,
extendScripts = @`
    <script>
        $("body").on("logged-in", () => {
            $("#data-container").html("<b>Username</b>: " + user.username + "<br><b>Email</b>: " + user.email )
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
