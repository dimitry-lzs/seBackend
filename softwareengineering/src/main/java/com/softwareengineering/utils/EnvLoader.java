package com.softwareengineering.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class EnvLoader {
    private static boolean loaded = false;
    
    public static void loadEnv() {
        if (loaded) return;
        
        try {
            // Try to load .env file from project root
            String envPath = ".env";
            
            // If running from softwareengineering directory, go up one level
            if (!Paths.get(envPath).toFile().exists()) {
                envPath = "../.env";
            }
            
            if (Paths.get(envPath).toFile().exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(envPath))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty() || line.startsWith("#")) {
                            continue;
                        }
                        
                        int equalsIndex = line.indexOf('=');
                        if (equalsIndex > 0) {
                            String key = line.substring(0, equalsIndex).trim();
                            String value = line.substring(equalsIndex + 1).trim();
                            
                            // Only set if not already set in system environment
                            if (System.getenv(key) == null) {
                                System.setProperty(key, value);
                            }
                        }
                    }
                }
                System.out.println("Loaded environment variables from .env file");
            }
        } catch (IOException e) {
            System.out.println("Could not load .env file (this is OK in production): " + e.getMessage());
        }
        
        loaded = true;
    }
}
