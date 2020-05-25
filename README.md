# elasticsearch-sql

## Setup requirements
- ElasticSearch 7.7
- Java 11

Add the entry below to your config/elasticsearch.yml file

**xpack.ml.enabled: false**

If you have installed Java11 and don't want to amend your existing Java setup, add some environment variables e.g. windows users can run below in your CMD console


`
set JAVA_HOME=C:\Program Files\Java\jdk-11.0.7
`

`
set path=C:\Program Files\Java\jdk-11.0.7\bin;%path%
`

Above applies to all consoles used for running commands.

## Startup

Execute *ES_HOME/bin/elasticsearch[.bat]* to start your ES instance. Using your favourite ES client or just plain commandline, you can optionally create a trial license for subscribed ES products as described at the [ES trial page](https://www.elastic.co/guide/en/elasticsearch/reference/current/start-trial.html). It will mean you can see the JDBC SQL stuff in action. 

To import test data, import es-data.json into your running ES instance e.g. windows users can run

`
curl -s -X POST -HContent-type:application/x-ndjson --data-binary @es-data.json http://localhost:9200/_bulk
`

Open another console running Java11. Navigate to this GitHub repository and run 

`
gradlew bootRun
`

or

`
./gradlew bootRun
`

With your application running, and your trial ES license, you should be able to see results for 

`
http://localhost:8080/jdbc-sql?text=ebola
`

If you've setup the Gradle project in your IDE, have a look at the other available controller requests and underlying services etc. There are requests managed with 

- ES JDBC client
- ES low-level REST client
- Spring WebClient

Too shutdown the application 

`
curl -X POST localhost:8080/actuator/shutdown
`


