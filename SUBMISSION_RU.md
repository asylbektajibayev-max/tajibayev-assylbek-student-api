# DevOps midterm выполненная работа

Tajibayev Assylbek  
Группа IT2 2310  
Student ID 38288

## Где открыть

- GitHub: https://github.com/asylbektajibayev-max/tajibayev-assylbek-student-api
- API и автор: http://127.0.0.1:18080/
- Студенты: http://127.0.0.1:18080/api/students
- Проверка здоровья: http://127.0.0.1:18080/actuator/health
- Jenkins main: http://127.0.0.1:8096/job/Tajibayev_Assylbek_Student_API/
- Jenkins тестовая ветка: http://127.0.0.1:8096/job/Tajibayev_Assylbek_Student_API_CI_Demo/

## Соответствие заданию

| Пункт | Выполнение | Доказательства |
|---|---|---|
| 1.1 | Каталоги, личные файлы, проверка структуры | scripts/linux_evidence.sh и evidence/linux-terminal.txt |
| 1.2 | Имя, дата, пользователь, ОС, ядро, CPU, память, диски | scripts/Tajibayev_Assylbek_system_report.sh и logs/Tajibayev_Assylbek_system_report.log |
| 1.3 | chmod 744, README backup, cmp и ls -l | evidence/linux-terminal.txt и backups/README_Tajibayev_Assylbek.md |
| 1.4 | Процессы, Java при наличии, tail, grep, перенаправление | evidence/linux-terminal.txt и logs/Tajibayev_Assylbek_log_analysis.txt |
| 2.1 | Git init, локальный автор, gitignore, первый личный commit | Git history и .git/config локального архива |
| 2.2 | Личная CRUD-ветка, поиск по имени без регистра, тест, два commit, merge | feature/tajibayev-assylbek-crud; commits 78d6dd6 и 1f3d169 |
| 2.3 | Две правки одной строки README, настоящий конфликт, ручное объединение | evidence/git-conflict.txt и resolution commit b215b87 |
| 2.4 | GitHub remote, main, личные ветки и аннотированный тег | evidence/git-remote.txt, git-history.txt, git-branches.txt, git-tags.txt |
| 3.1 | Multi-stage JDK/JRE, Gradle build, non-root runtime, порт, личные теги | Dockerfile; evidence/docker-build.txt и docker-images.txt |
| 3.2 | API и PostgreSQL, environment, volume, network, healthcheck | docker-compose.yml и evidence/container-management.txt |
| 3.3 | ps, logs, inspect, exec, stop/start, down/up, новая версия 1.1 | evidence/container-management.txt и docker-rebuild-v1.1.txt |
| 3.4 | curl POST, GET all/one, PUT, DELETE, 404; дополнительно 400 и 409 | evidence/curl-api-demonstration.json и api-responses.json |
| 4.1 | Личный SCM job, JDK tool midterm-jdk17, Wrapper, node docker | Jenkins job и console output |
| 4.2 | Checkout, Build, Test, Docker Build, Verify; имя и образ автора | Jenkinsfile и Jenkins console output |
| 4.3 | Контролируемый провал теста в личной ветке и исправление | CI_Demo build history и evidence/jenkins-*.txt |
| 4.4 | pollSCM каждые две минуты, автоматический запуск после push | console output с Started by an SCM change |

## Проверенное поведение

Основной Jenkins job №6 завершён SUCCESS. Полная последовательность CI_Demo: №4 SUCCESS → №5 FAILURE → №6 SUCCESS. В №5 падает только намеренный controlledFailure; Docker Build и Verify пропускаются. После исправления в №6 проходят все пять стадий. Основные сборки №5 и №6 запущены автоматически: в console output указано Started by an SCM change.

Коммит ошибки после успешной базовой сборки: cb68244. Коммит исправления: 2e603a6. Прямые ссылки на демонстрацию:

- http://127.0.0.1:8096/job/Tajibayev_Assylbek_Student_API_CI_Demo/4/
- http://127.0.0.1:8096/job/Tajibayev_Assylbek_Student_API_CI_Demo/5/
- http://127.0.0.1:8096/job/Tajibayev_Assylbek_Student_API_CI_Demo/6/

Автоматические тесты проверяют полный CRUD-цикл, некорректный ввод, дубликат email, обновление/удаление отсутствующего ID и фильтрацию по имени. HTTP-проверка выполняется против настоящего PostgreSQL в Docker; модульные интеграционные тесты используют отдельную H2 и не трогают рабочие записи.

Для persistence-проверки создан студент Tajibayev Assylbek с ID 3. После `docker compose down` и `docker compose up -d` получена та же запись. После пересборки и запуска версии 1.1 запись также осталась. Результаты: evidence/container-management.txt и evidence/version11-and-persistence.json.

Версия 1.0 и версия 1.1 имеют разные личные Docker-теги. Исходники версии приложения изменены в build.gradle и InfoController. Итоговый Compose использует 1.1.

Linux-команды выполнялись в настоящем Linux-контейнере Docker Desktop. Сведения об ОС, пользователе, CPU и памяти относятся к среде выполнения контейнера. Это не вымышленные выводы команд Windows.

## Объяснение сбоев Jenkins

Первый запуск прерван во время долгой загрузки Gradle, затем использован локальный кэш официального Gradle. Следующие диагностические запуски выявили отсутствие JUnit publisher и Buildx в существующем Jenkins. Pipeline сохраняет XML через archiveArtifacts; Docker CLI и Buildx получены из официального образа docker:29-cli. В Jenkinsfile включён BuildKit.

Намеренная ошибка вводилась только в личной ветке feature/tajibayev-assylbek-ci-demo. Она вызывает AssertionFailedError в controlledFailure; Gradle test возвращает ненулевой exit code, и Docker Build/Verify пропускаются. Исправление восстанавливает проверку и позволяет пройти всем стадиям. Основная ветка не содержит намеренно сломанного теста.

## Запуск после распаковки

```bash
python scripts/create_env.py
docker compose up -d --build
python scripts/api_verify.py
```

Архив не содержит `.env` с рабочим паролем. Скрипт create_env.py создаст новый локальный пароль. На текущем компьютере приложение уже запущено с существующим `.env`; его не нужно перегенерировать, потому что пароль существующей PostgreSQL БД должен совпадать.

Git-история включена в архив. Не создавать новый git init поверх неё. Для просмотра: `git log --graph --oneline --decorate --all`.

Ответы на вопросы для защиты находятся в DEFENSE_RU.md. Пояснения команд запуска, CI/CD, сети и секретов — в README.md.
