# Light Flame

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

## Instalation

Using maven, declare dependency:
```maven
<dependency>
	<groupId>com.github.light-flame</groupId>
	<artifactId>lightflame-core</artifactId>
</dependency>
```

## Quick Start


```java
package init;

import io.lightflame.bootstrap.FlameCore;
import io.lightflame.bootstrap.FlameLog4jConfig;
import io.lightflame.http.BasicHttpWsListener;

public class App {
    public static void main( String[] args ) {
        new FlameCore()
                .addConfiguration(new FlameLog4jConfig().basicConfig())
                .addConfiguration(new HandlerConfig().setDefautHandlers())
                .addListener(new BasicHttpWsListener(8080))
                .start(App.class);
    }
}

```

create a class that contain the configuration function, in this example, HandleConfig:
```java
package init;

import io.lightflame.bootstrap.ConfigFunction;
import io.lightflame.http.FlameHttp;

public class HandlerConfig {

    public ConfigFunction setDefautHandlers() {
        return () -> {
            Handler handler = new Handler();

            // flame store
            FlameHttp fs =  new FlameHttp("/api");

            fs.R().httpGET("/hello/world/simple", handler.simpleGreeting());
        };
    }
}
```
Now you can declare the simple handler:
```java
package init;

import io.lightflame.bootstrap.Flame;
import io.lightflame.http.FlameHttpContext;
import io.lightflame.http.FlameHttpResponse;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class Handler {

    Flame<FlameHttpContext, FlameHttpResponse> simpleGreeting() {
        return (ctx) -> {
            String name = ctx.getRequest().content().toString(CharsetUtil.UTF_8);
            String greeting = String.format("hello %s", name);
            return new FlameHttpResponse(HttpResponseStatus.OK, Unpooled.copiedBuffer(greeting, CharsetUtil.UTF_8));
        };
    }
}
```

## Routing rules

There is a bunch of existing rules that you can to route to your handler. The **FlameHttpStore** provides a way to store your functions and generate the rule for all.

```java
package routing;

import io.lightflame.bootstrap.ConfigFunction;
import io.lightflame.http.FlameHttp;

public class HandlerConfig {

    public ConfigFunction setDefautHandlers() {
        return () -> {
            Handler handler = new Handler();


            // flame store
            FlameHttp fs  =  new FlameHttp("/api");

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
        };
    }
}
```

## Flame Stores

The stores is the core API to designate the entrance of data on API. Lightflame supports:

* Http layer using **FlameHttpStore**
* Websocket layer using **FlameWsStore**
* NSQ message queue declaring on main lightflame class (see example above)
* Apache Kafka [issue #1]
* TCP server [issue #2]

(other supports on demmand. open issue if needed)

## Flame<IN,OUT> chainning API
This is one of the most important thing about lightflame. Everything is function, so every endpoint that you declare you can concatenate one or more function to this handler.

For example, to pass a http function to **FlameHttpStore** you have to declare a Flame function with **FlameHttpContext** as input and a **FlameHttpResponse** as a output. But you can chain multiple functions on the middle that connect the input with the output. Look at this example below:

```java
package com.ws;

import io.lightflame.bootstrap.Flame;
import io.lightflame.http.FlameHttpContext;
import io.lightflame.http.FlameHttpResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.File;
import java.io.FileInputStream;

public class StaticHandler {

    Flame<FlameHttpContext, File> getRootFile() { // 4
        return (ctx) -> {
            return new File(getClass().getClassLoader().getResource("dist/index.html").getFile());
        };
    }

    Flame<FlameHttpContext, File> getOtherFiles() { // 5
        return (ctx) -> {
            String url = ctx.getPathWithoutPrefix();
            return new File(getClass().getClassLoader().getResource("dist/" + url).getFile());
        };
    }

    Flame<File, FlameHttpResponse> proccess() { // 4 and 5
        return (ctx) -> {
            FileInputStream inFile = new FileInputStream(ctx);
            ByteBuf buffer = Unpooled.copiedBuffer(inFile.readAllBytes());
            inFile.close();
            return new FlameHttpResponse(HttpResponseStatus.OK, buffer);
        };
    }
}
```

In this one you have this ordering:

FlameHttpContext -> java.io.File -> FlameHttpResponse

So, you can chain function **proccess()** either 4 or 5!

## Testing

Lightflame provides a simple way to test you application. You can test either if the route works depending on request, and all the steps throw the route.  

```java
package com.helloworld;


import static org.junit.Assert.assertEquals;

import com.helloworld.config.HandlerConfig;

import io.lightflame.bootstrap.FlameLog4jConfig;
import io.lightflame.bootstrap.FlameMock;
import org.junit.Before;
import org.junit.Test;

import io.lightflame.bootstrap.FlameCore;
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
public class TestingHelloWorld {

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
        new FlameMock()
                .addConfiguration(new HandlerConfig().setDefautHandlers())
                .addConfiguration(new FlameLog4jConfig().basicConfig())
                .start(TestingHelloWorld.class);
    }

    @Test
    public void simpleHelloWorld(){
        FullHttpRequest httpRequest = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, 
                HttpMethod.GET, 
                "/api/greeting/world/simple",
                Unpooled.copiedBuffer("world", CharsetUtil.UTF_8)
        );
        channel.writeInbound(httpRequest);

        // get response
        FullHttpResponse ctx = channel.readOutbound();
        String msg = ctx.content().toString(CharsetUtil.UTF_8);
        assertEquals(msg.equals("hello world"), true);

    }

    @Test
    public void simpleHelloUrlParam(){
        FullHttpRequest httpRequest = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, 
                HttpMethod.GET, 
                "/api/greeting/myName"
        );
        channel.writeInbound(httpRequest);

        // get response
        FullHttpResponse ctx = channel.readOutbound();
        String msg = ctx.content().toString(CharsetUtil.UTF_8);
        assertEquals(msg.equals("hello myName"), true);

    }

    @Test
    public void simpleHelloQueryParam(){
        FullHttpRequest httpRequest = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, 
                HttpMethod.GET, 
                "/api/greeting/with//query/param?what=world"
        );
        channel.writeInbound(httpRequest);

        // get response
        FullHttpResponse ctx = channel.readOutbound();
        String msg = ctx.content().toString(CharsetUtil.UTF_8);
        assertEquals(msg.equals("hello world"), true);
    }

    @Test
    public void wideCardUrl(){
        FullHttpRequest httpRequest = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, 
                HttpMethod.GET, 
                "/api/greeting/with/generic?what=world"
        );
        channel.writeInbound(httpRequest);

        // get response
        FullHttpResponse ctx = channel.readOutbound();
        String msg = ctx.content().toString(CharsetUtil.UTF_8);
        assertEquals(msg.equals("hello world"), true);
    }

    @Test
    public void headerParam(){
        FullHttpRequest httpRequest = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, 
                HttpMethod.GET, 
                "/api/greeting/with/header"
        );
        httpRequest.headers().add("what", "world");
        channel.writeInbound(httpRequest);

        // get response
        FullHttpResponse ctx = channel.readOutbound();
        String msg = ctx.content().toString(CharsetUtil.UTF_8);
        assertEquals(msg.equals("hello world"), true);
    }
}
```
## Multi port

You can open multiple ports to work. and declare different or the same function to each port. Look at this simple example how it works. Its simple as look like:

```java
package multihttp;

import io.lightflame.bootstrap.FlameCore;
import io.lightflame.bootstrap.FlameLog4jConfig;
import io.lightflame.http.BasicHttpWsListener;

public class App 
{
    public static void main( String[] args )
    {
        new FlameCore()
                .addConfiguration(new FlameLog4jConfig().basicConfig())
                .addListener(new BasicHttpWsListener(8080))
                .addListener(new BasicHttpWsListener(8090))
                .start(App.class);
    }
}

```

then when you configure the project you just have to declare the port on constructor of **FlameHttpStore** like this:

```java
package multihttp;

import io.lightflame.bootstrap.ConfigFunction;
import io.lightflame.http.FlameHttp;
import routing.Handler;

public class HandlerConfig {

    public ConfigFunction setDefautHandlers() {
        return () -> {
            Handler handler = new Handler();

            // flame store to port 8080
            FlameHttp fs1 = new FlameHttp(8080,"/api");

            fs1.R().httpGET("/*", handler.simpleGreeting()); // widecard route
            fs1.R().httpGET("/path/to/my/url", handler.simpleGreeting());

            // flame store to port 8080
            FlameHttp fs2 = new FlameHttp(8090,"/api");

            fs2.R().httpGET("/*", handler.simpleGreeting()); // widecard route
            fs2.R().httpGET("/path/to/my/url", handler.simpleGreeting());
        };
    }
}
```

## Web Socket and Static files (2 in 1)

You can access the full example on **flame-examples** repository on github. In this one we openned two different ports, one to serve the static files and another to WS, but you can do it using the same port

```java
package com.ws;

import io.lightflame.bootstrap.FlameCore;
import io.lightflame.bootstrap.FlameLog4jConfig;
import io.lightflame.http.BasicHttpWsListener;

public class App {
    public static void main(String[] args) {
        new FlameCore()
                .addConfiguration(new Config().setDefautHandlers())
                .addConfiguration(new FlameLog4jConfig().basicConfig())
                .addListener(new BasicHttpWsListener(8080)) // 1
                .addListener(new BasicHttpWsListener(8081)) // 2
                .start(App.class);
        }
}

```
In this example we:

1. add a listener on port 8080 to static file
2. add a listener on port 8081 to websocket

```java
package com.ws;

import io.lightflame.bootstrap.ConfigFunction;
import io.lightflame.http.FlameHttp;
import io.lightflame.websocket.FlameWs;

public class Config {

    public ConfigFunction setDefautHandlers() {
        return () -> {

            // http
            StaticHandler sHandler = new StaticHandler();
            FlameHttp httpStore = new FlameHttp(8080); // 3
            httpStore.R().httpGET("/", sHandler.getRootFile().and(sHandler.proccess())); // 4
            httpStore.R().httpGET("/static/*", sHandler.getOtherFiles().and(sHandler.proccess())); // 5

            // websocket
            WsHandler wsHandler = new WsHandler();
            FlameWs wsStore =  new FlameWs(8081); // 3
            wsStore.R().path("/ws", wsHandler.webSocketFunc()); // 6

        };
    }
    
}
```

3. Its important to use the same port declared in the main file to instanciate the stores. If you dont do it the handler will throw an error.

4. the first handler execute two functions on context "/" (getRootFile() -> proccess()). This one is responsible to get the root index.html file and the second one process the file

5. this handler execute function on a widecard context after "/static/*". This happens to bring all other files that html will call, like style and js. In this one we used another function as the first one, but we mantain the second one, since the process is the same.

```java
package com.ws;

import io.lightflame.bootstrap.Flame;
import io.lightflame.http.FlameHttpContext;
import io.lightflame.http.FlameHttpResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.File;
import java.io.FileInputStream;

public class StaticHandler {

    Flame<FlameHttpContext, File> getRootFile() { // 4
        return (ctx) -> {
            return new File(getClass().getClassLoader().getResource("dist/index.html").getFile());
        };
    }

    Flame<FlameHttpContext, File> getOtherFiles() { // 5
        return (ctx) -> {
            String url = ctx.getPathWithoutPrefix();
            return new File(getClass().getClassLoader().getResource("dist/" + url).getFile());
        };
    }

    Flame<File, FlameHttpResponse> proccess() { // 4 and 5
        return (ctx) -> {
            FileInputStream inFile = new FileInputStream(ctx);
            ByteBuf buffer = Unpooled.copiedBuffer(inFile.readAllBytes());
            inFile.close();
            return new FlameHttpResponse(HttpResponseStatus.OK, buffer);
        };
    }
}
```

Now we have the static hander with our three functions. The last one is the main function that receive as parameter a File and return the final **FlameHttpResponse**. The first two functions are called from different routes to read the file depending on context.

```java
package com.ws;

import java.util.Arrays;
import java.util.List;

import io.lightflame.bootstrap.Flame;
import io.lightflame.websocket.FlameWsContext;


public class WsHandler {

    private int i = 0;

    private List<String> messages = Arrays.asList( "Hi there, I\"m Fabio and you?", "Nice to meet you", "How are you?", "Not too bad, thanks", "What do you do?", "That\"s awesome", "Codepen is a nice place to stay", "I think you\'re a nice person", "Why do you think that?", "Can you explain?", "Anyway I\'ve gotta go now", "It was a pleasure chat with you", "Time to make a new codepen", "Bye", ":)");

    Flame<FlameWsContext, FlameWsContext> webSocketFunc() { // 6
        return (ctx) -> {
            ctx.writeToChannel(this.messages.get(i));
            i++;
            return ctx;
        };
    }
}
```

at the end we have our final handler for websocket app.