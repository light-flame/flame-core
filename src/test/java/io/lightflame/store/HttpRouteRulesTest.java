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

        new HttpRouteRules().newRoute("1")
            .addRule(RuleEnum.PATH, "/path/to/my")
            .set();
        //
        new HttpRouteRules().newRoute("2")
            .addRule(RuleEnum.PATH, "/path/to/my")
            .addRule(RuleEnum.METHOD, "GET")
            .set();
        //
        new HttpRouteRules().newRoute("3")
            .addRule(RuleEnum.PATH, "/path/to/my")
            .addRule(RuleEnum.METHOD, "GET")
            .addRule(RuleEnum.QPARAM, "name=carol")
            .set();
        //
        new HttpRouteRules().newRoute("4")
            .addRule(RuleEnum.PATH, "/path/to/my")
            .addRule(RuleEnum.METHOD, "GET")
            .addRule(RuleEnum.QPARAM, "name=carol")
            .addRule(RuleEnum.HEADER, "x-auth=xlz")
            .set();
        //
        new HttpRouteRules().newRoute("5")
            .addRule(RuleEnum.PATH, "/path/to/my/url")
            .addRule(RuleEnum.METHOD, "GET")
            .set();
        //
        new HttpRouteRules().newRoute("6")
            .addRule(RuleEnum.PREFIX, "/path/to/*")
            .addRule(RuleEnum.METHOD, "POST")
            .set();
        //
        new HttpRouteRules().newRoute("7")
            .addRule(RuleEnum.PREFIX, "/path/to/*")
            .set();
        //
        new HttpRouteRules().newRoute("8")
            .addRule(RuleEnum.PATH, "/path/to/my/url/{param}")
            .set();

        assertEquals("2", HttpRouteRules.processRequest(Arrays.asList("x-auth=abc","GET","/path/to/my","name=daniel")).getId());
        assertEquals("3", HttpRouteRules.processRequest(Arrays.asList("x-auth=abc","GET","/path/to/my","name=carol")).getId());
        assertEquals("4", HttpRouteRules.processRequest(Arrays.asList("x-auth=xlz","GET","/path/to/my","name=carol")).getId());
        assertEquals("5", HttpRouteRules.processRequest(Arrays.asList("x-auth=xlz","GET","/path/to/my/url","name=carol")).getId());
        assertEquals("6", HttpRouteRules.processRequest(Arrays.asList("x-auth=xlz","POST","/path/to/my/life","name=carol")).getId());
        assertEquals("7", HttpRouteRules.processRequest(Arrays.asList("x-auth=xlz","GET","/path/to/my/life","name=carol")).getId());
        assertEquals("7", HttpRouteRules.processRequest(Arrays.asList("x-auth=xlz","GET","/path/to/my/url/bigger/more","name=carol")).getId());
        assertEquals("8", HttpRouteRules.processRequest(Arrays.asList("x-auth=xlz","GET","/path/to/my/url/bigger","name=carol")).getId());
    }

    @Test
    public void testCase2(){

        new HttpRouteRules().newRoute("1")
            .addRule(RuleEnum.PATH, "/*")
            .set();
        //
        new HttpRouteRules().newRoute("2")
            .addRule(RuleEnum.PATH, "/path/*")
            .set();

        assertEquals("1", HttpRouteRules.processRequest(Arrays.asList("x-auth=abc","GET","/","")).getId());
        assertEquals("2", HttpRouteRules.processRequest(Arrays.asList("x-auth=abc","GET","/path","")).getId());
        assertEquals("2", HttpRouteRules.processRequest(Arrays.asList("x-auth=xlz","GET","/path/to/my","")).getId());

    }
}