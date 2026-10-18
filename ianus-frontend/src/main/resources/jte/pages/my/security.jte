@template.base.dashboard-page(
title = "Security",
body = @`
    <div class="mdl-grid demo-content">
        <div class="mdl-card mdl-shadow--2dp mdl-cell mdl-cell--4-col mdl-cell--4-col-tablet mdl-cell--12-col-desktop">
            <div class="mdl-card__title mdl-card--expand mdl-color--teal-300">
                <h2 class="mdl-card__title-text">Sessions</h2>
            </div>
            <div class="mdl-card__supporting-text mdl-color-text--grey-600">
                <ul id="session-list">

                </ul>
            </div>
        </div>
        <div class="mdl-card mdl-shadow--2dp mdl-cell mdl-cell--4-col mdl-cell--4-col-tablet mdl-cell--12-col-desktop">
            <div class="mdl-card__title mdl-card--expand mdl-color--teal-300">
                <h2 class="mdl-card__title-text">Two-factor authentication</h2>
            </div>
            <div class="mdl-card__supporting-text mdl-color-text--grey-600">
                <span id="tfa-status"></span>
                <hr/>
                <button onclick="toggleTfa(true)" class="tfabtn mdl-button mdl-button--primary mdl-js-button mdl-js-ripple-effect">Enable</button>
                <button onclick="toggleTfa(false)" class="tfabtn mdl-button mdl-button--primary mdl-js-button mdl-js-ripple-effect">Disable</button>
                <hr/>
                <span id="tfa-toggle-error"></span>
            </div>
        </div>
    </div>
`,
extendScripts = @`
    <script>
        $("body").on("logged-in", () => {
            $("#tfa-status").text(user["2fa_enabled"]? "Enabled" : "Disabled")
            callGetApi("/api/v1/my/sessions").then(response => response.json().then(data => {
                $("#session-list").html("")
                for (var session of data) {
                    $("#session-list").append("<li>" + session.id + " from " + session.origin + "</li>" +
                    "<button onclick='revokeSession(\"" + session.id + "\")'>Revoke</button>")
                }
            }))
        })

        function toggleTfa(enabled) {
            var btns = $(".tfabtn")
            makeBtnLoading(btns)
            callPutApi(
                "/api/v1/2fa/toggle",
                {"2fa_enabled": enabled}
            ).then(
                ifOkOrDisplayError((status, data) => {
                    window.location.reload()
                }, "#tfa-toggle-error")
            ).catch(
                displayExcAt("#tfa-toggle-error")
            ).finally(() => unmakeBtnLoading(btns))
        }

        function revokeSession(sessionId) {
            callDeleteApi("/api/v1/my/sessions/" + sessionId)
        }
    </script>
`
)
