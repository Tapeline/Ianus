@import gg.jte.Content

@param String title
@param Content cardTitle = null
@param Content cardContent = null
@param Content cardActions = null
@param Content extendHead = null
@param Content extendScripts = null

@template.base.page(
    title = title,
    body = @`
    <div class="mdl-card mdl-shadow--6dp entry-card">
    <div class="mdl-card__title">${cardTitle}</div>
    <div class="mdl-card__supporting-text">${cardContent}</div>
    <div class="mdl-card__actions mdl-card--border">${cardActions}</div>
    </div>
    `,
    extendHead = @`
    <style>
        body {
          display: flex;
          align-items: center;
          justify-content: center;
          min-height: 100vh;
          background: #fcfcfc;
          margin: 0;
          font-family: 'Roboto', sans-serif;
        }
        .entry-card {
          max-width: 400px;
          width: 90%;
        }
        .entry-card .mdl-card__title {
          background: rgb(96,125,139);
          color: white;
        }
        .error-message {
          color: #d32f2f;
          margin-top: 8px;
          font-size: 14px;
        }
    </style>
    ${extendHead}
    `,
    extendScripts = extendScripts
)
