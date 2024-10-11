package com.rent.game.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SteamService {
    private static final Logger logger = LoggerFactory.getLogger(SteamService.class);


    @Value("${autohotkey.path}")
    private String autoHotkeyPath;

    @Value("${steam.script.path}")
    private String scriptPath;

    public void launchStem (String username , String password ){
        try {
            logger.info("Launching steam . . .");
            ProcessBuilder processBuilder = new ProcessBuilder(autoHotkeyPath , scriptPath , username , password);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw  new RuntimeException("Failed to execute auto hotkey script");
            }
        }catch (IOException  | InterruptedException e ) {
            throw new RuntimeException("Failed to execute commandss : " + e.getMessage() , e);
        }
    }
}
