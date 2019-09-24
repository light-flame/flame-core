# Light flame

Ligth flame is a modern Era ultra **light height web framework** based on netty and made for people who like to have more control over the application, since the input of the data entrance, to the output. Everything is highly configurable... So, if you are used to frameworks that make all the things for you like DI, handlers, services and repositories and all the stuffs with a lot of annotations and like it, light flame insn't for you.

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
package com.init;

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
package com.init;

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
package com.init;

import io.lightflame.http.FlameHttpFunction;
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
package com.routing;

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
package com.init;


import static org.junit.Assert.assertEquals;


import org.junit.Before;
import org.junit.Test;

import io.lightflame.bootstrap.LightFlame;
import io.lightflame.http.HttpServerHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * TestingHelloWorld
 */
public class TestingHandler {

    EmbeddedChannel channel;

    @Before
    public void createChannel(){
        channel = new EmbeddedChannel(
            new HttpResponseDecoder(), 
            new HttpServerHandler()
        );
    }

    @Before
    public void configureLightFlame(){
        new LightFlame()
            .runConfiguration(new HandlerConfig().setDefautHandlers(), null);
    }

    @Test
    public void simpleHelloWorld(){
        FullHttpRequest httpRequest = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, 
                HttpMethod.GET, 
                "/api/hello/world/simple",
                Unpooled.copiedBuffer("world", CharsetUtil.UTF_8)
        );
        channel.writeInbound(httpRequest);

        // get response
        FullHttpResponse ctx = channel.readOutbound();
        String msg = ctx.content().toString(CharsetUtil.UTF_8);
        assertEquals(msg.equals("hello world"), true);

    }

}
```
