package org.td5.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.td5.entity.Dish;
import org.td5.entity.Ingredient;
import org.td5.service.DishService;

@RestController
@RequestMapping("/dishes")
@AllArgsConstructor
public class DishController {
  private final DishService service;

  @GetMapping
  public ResponseEntity<List<Dish>> getAllDishes() {
    return ResponseEntity.status(HttpStatus.OK).body(service.getAllDishes());
  }

  @PutMapping("/{id}/ingredients")
  public ResponseEntity<?> updateIngredientsInDish(@PathVariable Integer id,@RequestBody(required = false) List<Ingredient> ingredientList) {

  }
}
