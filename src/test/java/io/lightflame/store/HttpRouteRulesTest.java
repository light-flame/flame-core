package io.lightflame.store;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import io.lightflame.store.HttpRouteRules.RuleEnum;


/**
 * HttpRouteRulesTest
 */
public class HttpRouteRulesTest {

    

    @Test
    public void testCase1(){

        new HttpRouteRules().newRoute()
            .addRule(RuleEnum.PATH, "/path/to/my")
            .set();
        //
        new HttpRouteRules().newRoute()
            .addRule(RuleEnum.PATH, "/path/to/my")
            .addRule(RuleEnum.METHOD, "GET")
            .set();
        //
        new HttpRouteRules().newRoute()
            .addRule(RuleEnum.PATH, "/path/to/my")
            .addRule(RuleEnum.METHOD, "GET")
            .addRule(RuleEnum.QPARAM, "name=carol")
            .set();
        //
        new HttpRouteRules().newRoute()
            .addRule(RuleEnum.PATH, "/path/to/my")
            .addRule(RuleEnum.METHOD, "GET")
            .addRule(RuleEnum.QPARAM, "name=carol")
            .addRule(RuleEnum.HEADER, "x-auth=xlz")
            .set();
        //
        new HttpRouteRules().newRoute()
            .addRule(RuleEnum.PATH, "/path/to/my/url")
            .addRule(RuleEnum.METHOD, "GET")
            .set();
        //
        new HttpRouteRules().newRoute()
            .addRule(RuleEnum.PREFIX, "/path/to/*")
            .addRule(RuleEnum.METHOD, "POST")
            .set();
        //
        new HttpRouteRules().newRoute()
            .addRule(RuleEnum.PREFIX, "/path/to/*")
            .set();
        //
        new HttpRouteRules().newRoute()
            .addRule(RuleEnum.PATH, "/path/to/my/url/{param}")
            .set();

        
        assertEquals(1, HttpRouteRules.processRequest(Arrays.asList("x-auth=abc","GET","/path/to/my","name=daniel")));
        assertEquals(2, HttpRouteRules.processRequest(Arrays.asList("x-auth=abc","GET","/path/to/my","name=carol")));
        assertEquals(3, HttpRouteRules.processRequest(Arrays.asList("x-auth=xlz","GET","/path/to/my","name=carol")));
        assertEquals(4, HttpRouteRules.processRequest(Arrays.asList("x-auth=xlz","GET","/path/to/my/url","name=carol")));
        assertEquals(5, HttpRouteRules.processRequest(Arrays.asList("x-auth=xlz","POST","/path/to/my/life","name=carol")));
        assertEquals(6, HttpRouteRules.processRequest(Arrays.asList("x-auth=xlz","GET","/path/to/my/life","name=carol")));
        assertEquals(6, HttpRouteRules.processRequest(Arrays.asList("x-auth=xlz","GET","/path/to/my/url/bigger/more","name=carol")));
        assertEquals(7, HttpRouteRules.processRequest(Arrays.asList("x-auth=xlz","GET","/path/to/my/url/bigger","name=carol")));
    }
}