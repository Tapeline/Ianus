@template.base.entry-card-page(
title = "Account consent",
cardTitle = @`<h2 class="mdl-card__title-text">Ianus</h2>`,
cardContent = @`
    <p>App <span id="app-name"></span> wants you to authorise access to following scopes of your account:</p>
    <ul id="scopes"></ul>
    <p id="error-container"></p>
`,
cardActions = @`
    <button
            class="mdl-button mdl-button--primary mdl-js-button mdl-js-ripple-effect"
            id="reject-btn">Reject</button>
    <button
            class="mdl-button mdl-button--primary mdl-js-button mdl-js-ripple-effect"
            id="accept-btn">Accept</button>`,
extendScripts = @`
    <script>
        $(document).ready(() => {
            var params = new URLSearchParams(window.location.search);
            $("#app-name").text(params.get("appName"));
            $("#scopes").html("");
            for (var scope of params.get("scopes").split(" ")) {
                $("#scopes").append("<li>"+scope+"</li>");
            }
        })
        $("#accept-btn").click((e) => {
            e.preventDefault()
            makeBtnLoading($("#accept-btn"))
            callPostApi(
                "/api/v1/connect/authorize",
                {intent: params.get("intent")}
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
            ).finally(() => unmakeBtnLoading($("#accept-btn")))
        })
    </script>
`
)

