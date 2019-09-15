package io.lightflame.store;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.lightflame.routerules.*;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;


/**
 * HttpRouteRulesTest
 */
public class HttpRouteRulesTest {

    
    @Test
    public void testcase0(){

        RouteStore<FullHttpRequest> rs = new RouteStore<>();

        rs.addRouteRule(
            new RouteRules(StoreKind.HTTP_STORE, "1")
                .addRule(new HttpPathRule("/path/to/my"))
        );

        rs.addRouteRule(
            new RouteRules(StoreKind.HTTP_STORE, "2")
                .addRule(new HttpPathRule("/path/to/my"))
                .addRule(new HttpMethodRule(HttpMethod.GET))
        );

        rs.addRouteRule(
            new RouteRules(StoreKind.HTTP_STORE, "3")
                .addRule(new HttpPathRule("/path/to/my"))
                .addRule(new HttpMethodRule(HttpMethod.GET))
                .addRule(new HttpQParamRule("name", "carol"))
        );

        rs.addRouteRule(
            new RouteRules(StoreKind.HTTP_STORE, "4")
                .addRule(new HttpPathRule("/path/to/my"))
                .addRule(new HttpMethodRule(HttpMethod.GET))
                .addRule(new HttpQParamRule("name", "carol"))
                .addRule(new HttpHeaderRule("x-auth", "xlz"))
        );

        rs.addRouteRule(
            new RouteRules(StoreKind.HTTP_STORE, "5")
                .addRule(new HttpPathRule("/path/to/my/url"))
                .addRule(new HttpMethodRule(HttpMethod.GET))
        );

        rs.addRouteRule(
            new RouteRules(StoreKind.HTTP_STORE, "6")
                .addRule(new HttpPrefixPathRule("/path/to/*"))
                .addRule(new HttpMethodRule(HttpMethod.POST))
        );

        rs.addRouteRule(
            new RouteRules(StoreKind.HTTP_STORE, "7")
                .addRule(new HttpPrefixPathRule("/path/to/*"))
        );

        rs.addRouteRule(
            new RouteRules(StoreKind.HTTP_STORE, "8")
                .addRule(new HttpPathRule("/path/to/my/url/{param}"))
        );


        FullHttpRequest request;

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/path/to/my?name=daniel");
        request.headers().set("x-auth", "abc");
        assertEquals("2", rs.getRouteRules(request, StoreKind.HTTP_STORE).getKey());

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/path/to/my?name=carol");
        request.headers().set("x-auth", "abc");
        assertEquals("3", rs.getRouteRules(request, StoreKind.HTTP_STORE).getKey());

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/path/to/my?name=carol");
        request.headers().set("x-auth", "xlz");
        assertEquals("4", rs.getRouteRules(request, StoreKind.HTTP_STORE).getKey());

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/path/to/my/url?name=carol");
        request.headers().set("x-auth", "xlz");
        assertEquals("5", rs.getRouteRules(request, StoreKind.HTTP_STORE).getKey());

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/path/to/my/life?name=carol");
        request.headers().set("x-auth", "xlz");
        assertEquals("6", rs.getRouteRules(request, StoreKind.HTTP_STORE).getKey());

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/path/to/my/life?name=carol");
        request.headers().set("x-auth", "xlz");
        assertEquals("7", rs.getRouteRules(request, StoreKind.HTTP_STORE).getKey());

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/path/to/my/url/bigger/more?name=carol");
        request.headers().set("x-auth", "xlz");
        assertEquals("7", rs.getRouteRules(request, StoreKind.HTTP_STORE).getKey());

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/path/to/my/url/bigger?name=carol");
        request.headers().set("x-auth", "xlz");
        assertEquals("8", rs.getRouteRules(request, StoreKind.HTTP_STORE).getKey());
        
    }

    @Test
    public void testcase2(){

        RouteStore<FullHttpRequest> rs = new RouteStore<>();

        rs.addRouteRule(
            new RouteRules(StoreKind.HTTP_STORE, "1")
                .addRule(new HttpPrefixPathRule("/*"))
        );

        rs.addRouteRule(
            new RouteRules(StoreKind.HTTP_STORE, "2")
                .addRule(new HttpPrefixPathRule("/path/*"))
        );


        FullHttpRequest request;

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/");
        request.headers().set("x-auth", "abc");
        assertEquals("1", rs.getRouteRules(request, StoreKind.HTTP_STORE).getKey());

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/path");
        request.headers().set("x-auth", "abc");
        assertEquals("2", rs.getRouteRules(request, StoreKind.HTTP_STORE).getKey());

        request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/path/to/my");
        request.headers().set("x-auth", "abc");
        assertEquals("2", rs.getRouteRules(request, StoreKind.HTTP_STORE).getKey());
    }
}