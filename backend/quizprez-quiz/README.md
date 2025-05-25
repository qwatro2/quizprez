1. [POST] `/api/v1/admin/create` с телом
    ```json
    { "html": "<quiz>...</quiz>" }
    ```
    Возвращается код сессии и байт-строка - представление куара \
    **TODO: узнать формат ссылки для подключения и зашивать в куар ее**
2. [POST] `/api/participant/join` с телом
    ```json
    { "sessionCode": "abcd1234", "name": "Иван" }
    ```
Дальше шаги которые надо будет разобрать с хотя бы темплейтом фронта
3. Подключиться по WebSocket (`/quiz-ws`, SockJS+STOMP), подписаться:
    + `/topic/quiz/{code}/question` — новые вопросы
    + `/topic/quiz/{code}/results` — финальная таблица
4. Админ шлёт по STOMP:
    + `/app/quiz/start` с payload "abcd1234" (код сессии)
    + `/app/quiz/next` с payload "abcd1234"
5. Участники шлют `/app/quiz/answer` с JSON `AnswerMessage`

Как только все вопросы пройдены, всем уйдёт сообщение с результатами.