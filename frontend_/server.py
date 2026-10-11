import http.server
import socketserver
import os

PORT = 8000
# Файл, который будет отдаваться для всех неуказанных путей
SINGLE_FILE = "index.html"

class SPAHTTPRequestHandler(http.server.SimpleHTTPRequestHandler):
    def do_GET(self):
        # 1. Задаем исключения: если путь начинается с этих префиксов,
        # сервер будет искать реальные файлы или выдавать ошибку (например, для API)
        if self.path.startswith('/api') or self.path.startswith('/static/'):
            # Роутим стандартным образом (ищет файлы в текущей директории)
            super().do_GET()
            return

        # 2. Проверяем, существует ли запрашиваемый файл физически
        # (полезно, если вы хотите отдавать favicon.ico или существующие картинки корневой папки)
        local_path = self.translate_path(self.path)
        if os.path.exists(local_path) and not os.path.isdir(local_path):
            super().do_GET()
            return

        # 3. Во всех остальных случаях отдаем единый файл (маршрутизация на клиенте)
        try:
            with open(SINGLE_FILE, 'rb') as f:
                self.send_response(200)
                self.send_header("Content-Type", "text/html")
                self.end_headers()
                self.wfile.write(f.read())
        except FileNotFoundError:
            self.send_error(404, f"Базовый файл {SINGLE_FILE} не найден")

if __name__ == "__main__":
    # Разрешаем повторное использование порта, чтобы избежать ошибки "Address already in use"
    socketserver.TCPServer.allow_reuse_address = True

    with socketserver.TCPServer(("", PORT), SPAHTTPRequestHandler) as httpd:
        print(f"Сервер запущен на http://localhost:{PORT}")
        try:
            httpd.serve_forever()
        except KeyboardInterrupt:
            print("\nСервер остановлен.")
