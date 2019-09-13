package io.lightflame.store;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * FlameHttpStoreTest
 */
public class FlameHttpStoreTest {


    @Test
    public void testUrlsConditions(){

        HttpUrlScore path = new HttpUrlScore("/path/to/url", "GET");

        assertEquals(path.getScore("/", "GET"), 0);
        assertEquals(path.getScore("/path", "GET"), 0);
        assertEquals(path.getScore("/path/to/url", "GET"), 100);
        assertEquals(path.getScore("/path/to/url/bigger", "GET"), 0);
        
    }

    @Test
    public void testUrlsConditionsWide(){

        HttpUrlScore path = new HttpUrlScore("/path/to/url/*", "GET");

        assertEquals(path.getScore("/", "GET"), 0);
        assertEquals(path.getScore("/path", "GET"), 0);
        assertEquals(path.getScore("/path/to/url", "GET"), 4);
        assertEquals(path.getScore("/path/to/url/bigger", "GET"), 5);
    }

    @Test
    public void testUrlsConditionsDynamic(){

        HttpUrlScore path = new HttpUrlScore("/path/to/{url}", "GET"); 

        assertEquals(path.getScore("/", "GET"), 0);
        assertEquals(path.getScore("/path", "GET"), 0);
        assertEquals(path.getScore("/path/to/url", "GET"), 100);
        assertEquals(path.getScore("/path/to/url/bigger", "GET"), 0);
    }

    @Test
    public void testUrlsConditionsBigWide(){

        HttpUrlScore path = new HttpUrlScore("/*", "GET");

        assertEquals(path.getScore("/", "GET"), 1);
        assertEquals(path.getScore("/path", "GET"), 2);
        assertEquals(path.getScore("/path/to/url", "GET"), 4);
        assertEquals(path.getScore("/path/to/url/bigger", "GET"), 5);
    }

    @Test
    public void testUrlsConditionsBigDynamic(){

        HttpUrlScore path = new HttpUrlScore("/path/{to}/{url}", "GET");

        assertEquals(path.getScore("/", "GET"), 0);
        assertEquals(path.getScore("/path", "GET"), 0);
        assertEquals(path.getScore("/path/my/url", "GET"), 100);
        assertEquals(path.getScore("/path/to/url/bigger", "GET"), 0);
    }

    @Test
    public void test3(){
        assertEquals(new HttpUrlScore("/api/hello/world/simple", "GET")
            .getScore("/api/hello/world/simple", "GET"), 100);
        //
        assertEquals(new HttpUrlScore("/api/hello/*", "GET")
            .getScore("/api/hello/world/simple", "GET"), 5);
        //
    }

    public void t(){
        // apresentado uma lista de condicoes, qual bate melhor com

    }
}