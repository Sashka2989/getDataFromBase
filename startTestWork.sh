echo "Search"
java -jar ./target/getDataFromBase-1.0-jar-with-dependencies.jar search ./filesFromTest/inFiles/search_in.json ./filesFromTest/outFiles/search_out.json

echo "Stat"
java -jar ./target/getDataFromBase-1.0-jar-with-dependencies.jar stat ./filesFromTest/inFiles/stat_in.json ./filesFromTest/outFiles/stat_out.json

