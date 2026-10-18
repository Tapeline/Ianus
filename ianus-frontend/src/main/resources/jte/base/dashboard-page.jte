@import gg.jte.Content

@param String title
@param Content body
@param Content extendHead = null
@param Content extendScripts = null

@template.base.page(
title = title,
body = @`
    <div class="demo-layout mdl-layout mdl-js-layout mdl-layout--fixed-drawer mdl-layout--fixed-header">
        <header class="demo-header mdl-layout__header mdl-color--grey-100 mdl-color-text--grey-600">
            <div class="mdl-layout__header-row">
                <span class="mdl-layout-title">${title}</span>
                <div class="mdl-layout-spacer"></div>
            </div>
        </header>
        <div class="demo-drawer mdl-layout__drawer mdl-color--blue-grey-900 mdl-color-text--blue-grey-50">
            <header class="demo-drawer-header">
                <img src="images/user.jpg" class="demo-avatar">
                <div class="demo-avatar-dropdown">
                    <span id="username-container"></span>
                    <div class="mdl-layout-spacer"></div>
                    <button id="accbtn" class="mdl-button mdl-js-button mdl-js-ripple-effect mdl-button--icon">
                        <i class="material-icons" role="presentation">arrow_drop_down</i>
                        <span class="visuallyhidden">Accounts</span>
                    </button>
                    <ul class="mdl-menu mdl-menu--bottom-right mdl-js-menu mdl-js-ripple-effect" for="accbtn">
                        <li class="mdl-menu__item">hello@example.com</li>
                        <li class="mdl-menu__item">info@example.com</li>
                        <li class="mdl-menu__item"><i class="material-icons">add</i>Add another account...</li>
                    </ul>
                </div>
            </header>
            <nav class="demo-navigation mdl-navigation mdl-color--blue-grey-800">
                <a class="mdl-navigation__link" href="/my">
                    <i class="mdl-color-text--blue-grey-400 material-icons">home</i>
                    Dashboard
                </a>
                <a class="mdl-navigation__link" href="/my/data">
                    <i class="mdl-color-text--blue-grey-400 material-icons">person</i>
                    Personal data
                </a>
                <a class="mdl-navigation__link" href="/my/security">
                    <i class="mdl-color-text--blue-grey-400 material-icons">lock</i>
                    Sessions
                </a>
                <a class="mdl-navigation__link" href="/my/apps">
                    <i class="mdl-color-text--blue-grey-400 material-icons">widgets</i>
                    Applications
                </a>
                <div class="mdl-layout-spacer"></div>
                <a class="mdl-navigation__link" href="/help">
                    <i class="mdl-color-text--blue-grey-400 material-icons">help_outline</i>
                    Help center
                </a>
            </nav>
        </div>
        <main class="mdl-layout__content mdl-color--grey-100">
            ${body}
        </main>
    </div>
`,
extendScripts = @`
    <script>
        var user = null;
        $().ready(() => {
            callGetApi("/api/v1/my").then(response => response.json().then(data => {
                if (response.status === 200) {
                    $("#username-container").text(data.username)
                    user = data
                    $("body").trigger("logged-in")
                } else if (response.status === 401) {
                    window.location.href = "/entrypoint/login"
                }
            }))
        })
    </script>
    ${extendScripts}
`,
extendHead = @`
    <style>
        /**
        * Copyright 2015 Google Inc. All Rights Reserved.
        *
        * Licensed under the Apache License, Version 2.0 (the "License");
        * you may not use this file except in compliance with the License.
        * You may obtain a copy of the License at
        *
        *      http://www.apache.org/licenses/LICENSE-2.0
        *
        * Unless required by applicable law or agreed to in writing, software
        * distributed under the License is distributed on an "AS IS" BASIS,
        * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        * See the License for the specific language governing permissions and
        * limitations under the License.
        */

        html, body {
            font-family: 'Roboto', 'Helvetica', sans-serif;
        }

        .demo-avatar {
            width: 48px;
            height: 48px;
            border-radius: 24px;
        }

        .demo-layout .mdl-layout__header .mdl-layout__drawer-button {
            color: rgba(0, 0, 0, 0.54);
        }

        .mdl-layout__drawer .avatar {
            margin-bottom: 16px;
        }

        .demo-drawer {
            border: none;
        }

        /* iOS Safari specific workaround */
        .demo-drawer .mdl-menu__container {
            z-index: -1;
        }

        .demo-drawer .demo-navigation {
            z-index: -2;
        }

        /* END iOS Safari specific workaround */
        .demo-drawer .mdl-menu .mdl-menu__item {
            display: -webkit-flex;
            display: -ms-flexbox;
            display: flex;
            -webkit-align-items: center;
            -ms-flex-align: center;
            align-items: center;
        }

        .demo-drawer-header {
            box-sizing: border-box;
            display: -webkit-flex;
            display: -ms-flexbox;
            display: flex;
            -webkit-flex-direction: column;
            -ms-flex-direction: column;
            flex-direction: column;
            -webkit-justify-content: flex-end;
            -ms-flex-pack: end;
            justify-content: flex-end;
            padding: 16px;
            height: 151px;
        }

        .demo-avatar-dropdown {
            display: -webkit-flex;
            display: -ms-flexbox;
            display: flex;
            position: relative;
            -webkit-flex-direction: row;
            -ms-flex-direction: row;
            flex-direction: row;
            -webkit-align-items: center;
            -ms-flex-align: center;
            align-items: center;
            width: 100%;
        }

        .demo-navigation {
            -webkit-flex-grow: 1;
            -ms-flex-positive: 1;
            flex-grow: 1;
        }

        .demo-layout .demo-navigation .mdl-navigation__link {
            display: -webkit-flex !important;
            display: -ms-flexbox !important;
            display: flex !important;
            -webkit-flex-direction: row;
            -ms-flex-direction: row;
            flex-direction: row;
            -webkit-align-items: center;
            -ms-flex-align: center;
            align-items: center;
            color: rgba(255, 255, 255, 0.56);
            font-weight: 500;
        }

        .demo-layout .demo-navigation .mdl-navigation__link:hover {
            background-color: #00BCD4;
            color: #37474F;
        }

        .demo-navigation .mdl-navigation__link .material-icons {
            font-size: 24px;
            color: rgba(255, 255, 255, 0.56);
            margin-right: 32px;
        }

        .demo-content {
            max-width: 1080px;
        }

        .demo-charts {
            -webkit-align-items: center;
            -ms-flex-align: center;
            align-items: center;
        }

        .demo-chart:nth-child(1) {
            color: #ACEC00;
        }

        .demo-chart:nth-child(2) {
            color: #00BBD6;
        }

        .demo-chart:nth-child(3) {
            color: #BA65C9;
        }

        .demo-chart:nth-child(4) {
            color: #EF3C79;
        }

        .demo-graphs {
            padding: 16px 32px;
            display: -webkit-flex;
            display: -ms-flexbox;
            display: flex;
            -webkit-flex-direction: column;
            -ms-flex-direction: column;
            flex-direction: column;
            -webkit-align-items: stretch;
            -ms-flex-align: stretch;
            align-items: stretch;
        }

        /* TODO: Find a proper solution to have the graphs
        * not float around outside their container in IE10/11.
        * Using a browserhacks.com solution for now.
        */
        _:-ms-input-placeholder, :root .demo-graphs {
            min-height: 664px;
        }

        _:-ms-input-placeholder, :root .demo-graph {
            max-height: 300px;
        }

        /* TODO end */
        .demo-graph:nth-child(1) {
            color: #00b9d8;
        }

        .demo-graph:nth-child(2) {
            color: #d9006e;
        }

        .demo-cards {
            -webkit-align-items: flex-start;
            -ms-flex-align: start;
            align-items: flex-start;
            -webkit-align-content: flex-start;
            -ms-flex-line-pack: start;
            align-content: flex-start;
        }

        .demo-cards .demo-separator {
            height: 32px;
        }

        .demo-cards .mdl-card__title.mdl-card__title {
            color: white;
            font-size: 24px;
            font-weight: 400;
        }

        .demo-cards ul {
            padding: 0;
        }

        .demo-cards h3 {
            font-size: 1em;
        }

        .demo-updates .mdl-card__title {
            min-height: 200px;
            background-image: url('images/dog.png');
            background-position: 90% 100%;
            background-repeat: no-repeat;
        }

        .demo-cards .mdl-card__actions a {
            color: #00BCD4;
            text-decoration: none;
        }

        .demo-options h3 {
            margin: 0;
        }

        .demo-options .mdl-checkbox__box-outline {
            border-color: rgba(255, 255, 255, 0.89);
        }

        .demo-options ul {
            margin: 0;
            list-style-type: none;
        }

        .demo-options li {
            margin: 4px 0;
        }

        .demo-options .material-icons {
            color: rgba(255, 255, 255, 0.89);
        }

        .demo-options .mdl-card__actions {
            height: 64px;
            display: -webkit-flex;
            display: -ms-flexbox;
            display: flex;
            box-sizing: border-box;
            -webkit-align-items: center;
            -ms-flex-align: center;
            align-items: center;
        }
    </style>
    ${extendHead}
`
)