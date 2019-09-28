# Light flame

Ligth flame is a modern Era ultra **light height web framework** based on netty and made for people who  like to have more control over the application, since the input of the data entrance, to the output. Everything is highly configurable... 

If you want to configure all by yourself, and have all control of your code, business logical without infer directly with the framework, so, light flame its perfectly made for you. We also encourage you to use it In a way that your code could be totally readable and decouple from the infra parts.


- low level, low abstraction , high performance, fast compile and start time
- Composite everywhere and everything, **its functions babe!**
- microservices mindset, without application services (no tomcat, jboss....)
- high configurability, before and during running time
- testing everything, intregrate tests as pure functions
- no mixing between domain and infra
- reactive non blocking IO
- non reflections, annotations free (thanks god!!)
- do whatever you want, we are on the ground =P

 
You can have some of our boiler plates example of projects using our engine and some principles of DDD, and microservices structure. I hope you enjoy, use it and help us to improve even more this simple code.

# Instalation

Using maven, declare dependency:
```maven
<dependency>
	<groupId>com.github.light-flame</groupId>
	<artifactId>lightflame-core</artifactId>
	<version>0.9.2</version>
</dependency>
```

# Quick Start


```java
package init;

import io.lightflame.bootstrap.LightFlame;

public class App 
{
    public static void main( String[] args )
    {
        new LightFlame()
            .addBasicLog4jConfig()
            .addHttpAndWsListener(8080)
            .start(App.class);
    }
}

```

create a class that contain the configuration function, in this example, HandleConfig:
```java
package init;

import io.lightflame.bootstrap.ConfigFunction;
import io.lightflame.http.FlameHttpStore;

public class HandlerConfig {

    public ConfigFunction setDefautHandlers() {
        return (config) -> {
            Handler handler = new Handler();

            // flame store
            FlameHttpStore fs =  new FlameHttpStore("/api");

            fs.R().httpGET("/hello/world/simple", handler.simpleGreeting());

            return null;
        };
    }
}
```
Now you can declare the simple handler:
```java
package init;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class Handler {

    public FlameHttpFunction simpleGreeting() {
        return (ctx) -> {
            String name = ctx.getRequest().content().toString(CharsetUtil.UTF_8);
            String greeting = String.format("hello %s", name);
            return ctx.setResponse(new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, 
                Unpooled.copiedBuffer(greeting, CharsetUtil.UTF_8)
            ));
        };
    }
}
```

# Routing rules

There is a bunch of existing rules that you can to route to your handler. The **FlameHttpStore** provides a way to store your functions and generate the rule for all.

```java
package routing;

import io.lightflame.bootstrap.ConfigFunction;
import io.lightflame.http.FlameHttpStore;

public class HandlerConfig {

    public ConfigFunction setDefautHandlers() {
        return (config) -> {
            Handler handler = new Handler();


            // flame store
            FlameHttpStore  fs  =  new  FlameHttpStore("/api");

            fs.R().httpGET("/*", handler.simpleGreeting()); // widecard route
            fs.R().httpGET("/path/to/my/url", handler.simpleGreeting());
            fs.R().httpGET("/hello/{name}", handler.simpleGreeting()); // dynamic route
            fs.R().httpPOST("/this/is/a/post", handler.simpleGreeting());
            // complex filters
            fs.R()
                .headerRule("x-auth","abc")
                .queryRule("name","daniel")
                .pathRule("name","daniel")
                .httpALL("/*", handler.simpleGreeting());

            return null;
        };
    }
}
```

# Router middleware
TODO: on next release

# Testing

Lightflame provides a simple way to test you application. You can test either if the route works depending on request, and all the steps throw the route.  

```java
package init;

public class TestingHandler {
}

```
# Multi port

You can open multiple ports to work. and declare different or the same function to each port. Look at this simple example how it works. Its simple as look like:

```java
package multihttp;

import io.lightflame.bootstrap.LightFlame;

public class App 
{
    public static void main( String[] args )
    {
        new LightFlame()
                .addBasicLog4jConfig()
                .addHttpAndWsListener(8080)
                .addHttpAndWsListener(8090)
                .start(App.class);
    }
}

```

than when you configure the project you just have to declare the port on constructor of **FlameHttpStore** like this:

```java
package multihttp;

import io.lightflame.bootstrap.ConfigFunction;
import io.lightflame.http.FlameHttpStore;
import routing.Handler;

public class HandlerConfig {

    public ConfigFunction setDefautHandlers() {
        return (config) -> {
            Handler handler = new Handler();

            // flame store to port 8080
            FlameHttpStore fs1 = new FlameHttpStore(8080,"/api");

            fs1.R().httpGET("/*", handler.simpleGreeting()); // widecard route
            fs1.R().httpGET("/path/to/my/url", handler.simpleGreeting());

            // flame store to port 8080
            FlameHttpStore fs2 = new FlameHttpStore(8090,"/api");

            fs2.R().httpGET("/*", handler.simpleGreeting()); // widecard route
            fs2.R().httpGET("/path/to/my/url", handler.simpleGreeting());

            return null;
        };
    }
}
```