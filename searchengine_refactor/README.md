Программа индексации и поиска данных.

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

В приложении предусмотрено работа в MongoDB.

Техническое описание приложения:

Структура.
 - searchengine(основной пакет).
    - config (конфигурация beans)
    - controllers(классы контроллеры - web API)
    - dto (модели объектов для работы внутри приложения, с базой данных)
    - searching(раздел логики).
      - processing(подключение, обработка, сохранение, поиск)
      - repository(управление репозиторием)
      - service(взаимодействие с контроллером)
      - storage(репозитории(для каждой таблицы в бд))  

Описания процессов.
 
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

Из искомого слова получаем код - несколько первых символов.
 Для слов до 5 символов включительно - первые 3 символа.
 Для слов более 5 символов - первые 5 символов.

По коду из базы данных получаем список слов и определяем релевантность.
После все полученные данные формируются в один ответ и отправляются.

Поиск на основании лемм вполне возможен, но только при условии:
 если следующие слова будут иметь один смысл:
  - стол, подстолье, настольный, застолье
  - захват, подхват, ухватившись
  - краска, красивый, краса, покрас

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

 StatisticBuilder.class - предназначен для формирования текущей статистики.
 