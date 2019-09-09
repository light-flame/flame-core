package io.lightflame.store;

import static org.junit.Assert.assertEquals;

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
        assertEquals(path.getScore("/path/to/url", "GET"), 30);
        assertEquals(path.getScore("/path/to/url/bigger", "GET"), 0);
        
    }

    @Test
    public void testUrlsConditionsWide(){

        HttpUrlScore path = new HttpUrlScore("/path/to/url/*", "GET");

        assertEquals(path.getScore("/", "GET"), 0);
        assertEquals(path.getScore("/path", "GET"), 0);
        assertEquals(path.getScore("/path/to/url", "GET"), 30);
        assertEquals(path.getScore("/path/to/url/bigger", "GET"), 35);
    }

    @Test
    public void testUrlsConditionsDynamic(){

        HttpUrlScore path = new HttpUrlScore("/path/to/{url}", "GET");

        assertEquals(path.getScore("/", "GET"), 0);
        assertEquals(path.getScore("/path", "GET"), 0);
        assertEquals(path.getScore("/path/to/url", "GET"), 30);
        assertEquals(path.getScore("/path/to/url/bigger", "GET"), 0);
    }

    @Test
    public void testUrlsConditionsBigWide(){

        HttpUrlScore path = new HttpUrlScore("/*", "GET");

        assertEquals(path.getScore("/", "GET"), 10);
        assertEquals(path.getScore("/path", "GET"), 20);
        assertEquals(path.getScore("/path/to/url", "GET"), 40);
        assertEquals(path.getScore("/path/to/url/bigger", "GET"), 50);
    }

    @Test
    public void testUrlsConditionsBigDynamic(){

        HttpUrlScore path = new HttpUrlScore("/path/{to}/{url}", "GET");

        assertEquals(path.getScore("/", "GET"), 0);
        assertEquals(path.getScore("/path", "GET"), 0);
        assertEquals(path.getScore("/path/my/url", "GET"), 20);
        assertEquals(path.getScore("/path/to/url/bigger", "GET"), 0);
    }
}