package com.BombingGames.EngineCore;

import java.io.File;

/**
 * A class which helps getting OS specific information
 * @author Martin Brunokowsky
 */
public class WorkingDirectory {
    public static File getWorkingDirectory(String applicationName) {
        String userHome = System.getProperty("user.home", ".");
        File workingDirectory;
        switch (getPlatform()) {
        case linux:
        case solaris:
                workingDirectory = new File(userHome, '.' + applicationName + '/');
                break;
        case windows:
                String applicationData = System.getenv("APPDATA");
                if (applicationData != null)
                        workingDirectory = new File(applicationData, applicationName + '/');
                else
                        workingDirectory = new File(userHome, '.' + applicationName + '/');
                break;
        case macos:
                workingDirectory = new File(userHome, "Library/Application Support/" + applicationName);
                break;
        default:
                workingDirectory = new File(userHome, applicationName + '/');
        }
        if ((!workingDirectory.exists()) && (!workingDirectory.mkdirs()))
                throw new RuntimeException("The working directory could not be created: " + workingDirectory);
        return workingDirectory;
    }

    private static OS getPlatform() {
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.contains("win"))
                    return OS.windows;
            if (osName.contains("mac"))
                    return OS.macos;
            if (osName.contains("solaris"))
                    return OS.solaris;
            if (osName.contains("sunos"))
                    return OS.solaris;
            if (osName.contains("linux"))
                    return OS.linux;
            if (osName.contains("unix"))
                    return OS.linux;
            return OS.unknown;
    }

    private static enum OS {
            linux, solaris, windows, macos, unknown;
    }
}