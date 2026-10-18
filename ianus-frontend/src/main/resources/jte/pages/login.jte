@template.base.entry-card-page(
    title = "Login into Ianus",
    cardTitle = @`<h2 class="mdl-card__title-text">Ianus</h2>`,
    cardContent = @`
    <p>Enter credentials or <a href="/entrypoint/register">register</a> to continue</p>
    <form id="login-form">
        <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label" style="width:100%">
            <input class="mdl-textfield__input" type="text" id="username" name="username" required>
            <label class="mdl-textfield__label" for="username">Username</label>
        </div>
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
            var username = $("#username").val()
            var password = $("#password").val()
            var loginBtn = $("#login-btn")
            makeBtnLoading(loginBtn)
            callPostApi(
                "/api/v1/login",
                {username: username, password: password}
            ).then(
                ifOkOrDisplayError((status, data) => {
                    if (status === 200) {
                        window.location.href = "/entrypoint/login/challenge";
                    } else if (status === 201) {
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

