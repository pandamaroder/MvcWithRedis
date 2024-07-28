

# О проекте:

[![Java CI](https://github.com/pandamaroder/ContactRegistry/actions/workflows/github-actions-demo.yml/badge.svg)](https://github.com/pandamaroder/ContactRegistry/actions/workflows/github-actions-demo.yml)
[![codecov](https://codecov.io/gh/pandamaroder/ContactRegistry/graph/badge.svg?token=9KNR2SQ3QI)](https://codecov.io/gh/pandamaroder/ContactRegistry)

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=pandamaroder_ContactRegistry&metric=bugs)](https://sonarcloud.io/summary/new_code?id=pandamaroder_ContactRegistry)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=pandamaroder_ContactRegistry&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=pandamaroder_ContactRegistry)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=pandamaroder_ContactRegistry&metric=coverage)](https://sonarcloud.io/summary/new_code?id=pandamaroder_ContactRegistry)

MVC сервис для приложения «Новостной сервис»

**Технологии:** Java, Spring Boot, Postgress, Spring Data, Redis

# Первичная установка

1. Скачать проект с гитхаба https://github.com/<>
2. Обновить gradle зависимости
3. Добавить в конфигурацию идеи environment variables DB_PORT=;DB_HOST=localhost;DB_USER=postgres
4. Установить докер(десктоп версию под Win or Mac)
5. запустить локальный файл компоуз для поднятия бд либо через idea либо в терминале `docker-compose -f docker-compose-env-only.yml up`
6. запустить Application



#Тестирование эндпоинтов:



# Локальный запуск:

##  окружение в докере

1. Выполнить в корне 
```shell
docker-compose -f docker-compose-env-only.yml up
```

2. Установить переменные окружения (env vars): `DB_PORT=5400;DB_HOST=localhost;DB_USER=postgres;DB_PASS=123;DB_NAME=FMH_DB;SWAGGER_HOST=localhost:8080;DOCUMENTS_STATIC_PATH=/var;STATIC_HOST=test.vhospice.org;APP_MAIL_USERNAME=test@vhospice.org;APP_MAIL_PASSWORD=G3ttdHGrgjqtfjkjpHeF`
3. Запустить backend сервис
