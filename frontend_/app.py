from pathlib import Path

from litestar import Litestar, get
from litestar.response import Template
from litestar.contrib.jinja import JinjaTemplateEngine
from litestar.template.config import TemplateConfig


@get(path="/entrypoint/login")
def login_page() -> Template:
    return Template(template_name="pages/login.html")

@get(path="/entrypoint/register")
def register_page() -> Template:
    return Template(template_name="pages/register.html")

@get(path="/my")
def my_page() -> Template:
    return Template(template_name="pages/my.html")

app = Litestar(
    debug=True,
    route_handlers=[login_page, register_page, my_page],
    template_config=TemplateConfig(
        directory=Path("templates"),
        engine=JinjaTemplateEngine,
    ),
)
