# mHelper

Website automation service zakupki.gov.ru Our project - https://github.com/users/BJCreslin/projects/2

Для компиляции, с выключенными тестами, mvn clean package -DskipTests=true -Pci

# 1. БД

# 1.1 профиль ci

Для CI Postgresql, все данные о бд в системе CI/CD. База формируется через liquibase. Файлы для изменения БД в
src/main/resources/db/changelog.

# 1.2 профиль dev

Для профиля dev -h2. До бд можно добраться через http://localhost:8080/h2-console . Данные (Записаны в
src/main/resources/application-dev.properties) пользователь: sa , пароль: password .     
Пользователи системы ( создаются в сервисе src/main/java/ru/mhelper/init ). Имя/пароль : Admin:password User0:password1
User1:password1 User2:password2 .
