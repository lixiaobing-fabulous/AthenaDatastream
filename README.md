# How to start the application

mvn install -DskipTests

First start the configuration server as a name service.
```bash
java -cp AthenaCommons/target/AthenaCommons-1.0-SNAPSHOT.jar com.athena.server.ConfigServer &
```
Then start the master node
```bash
java -cp AthenaStandaloneCluster/target/AthenaStandaloneCluster-1.0-SNAPSHOT.jar:AthenaCommons/target/AthenaCommons-1.0-SNAPSHOT.jar com.athena.StandaloneMaster &
```
Then start two worker nodes
```bash
java -cp AthenaStandaloneCluster/target/AthenaStandaloneCluster-1.0-SNAPSHOT.jar:AthenaCommons/target/AthenaCommons-1.0-SNAPSHOT.jar com.athena.StandaloneWorker1 &
java -cp AthenaStandaloneCluster/target/AthenaStandaloneCluster-1.0-SNAPSHOT.jar:AthenaCommons/target/AthenaCommons-1.0-SNAPSHOT.jar com.athena.StandaloneWorker2 &
```

When we set up all the envirenments, then we can submit our program to the cluster.
```bash
 java -cp AthenaCommons/target/AthenaCommons-1.0-SNAPSHOT.jar:AthenaClient/target/AthenaClient-1.0-SNAPSHOT.jar:AthenaExample/target/AthenaExample-1.0-SNAPSHOT.jar:AthenaCore/target/AthenaCore-1.0-SNAPSHOT.jar:AthenaRuntime/target/AthenaRuntime-1.0-SNAPSHOT.jar com.athena.WordCount
```
