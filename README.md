Программа индексации и поиска данных.

Запуск приложения:
 - открываем в среде разработки,
 - переходим на вкладку терминала,
 - "cd docker" -> "docker compose up"
 - запускаем приложение
 - открываем браузер
 - вводим: "http://localhost:8080/"
 - используем по назначению

Особенности управления:
 - при активации индексации("START INDEXING") - кликнуть по иконке два раза(не двойной щелчок).
 - при обновлении страницы(с запущенным процессом индексации) - иконка измениться на исходную,
   кликнуть один раз - "STOP INDEXING".  
 - добавить присутствующий сайт - все данные о нем обнуляться и индексация по этому сайту начнется заново.
Индексация происходит асинхронно относительно исходных страниц - т.е.
поиск данных происходит одновременно по всем направлениям хаотично.

Описание.

                            Индексация.

 Приложение производит рекурсивный обход по вложенным сайтам, начиная со стартовой страницы.
 В процессе обхода страниц происходит чтение данных с каждой страницы. 
 Все данные сортируются по типам:
 - системные страницы: графические данные(картинки) и системные данные(страницы с кодом)
 - текстовые данные(страницы с содержанием простого текста)
 - невалидные данные(ошибки перехода по URL)

 Процесс индексации происходит в многопоточном режиме.
 Данные со страницы считываются только один раз.
 
                             Поиск.
 
Поиск происходит в многопоточном режиме.
Из искомого слова получается ключ, по ключу из базы данных сразу получаем ответ.
Для каждого потока отводиться только своя индивидуальная область поиска.
Основная задача потоков - определить релевантность и внести данные в нужном формате в ответ. 
Поиск можно проводить только по словам(в соответствии с техническим требованием,
может быть расширено с целью поиска кода, ошибок перехода, картинок).

                           Хранилище.

В приложении предусмотрено работа в PostgresSQL.

Все возможные операции происходят при помощи Prepared Statement.
Данные для сохранения формируются в момент сохранения. 
Данная модель работы с базой данных позволяет избежать мошеннических операций.

Техническое описание приложения:

Структура.
 - searchengine(основной пакет).
    - config (конфигурация beans)
    - controllers(классы контроллеры - web API)
    - dto: 
       - entity(модели объектов для работы внутри приложения, с базой данных)
       - mapper(маппинг данных из базы данных)
       - model(объекты для работы с контроллером: запросами и ответами)
       - statistics(объекты для формирования статистики)
    - searching(раздел логики).
      - processing(подключение, обработка, сохранение, поиск)
      - repository(управление репозиторием)
      - service(взаимодействие с контроллером)

      

                       Процессы.

Индексация.

Каждый поток проходит только один раз по url!

Рекурсия происходит в следующем цикле обработки:
 - подключаемся к источнику данных(выполняем переход по url - FoundDataSite.class)
 - считываем данные(FoundDataSite.class)
 - распределяем(DataSearchEngine.class)
 - сохраняем(ManagementRepository.class)
 - страница помечается как проиндексированная
 - запускаем цикл еще раз(до полного считывания всех вложенных страниц)

  В приложении многопоточность происходит без метода "join".
Это сделано с целью не занимать ресурсы и для ускорения.

Остановка процесса индексации произойдет после того, как закончатся вложенные страницы.
Предусмотрено принудительная остановка, сохраняется пройденная индексация.

Поиск.

Для каждого потока предусмотрена только своя область поиска:
ни при каких условиях потоки не смогут обрабатывать одну сущность вместе!

Основной механизм построен только на boolean операциях, с целью ускорения. 

Из искомого слова получаем код - лемма.

По коду из базы данных получаем список слов и определяем релевантность.
После все полученные данные формируются в один ответ и отправляются.

Описание основных классов:

 ManagementRepository.class - предназначен для работы с данными в базе данных.
 - сохранение данных.
 - получение данных.

 ProjectManagement.class - предназначен для управления функциями приложения.
 - запуск индексации.
 - остановка индексации.
 - запуск поиска.
 - формирование конечного ответа поиска.

 ProjectService.class - предназначен для работы с контроллером.
 - соединяет логику и контроллер.

 StatisticBuilder.class - формирование текущей статистики.

 FixedValue.class - фиксированные величины примитивов и статические методы.

 ProjectMorphology - получение лемм из слов.

 FoundDataSite - получение данных с сайта.

 DataIndexingEngine - сортировка полученных данных с сайта.
 
 DataSearchEngine - построение списка слов с кроткими отрывками из теста.

 