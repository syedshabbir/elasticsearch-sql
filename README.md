# elasticsearch-sql

## Setup
- ElasticSearch 7.7
- Java 11

## Startup

Add the entry below to your path-to-es.es.yml file

If you have installed Java11 and don't want to amend your existing Java setup, run below in your CMD console

`
set JAVA_HOME=C:\Program Files\Java\jdk-11.0.7
set path=C:\Program Files\Java\jdk-11.0.7\bin;%path%
`

Above applies to all consoles used for running startup commands.

Execute ES_HOME/bin/elasticsearch[.bat] to start ES instance. Using your favourite ES client or just plain commandline, you can optionally create a trial license for subscribed ES products as described at the [ES trial page](https://www.elastic.co/guide/en/elasticsearch/reference/current/start-trial.html). It will mean you can see the JDBC SQL stuff in action. 

Open another console running Java11. Navigate to this GitHub repository and run 

`
gradlew bootRun
`

or

`
./gradlew bootRun
`

If you've setup the Gradle project in your IDE, have a look at the avaiable controller requests and underlying services etc. Alternatively you can just use the URLs below to see data. You may want to refer to post to make sense of it all.
