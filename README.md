# Запуск проекта
1. Необходим установленный и запущенный Docker
2. Выполнить `compose.yaml` из корня проекта для запуска контейнера с PostgeSQL
3. Запустить приложение com.dyalex.personalfinancemephi.PersonalFinanceMephiApplication
4. Перейти в браузере по адресу `http://localhost:8080/registration` и зарегистрировать пользователя
5. Создать кошелек
6. Создать категорию
7. После этого можно создавать транзакции
8. Опционально можно установить лимиты по отдельным категориям
9. Кошельки, лимиты и транзакции можно редактировать и удалять
10. Для выхода из приложения перейти по адресу `http://localhost:8080/logout`

Пример работающего приложения:

![Пример главной страницы работающего приложения](https://github.com/DYAlex/PersonalFinanceMephi/blob/main/example.png?raw=true)

# О проекте

Разработать backend-приложение для управления личными финансами. 
Используя знания полученные в ходе изучения курса ООП. 

Приложение должно предоставлять возможность пользователям:
- добавлять расходы и доходы, 
- просматривать статистику по тратам, 
- устанавливать бюджеты и категории расходов.

## Технические требования
Для успешного выполнения проекта ваше backend-приложение для управления личными финансами должно соответствовать следующим требованиям:

1. Хранение данных
- Все данные должны храниться в памяти приложения.
2. Авторизация пользователей
- [x] Реализовать функциональность для авторизации пользователей по логину и паролю. Приложение должно поддерживать несколько пользователей.
3. Функционал управления финансами
- [x] Разработать логику для добавления доходов и расходов. 
- [x] Пользователь должен иметь возможность создавать категории для планирования бюджета.
- [x] Предусмотреть функциональность для установления бюджета на каждую категорию расходов.
4. Работа с кошельком пользователя:
- [x] Привязать кошелёк к авторизованному пользователю. 
- [x] Кошелёк должен хранить информацию о текущих финансах и всех операциях (доходах и расходах).
- [x] Сохранять установленный бюджет по категориям.
5. Вывод информации:
- [x] Реализовать возможность отображения общей суммы доходов и расходов, а также данных по каждой категории.
- [x] Выводить информацию о текущем состоянии бюджета для каждой категории, а также оставшийся лимит.
- [x] Поддерживать вывод информации в терминал или в файл.
6. Подсчет расходов и доходов:
- [x] Разработать методы, подсчитывающие общие расходы и доходы, а также по категориям.
- [x] Поддержать возможность подсчета по нескольким выбранным категориям. Если категория не найдена, уведомлять пользователя.
7. Проверка вводимых данных:
- [x] Валидация пользовательского ввода и уведомление о некорректных данных.
8. Оповещения:
- [x] Оповещать пользователя, если превышен лимит бюджета по категории или расходы превысили доходы. Если категория не найдена, уведомлять пользователя.
9. Сохранение данных:
- [x] При выходе из приложения сохранять данные кошелька пользователя в файл.
- [x] При авторизации загружать данные кошелька из файла.
10. Чтение команд пользователя в цикле:
- [x] Реализовать цикл для постоянного чтения команд пользователя.
- [x] Поддержать возможность выхода из приложения.

 
Дополнительное задание (не оценивается):
- Реализовать возможность переводов между кошельками пользователей. При переводе фиксировать расход у отправителя и доход у получателя, идентифицируя их по логину.

## Пример работы
Предположим, что были введены следующие данные:

### Расходы:
- Еда: 300
- Еда: 500
- Развлечения: 3000
- Коммунальные услуги: 3000
- Такси: 1500

### Доходы:
- Зарплата: 20000
- Зарплата: 40000
- Бонус: 3000

### Бюджеты:
- Еда: 4000
- Развлечения: 3000
- Коммунальные услуги: 2500

После выполнения подсчетов приложение должно выводить следующую информацию:

#### Общий доход: 63,000.0
#### Доходы по категориям:
- Зарплата: 60,000.0
#### Общие расходы: 8,300.0
#### Бюджет по категориям:
- Коммунальные услуги: 2500.0, Оставшийся бюджет: -500.0
- Еда: 4000.0, Оставшийся бюджет: 3200.0
- Развлечения: 3000.0, Оставшийся бюджет: 0.0


## Критерии оценивания
1. [x] Реализация авторизации пользователей (2 балла):
2. [x] Взаимодействие с пользователем (2 балла):
3. [x] Управление доходами и расходами (3 балла):
4. [x] Работа с кошельком пользователя (3 балла):
5. [x] Вывод информации (4 балла):
6. [x] Оповещения пользователя (4 балла):
7. [x] Сохранение и загрузка данных (3 балла):
8. [x] Чтение команд в цикле (3 балла):
9. [x] Валидация данных (3 балла):
10. [x] Дополнительные возможности (2 балла):
- Редактирование и удаление кошельков, лимитов и транзакций
11. [x] Разделение функционала по классам (2 балла):
