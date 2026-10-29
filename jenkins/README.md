# Настройка Jenkins для проекта

Экспорт настроенных jobs находится в main-job.xml и ci-demo-job.xml. Оба используют Pipeline script from SCM и публичный личный GitHub-репозиторий. Файлы не содержат паролей. main-job.xml смотрит на main; ci-demo-job.xml — на личную ветку эксперимента.

На текущем компьютере Jenkins доступен на http://127.0.0.1:8096. Используется существующий личный учебный Jenkins-контейнер, а не production-система. Linux node имеет label docker. Контроллер Jenkins работает на Java 21, для сборки настроен отдельный JDK tool midterm-jdk17 с Java 17. Gradle запускается из Wrapper, включённого в репозиторий.

Для повторной настройки в другом Jenkins:

1. Установить Pipeline, Git и Timestamper plugins.
2. Настроить Linux node с label docker, Git, JDK 17, Docker CLI и Docker Buildx, с доступом к Docker daemon. Проверить docker buildx version и docker info от имени пользователя агента.
3. В Manage Jenkins → Tools добавить JDK с именем midterm-jdk17 и реальным Linux-путём к JDK 17 на агенте. Автоматическая загрузка не обязательна.
4. Создать Pipeline job Tajibayev_Assylbek_Student_API с Pipeline script from SCM, Git URL из README, Branch */main и Script Path Jenkinsfile. Для CI_Demo указать */feature/tajibayev-assylbek-ci-demo.
5. Запустить job один раз. Jenkinsfile настроит pollSCM каждые две минуты. Далее commit/push автоматически инициирует новый запуск при обнаружении изменений.
6. Для private repository добавить ограниченный токен или SSH-ключ в Jenkins Credentials и выбрать credentialsId в SCM. Пароль не добавлять в Jenkinsfile или URL.

В текущей среде недостающие CLI и Buildx получены из официального образа docker:29-cli. JDK 17 получен из официального eclipse-temurin:17-jdk-jammy. Эти инструменты установлены внутри учебного Jenkins-контейнера. При его пересоздании из исходного старого образа изменения инструментов потребуется повторить или включить в собственный образ агента.

Встроенный node учебного Jenkins выполняет роль Docker-capable агента. Для командной или production-среды следует выделить отдельный изолированный агент. Настройки аутентификации существующего Jenkins не изменялись.
