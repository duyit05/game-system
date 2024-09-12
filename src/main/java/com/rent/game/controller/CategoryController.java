package com.rent.game.controller;

import com.rent.game.dto.CategoryDTO;
import com.rent.game.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rent-game/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public List<CategoryDTO> getALlCategories() {
        return categoryService.getALlCategory();
    }
}
