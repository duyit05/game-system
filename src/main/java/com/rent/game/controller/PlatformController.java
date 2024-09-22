package com.rent.game.controller;

import com.rent.game.dto.PlatformDTO;
import com.rent.game.service.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rent-game/platform")
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    @GetMapping()
    public List<PlatformDTO> getALlPlatforms() {
        return platformService.getAllPlatforms();
    }
}
