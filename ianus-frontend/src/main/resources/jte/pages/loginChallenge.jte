@template.base.entry-card-page(
    title = "Login into Ianus 2FA",
    cardTitle = @`<h2 class="mdl-card__title-text">Ianus</h2>`,
    cardContent = @`
    <p>Enter 2FA code sent to your email</p>
    <form id="login-form">
        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label" style="width:100%">
            <input class="mdl-textfield__input" type="password" id="password" name="password" required>
            <label class="mdl-textfield__label" for="password">Password</label>
        </div>
        <div id="error-container" class="error-message" style="display:none"></div>
    </form>
    `,
    cardActions = @`
    <button type="submit" form="login-form"
            class="mdl-button mdl-button--primary mdl-js-button mdl-js-ripple-effect"
            id="login-btn">Login</button>`,
    extendScripts = @`
    <script>
        $("#login-form").on("submit", (e) => {
            e.preventDefault()
            var password = $("#password").val()
            var loginBtn = $("#login-btn")
            makeBtnLoading(loginBtn)
            callPostApi(
                "/api/v1/2fa/confirm",
                {code: password}
            ).then(
                ifOkOrDisplayError((status, data) => {
                    if (status === 200 || status === 201) {
                        window.location.href = "/my"
                    }
                }, "#error-container")
            ).catch(
                displayExcAt("#error-container")
            ).finally(() => unmakeBtnLoading(loginBtn))
        })
    </script>
    `
)

