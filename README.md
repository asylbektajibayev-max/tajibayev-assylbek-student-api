# Student API DevOps midterm

Автор: Tajibayev Assylbek

Группа: IT2 2310. Student ID: 38288. GitHub: https://github.com/asylbektajibayev-max

Documentation status: main submission guide by Tajibayev Assylbek

Личная CRUD-доработка Tajibayev Assylbek: GET /api/students?name=TAJIBAYEV фильтрует студентов по фрагменту имени без учёта регистра. Покрыто тестом nameFilter.

Spring Boot CRUD API для студентов с PostgreSQL, Gradle Wrapper, Docker Compose и Jenkins CI. Java 17; Spring Boot 3.5.16; Gradle 8.14.3; PostgreSQL 17. Приложение показывает имя автора через `GET /`.

## Запуск

1. Скопировать `.env.example` в `.env`; задать личное имя, PROJECT_NAME, IMAGE_TAG и случайный DB_PASSWORD. Не публиковать `.env`.
2. Выполнить `docker compose up -d --build`.
3. Проверить `docker compose ps` и `curl http://localhost:18080/actuator/health`.
4. Выполнить `python scripts/api_verify.py` для проверки всех CRUD-операций, 404, 400 и 409.

API: `http://localhost:18080/api/students`. Все входные JSON содержат name, email, course. POST возвращает 201 и Location, GET/PUT — 200, DELETE — 204. Неизвестный ID возвращает 404; некорректные данные — 400; повторный email — 409.

```bash
curl -i -H 'Content-Type: application/json' -d '{"name":"Tajibayev Assylbek","email":"student@example.org","course":"DevOps"}' http://localhost:18080/api/students
curl -i http://localhost:18080/api/students
curl -i http://localhost:18080/api/students/1
curl -i -X PUT -H 'Content-Type: application/json' -d '{"name":"Tajibayev Assylbek","email":"student@example.org","course":"DevOps updated"}' http://localhost:18080/api/students/1
curl -i -X DELETE http://localhost:18080/api/students/1
curl -i http://localhost:18080/api/students/1
```

Подставлять ID, который вернул POST; последовательность выше предполагает ID 1 только для примера.

## Сборка и тесты

Linux: `./gradlew clean test bootJar`; Windows: `gradlew.bat clean test bootJar`.
Тесты используют отдельную H2 in-memory БД и не меняют PostgreSQL. HTTP-проверки проверяют реальную базу в Compose. JUnit XML: `build/test-results/test`; HTML: `build/reports/tests/test/index.html`.

## Linux

Запустить `bash scripts/linux_evidence.sh` в Linux. Скрипт создаёт каталоги через mkdir, пустой лог через touch, задаёт права 744, создаёт резервную копию README, сравнивает её с оригиналом через cmp, выводит процессы, последние пять строк и поиск фамилии. Отчёт включает время, пользователя, ОС, ядро, CPU, память и диски. Лог находится в logs; свидетельства — evidence/linux-terminal.txt.

`744` = владелец rwx, группа r--, остальные r--. Это требование задания; `755` дало бы группе и остальным ещё и исполнение.

`>` создаёт или перезаписывает файл; `>>` создаёт или дописывает в конец.

## Docker

```bash
docker compose ps
docker compose logs --no-color app
docker inspect "$(docker compose ps -q app)"
docker compose exec -T app java -version
docker compose stop app
docker compose start app
docker image inspect "$PROJECT_NAME:$IMAGE_TAG"
docker image ls
```

Для новой версии изменить version в build.gradle и значение version в InfoController, задать новый IMAGE_TAG в .env и выполнить `docker compose up -d --build app`.

Проверка сохранности: создать запись, сохранить её ID, выполнить `docker compose down`, затем `docker compose up -d`, снова получить запись по ID. Именованный volume сохраняется. `down -v` удаляет данные и не подходит для этой проверки.

DB_URL содержит имя сервиса `db`; localhost внутри app означает сам app-контейнер. База не публикует порт на хост. Приложение доступно только с локального компьютера через 18080. PostgreSQL healthcheck проверяет готовность сервера; приложение стартует после успешной проверки.

## Git и GitHub

Личное имя и email нужно задать локально в этом репозитории, не менять общие настройки чужих проектов.

```bash
git config user.name 'Tajibayev Assylbek'
git config user.email 'YOUR_VERIFIED_GITHUB_EMAIL'
git remote add origin https://github.com/YOUR_USERNAME/YOUR_PERSONAL_REPOSITORY.git
git push -u origin main
git push origin --all
git push origin --tags
git remote -v
git log --graph --oneline --decorate --all
git branch -a
git tag -n
```

Создать пустой личный GitHub repository без автоматически созданного README. Авторизация выполняется через Git Credential Manager или SSH, токен не вставляется в URL.

`.env`, пароли и ключи не коммитятся: Git сохраняет старые версии, поэтому удаление файла новым коммитом не удаляет секрет из истории. Утёкший секрет нужно отозвать и заменить. `.env.example` содержит только образец.

## Jenkins

Создать Multibranch Pipeline с личным именем `Surname_Name_Student_API`. Добавить GitHub Branch Source с URL своего репозитория; credentialsId хранить в Jenkins Credentials. Настроить обнаружение main и feature-веток. Script Path: Jenkinsfile.

Агент Linux с label `docker` должен иметь Git, JDK 17, доступ к Docker и сеть к Gradle/Maven repositories. Gradle устанавливается через закоммиченный Wrapper. Доступ к Docker daemon даёт широкие права: использовать личный доверенный агент для этого проекта.

Pipeline выполняет Checkout, Build, Test, Docker Build, Verify. Ошибка sh/тестов прерывает дальнейшие stages. JUnit результаты публикуются и при провале тестов. Имя автора печатается в консоль. Образ должен совпадать с личным именем и тегом из .env.

Для демонстрации ошибки использовать личную feature-ветку CI, указанную в Jenkinsfile. Первый запуск без параметра CONTROLLED_FAILURE должен пройти. Второй запуск с параметром должен упасть на Test. Третий запуск с выключенным параметром должен снова пройти. Сохранить Console Output и build history для всех трёх запусков. Для демонстрации исправления кодом можно добавить в feature-ветке ошибочное утверждение теста, закоммитить его, дождаться failed build, затем исправить отдельным коммитом.

pollSCM опрашивает Git каждые две минуты после первого запуска pipeline. После первого успешного запуска сделать новый commit и push, дождаться автоматического запуска. Альтернатива — GitHub webhook на доступный Jenkins endpoint с секретом. Не открывать Jenkins в интернет без аутентификации и TLS.

CI автоматически собирает и проверяет изменения. Continuous Delivery готовит проверенную версию к выпуску с ручным одобрением. Continuous Deployment автоматически выпускает каждую прошедшую проверки версию. Это задание не требует развёртывания на production.

## Источники

- https://docs.spring.io/spring-boot/3.5/system-requirements.html
- https://docs.docker.com/compose/how-tos/startup-order/
- https://www.jenkins.io/doc/book/pipeline/syntax/
- https://docs.gradle.org/8.14.3/userguide/gradle_wrapper.html
