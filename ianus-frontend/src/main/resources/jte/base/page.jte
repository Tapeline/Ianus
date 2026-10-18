@import gg.jte.Content

@param String title
@param Content body
@param Content extendHead = null
@param Content extendScripts = null

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/material-design-lite/1.3.0/material.blue_grey-blue.min.css"/>
    ${extendHead}
    <style>
        .d-none {
            display: none;
        }
    </style>
</head>
<body>
${body}
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.12.4/jquery.min.js"
        integrity="sha512-jGsMH83oKe9asCpkOVkBnUrDDTp8wl+adkB2D+//JtlxO4SrLoJdhbOysIFQJloQFD+C4Fl1rMsQZF76JjV0eQ=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/material-design-lite/1.3.0/material.min.js"></script>
<script type="text/javascript">
    function callPostApi(url, body) {
        return fetch(url, {
            method: "POST",
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body),
        })
    }
    function callPutApi(url, body) {
        return fetch(url, {
            method: "PUT",
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body),
        })
    }
    function callDeleteApi(url) {
        return fetch(url, {
            method: "DELETE",
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' }
        })
    }
    function callGetApi(url) {
        return fetch(url, {
            method: "GET",
            credentials: 'include'
        })
    }
    function ifOkOrDisplayError(f, errorSelector) {
        return (response) => response.json().then(data => {
            if (!response.ok) {
                var errorContainer = $(errorSelector);
                errorContainer.text(data.message || 'Failed');
                errorContainer.show();
            } else {
                f(response.status, data)
            }
        })
    }
    function displayExcAt(errorSelector) {
        return e => {
            console.log(e)
            var errorContainer = $(errorSelector);
            errorContainer.text(e.response.data.message);
            errorContainer.show()
        }
    }
    function makeBtnLoading(comp) {
        comp.prop("disabled",true)
        comp.prepend('<div style="width:200px" class="mdl-js-progress mdl-progress--indeterminate"></div>');
    }
    function unmakeBtnLoading(comp) {
        comp.prop("disabled",false)
        comp.children().remove(".mdl-js-progress")
    }

</script>
${extendScripts}
</body>
</html>
