package com.acelerazg.utils

import groovy.transform.CompileStatic

@CompileStatic
class EnvHandler {
    static void loadEnv(String filePath = ".env") {
        File envFile = new File(filePath)
        if (!envFile.exists()) {
            throw new RuntimeException(".env file not found at: ${filePath}")
        }

        for (String line : envFile.readLines()) {
            line = line.trim()
            if (!line.startsWith("#") && line.contains("=")) {
                String[] parts = line.split("=", 2)
                String key = parts[0].trim()
                String value = parts[1].trim()
                System.setProperty(key, value)
            }
        }
    }

    static String get(String key) {
        return System.getProperty(key)
    }
}