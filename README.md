# affr
Amazon Fine Food Reviews competition

# Build
Maven is used as a build tool. So too build project simply run:
*mvn install*
or 
*mvn clean install* if you already built project before.

Executable jar file will be created as a result of build.
You can find it in target file and it is called affr-version.jar
Current version is 1.0.0-SNAPSHOT, so affr-1.0.0-SNAPSHOT.jar will be built. 

# Run
To run project you can simply next command:
java -jar affr-1.0.0-SNAPSHOT.jar "<full path to csv file>" translate=true

*translate=true* means that text of comments from csv file will be sent to mocked Google Translate API.
If this option is not specified then translation will be not performed by default.


