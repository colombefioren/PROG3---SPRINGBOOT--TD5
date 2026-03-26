package org.td5.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.td5.entity.Ingredient;
import org.td5.service.IngredientService;

import java.util.List;

@RestController
@AllArgsConstructor
public class IngredientController {
    private final IngredientService service;

    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> getAllIngredients(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getAllIngredients());
    }


}
