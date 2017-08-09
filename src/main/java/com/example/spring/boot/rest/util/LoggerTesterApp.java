package com.example.spring.boot.rest.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

public class LoggerTesterApp {

    private static final Logger logger = LoggerFactory.getLogger(LoggerTesterApp.class);

    /**
     * @param args
     */
    public static void main(String[] args) {

        new LoggerTesterApp().test();

    }

    private void test() {

        long startTime = System.currentTimeMillis();
        Data request = new Data();

        for (int i = 0; i < 100000000; i++) {

            if (logger.isTraceEnabled()) {
                logger.trace(request.getUserID());
                logger.trace(request.getEntitlementResource().getResourceString());
                logger.trace(request.getEntitlementResource().getActionString());
                logger.trace(request.getContextMap().toString());
            }
        }

        long endTime = System.currentTimeMillis();
        logger.error("With Check Enabled : " + (endTime - startTime) + " ms");

        startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {

            logger.trace(request.getUserID());
            logger.trace(request.getEntitlementResource().getResourceString());
            logger.trace(request.getEntitlementResource().getActionString());
            logger.trace(request.getContextMap().toString());

        }

        endTime = System.currentTimeMillis();
        logger.error("With Check Disabled : " + (endTime - startTime) + " ms");

    }

    class Data {

        String userID = "Sri Radha Krishna";
        Map<String, EntitlementResource> contextMap = new HashMap<String, EntitlementResource>();
        EntitlementResource entitlementResource = new EntitlementResource();

        /**
         * @return the userID
         */
        public String getUserID() {
            return userID;
        }

        /**
         * @return the entitlementResource
         */
        public EntitlementResource getEntitlementResource() {
            return entitlementResource;
        }

        /**
         * @return the entitlementResource
         */
        public Map<String, EntitlementResource> getContextMap() {
            return contextMap;
        }

    }

    class EntitlementResource {

        String resourceString = "Testing";
        String actionString = "Testing";

        /**
         * @return the resourceString
         */
        public String getResourceString() {
            return resourceString;
        }

        /**
         * @return the actionString
         */
        public String getActionString() {
            return actionString;
        }

    }
}
