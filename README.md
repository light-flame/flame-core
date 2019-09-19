# Light Flame

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
public  static  void  main( String[] args ) {

new  LightFlame()
	.runConfiguration(new  HandlerConfig().setDefautHandlers(), null)
	.port(8080)
	.start(App.class, args);

}
```

create a class that contain the configuration function, in this example, HandleConfig:
```java
public  class  HandlerConfig {

	public  ConfigFunction  setDefautHandlers() {
		return (config) -> {
			MyHandler  handler  =  new  MyHandler();
			
			// flame store
			FlameHttpStore  fs  =  new  FlameHttpStore("/api");
			fs.R().httpGET("/*", handler.genericRoute());
			fs.R().httpGET("/hello", handler.helloRoute());
			return  null;
		};
	}
}
```
Now you can declare the simple handler:
```java
public  class  MyHandler {

	public  FlameHttpFunction  helloRoute() {
		return (ctx) -> {
			// do somethings
			return  ctx.setResponse(new  DefaultFullHttpResponse(
				HttpVersion.HTTP_1_1, 						
				HttpResponseStatus.OK,
				Unpooled.copiedBuffer(staticFile, CharsetUtil.UTF_8))
			);
		};
	}
}
```

# Routing rules

There is a bunch of existing rules that you can to route to your handler. The **FlameHttpStore** provides a way to store your functions and generate the rule for all.

```java
public  class  HandlerConfig {

	public  ConfigFunction  setDefautHandlers() {
		return (config) -> {
			MyHandler  handler  =  new  MyHandler();
			
			// flame store
			FlameHttpStore  fs  =  new  FlameHttpStore("/api");
			fs.R().httpGET("/*", handler.genericRoute()); // widecard route
			fs.R().httpGET("/path/to/my/url", handler.helloRoute());
			fs.R().httpGET("/hello/{name}", handler.helloDynamic()); // dynamic route
			fs.R().httpPOST("/this/is/a/post", handler.postRouter());
			// complex filters
			fs.R()
				.headerRule("x-auth":"abc")
				.queryRule("name":"daniel")
				.pathRule("name":"daniel")
				.httpALL("/*", handler.complexRoute());
			return  null;
		};
	}
}
```

# Router middleware
TODO: on next release

# Testing

Lightflame provides a simple way to test you application.

```java

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
    public void wideCardUrl(){
        FullHttpRequest httpRequest = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, 
                HttpMethod.GET, 
                "/api/hello/with/generic?what=world"
        );
        channel.writeInbound(httpRequest);

        // get response
        FullHttpResponse ctx = channel.readOutbound();
        String msg = ctx.content().toString(CharsetUtil.UTF_8);
        assertEquals(msg.equals("hello world"), true);
    }
```
