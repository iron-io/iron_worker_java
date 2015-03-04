Getting Started
===============

There are three ways to get this package.

1. Add it as a Maven dependency
   Your pom.xml will look something like:

```xml
    <dependencies>
        <dependency>
            <groupId>io.iron.ironworker</groupId>
            <artifactId>ironworker</artifactId>
            <version>1.0.4</version>
        </dependency>
    </dependencies>
```

2. [Download the jar from Maven Repo](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22io.iron.ironworker%22).

3. Build from source with [Apache Buildr](http://buildr.apache.org):

    buildr package

The .jar file will appear under the target directory.


# Basic Usage

Visit http://iron.io for more details.

This is very basic guide. Note that this library supports all IronWorker API features, it just lacks documentation. 

## Create Client

It'll be used for all IronWorker interactions.

```java
import io.iron.ironworker.client.Client;

Client client = new Client("IRON_IO_TOKEN", "IRON_IO_PROJECT_ID");
```

## Create Code Package

This isn't implemented in this library yet, so you need to create zip which will contain all jars you need and __runner__.sh which will simply run java executable.

```sh
root() {
  while [ $# -gt 0 ]; do
    if [ "$1" = "-d" ]; then
      printf "%s\n" "$2"
      break
    fi
  done
}

cd "$(root "$@")"

java -cp xerces.jar -jar worker.jar "$@"

```

You can also use https://github.com/iron-io/iron_worker_ruby_ng/ to create and upload code package.

## Upload Code Package

You need to specify name by which you'll call your worker later and path to zip created at previous step.

```java
import io.iron.ironworker.client.codes.JavaCode;

JavaCode code = new JavaCode("MyWorker", "path/to/MyWorker.zip");
client.createCode(code);
```

## Run It

Prefered way is to use provided builders like Params or TaskOptions, but you can fall back and pass Map&lt;String, Object&gt; instead.

```java
import io.iron.ironworker.client.entities.TaskEntity;
import io.iron.ironworker.client.builders.Params;
import io.iron.ironworker.client.builders.TaskOptions;

// specififying some options
TaskEntity t = client.createTask("MyWorker",
        Params.add("param", 13).add("another", "value"),
        TaskOptions.priority(1).delay(60));

// alternate params syntax
client.createTask("MyWorker", Params.create("param", 13, "another", "value"));
```

### Setting Task Priority

You can specify priority of the task by setting the corresponding parameter of Client.createTask method.

```java
// Run the task with highest priority
TaskEntity t = client.createTask("MyWorker", params, TaskOptions.priority(2));
```

There are 3 possible Options which could be set using TaskOptions:

  - **priority**: Setting the priority of your job. Valid values are 0, 1, and 2. The default is 0.
  - **timeout**: The maximum runtime of your task in seconds. No task can exceed 3600 seconds (60 minutes). The default is 3600 but can be set to a shorter duration.
  - **delay**: The number of seconds to delay before actually queuing the task. Default is 0.

## Schedule Task

```java
ScheduleEntity s = client.createSchedule("MyWorker",
        Params.add("param", 13).add("another", "value"),
        ScheduleOptions.delay(10));
```

There are several possible options which could be set using ScheduleOptions:

  - **runEvery**: The amount of time, in seconds, between runs. By default, the task will only run once. run_every will return a 400 error if it is set to less than 60.
  - **endAt**: The time tasks will stop being queued. Should be an instance of Date.
  - **runTimes**: The number of times a task will run.
  - **priority**: Setting the priority of your job. Valid values are 0, 1, and 2. The default is 0. Higher values means tasks spend less time in the queue once they come off the schedule.
  - **startAt**: The time the scheduled task should first be run. Should be an instance of Date.

## Get Results

At the moment entities just hold data, you need to call client yourself.

```java
  t = client.getTask(t.getId()); // update information
  String status = t.getStatus();

  String log = client.getTaskLog(t.getId()); 
```
