package com.example.spring.boot.rest.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.FilterBuilder;
import org.springframework.stereotype.Component;

/**
 * This class is used to find the duplicate code.
 * 
 * @author mlahariya
 * @version 1.0, Dec 2016
 */

public class DuplicateMethodApp {

    static Map<String, String> duplicateCalassA = new HashMap<String, String>();
    static Map<String, String> duplicateCalassB = new HashMap<String, String>();

    public static void main(String... args) {

        DuplicateMethodApp.findOverloadedMethods("com.example.spring.boot.rest.handler", true);
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++");
        for (String key : duplicateCalassA.keySet()) {
            System.out.println();
            System.out.print("Class A: " + duplicateCalassA.get(key));
            System.out.println(" ::::: ");
            System.out.print("Class B: " + duplicateCalassB.get(key));
        }
    }

    public static class MethodInfo {
        @SuppressWarnings("rawtypes")
        Class fromClass;
        ArrayList<Method> methods = new ArrayList<Method>();

        public MethodInfo(@SuppressWarnings("rawtypes") Class fromClass) {
            this.fromClass = fromClass;
        }
    }

    public static HashMap<String, MethodInfo> findOverloadedMethods(String packageToScan, boolean declaredInClass) {

        Set<Class<?>> allClasses = findAllClassesInPackage(packageToScan);
        System.out.println("Number of Classes in " + packageToScan + " = " + allClasses.size());

        HashMap<String, MethodInfo> map = new HashMap<String, MethodInfo>();

        for (@SuppressWarnings("rawtypes")
        Class c : allClasses) {
            try {
                findAllMethodsForClass(c, declaredInClass, map);
            } catch (java.lang.NoClassDefFoundError e) {

                System.err.println(c.getName() + ": java.lang.NoClassDefFoundError");
            }
        }

        return map;
    }

    public static void findAllMethodsForClass(@SuppressWarnings("rawtypes") Class c, boolean declaredInClass,
            HashMap<String, MethodInfo> map) throws java.lang.NoClassDefFoundError {

        Method[] methods = declaredInClass ? c.getDeclaredMethods() : c.getMethods();
        @SuppressWarnings("unused")
        int i = 0;
        for (Method method : methods) {
            i++;
            String key = method.getName() + "(";

            if (method.getParameterTypes().length > 0) {
                for (@SuppressWarnings("rawtypes")
                Class paremeter : method.getParameterTypes()) {
                    key = key + paremeter.getName() + ",";
                }

                key = key.substring(0, key.length() - 1);

            }
            key = key + ")";

            if (!map.containsKey(key)) {
                map.put(key, new MethodInfo(c));
                duplicateCalassB.put(key, method.toString());
            } else {
                duplicateCalassA.put(key, method.toString());
            }

            MethodInfo methodInfo = map.get(key);
            methodInfo.methods.add(method);
        }

    }

    public static Set<Class<?>> findAllClassesInPackage(String packageToScan) {
        Reflections reflections = new Reflections(new SubTypesScanner(false), new ResourcesScanner(),
                new TypeAnnotationsScanner(), new FilterBuilder().include(FilterBuilder.prefix(packageToScan)));
        try {
            return reflections.getTypesAnnotatedWith(Component.class, false);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reflections.getSubTypesOf(Object.class);
    }

}
