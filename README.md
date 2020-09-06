# Сборка

Для сборки jar:
```
$mvn compile assembly:single
```
Расположение артефакта:
- ./target/getDataFromBase-1.0-jar-with-dependencies.jar

# Набор данных для тестирования
- Дамп базы данных:
./dump_dateBase/testDataBase

- Тестовый INPUT файл для команды "SEARCH"
./filesFromTest/inFiles/search_in.json

- Тестовый INPUT файл для команды "STAT"
./filesFromTest/inFiles/stat_in.json

- OUTPUT файлы
./filesFromTest/outFiles/stat_out.json
./filesFromTest/outFiles/search_out.json


# Запуск

```
$java -jar getDataFromBase-1.0-jar-with-dependencies.jar [stat | search] [путь к input файлу] [путь к output файлу]
```

Запуск приложения с тестовыми фалами:
- stat:
```
java -jar ./target/getDataFromBase-1.0-jar-with-dependencies.jar stat ./filesFromTest/inFiles/stat_in.json ./filesFromTest/outFiles/stat_out.json
```
- search:
```
java -jar ./target/getDataFromBase-1.0-jar-with-dependencies.jar search ./filesFromTest/inFiles/search_in.json ./filesFromTest/outFiles/search_out.json
```


