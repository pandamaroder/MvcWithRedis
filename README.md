

# О проекте:

[![Java CI](https://github.com/pandamaroder/ContactRegistry/actions/workflows/github-actions-demo.yml/badge.svg)](https://github.com/pandamaroder/ContactRegistry/actions/workflows/github-actions-demo.yml)
[![codecov](https://codecov.io/gh/pandamaroder/ContactRegistry/graph/badge.svg?token=9KNR2SQ3QI)](https://codecov.io/gh/pandamaroder/ContactRegistry)

[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=pandamaroder_ContactRegistry&metric=bugs)](https://sonarcloud.io/summary/new_code?id=pandamaroder_ContactRegistry)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=pandamaroder_ContactRegistry&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=pandamaroder_ContactRegistry)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=pandamaroder_ContactRegistry&metric=coverage)](https://sonarcloud.io/summary/new_code?id=pandamaroder_ContactRegistry)

MVC сервис для приложения «Новостной сервис» c кешированием

**Технологии:** Java, Spring Boot, Postgress, Spring Data, Redis

# Первичная установка

1. Скачать проект с гитхаба https://github.com/<>
2. Обновить gradle зависимости
3. Добавить в конфигурацию идеи environment variables DB_PORT=;DB_HOST=localhost;DB_USER=postgres
4. Установить докер(десктоп версию под Win or Mac)
5. запустить локальный файл компоуз для поднятия бд либо через idea либо в терминале `docker-compose -f docker-compose-env-only.yml up`
6. запустить Application
7. проверить корректность примененных миграций: DDL + Data 
8. проверить состояние контейнеров : логи 



#Тестирование эндпоинтов:
Для проверки основного контроллера сервиса - необходимо выполнить запрос : http://localhost:8086/books/lastUpdates?now=2024-08-11


# Локальный запуск:

1. Выполнить в корне 
```shell
docker-compose -f docker-compose-env-only.yml up
```


#Тестирование редиса:
1. запустить утилиту redis-cli
2. выполнить запрос : http://localhost:8086/books/lastUpdates?now=2024-08-11
3. проверить состояние кеша keys '*'
