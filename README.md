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
            <version>1.0.2</version>
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

## Get Results

At the moment entities just hold data, you need to call client yourself.

```java
  t = client.getTask(t.getId()); // update information
  String status = t.getStatus();

  String log = client.getTaskLog(t.getId()); 
```
