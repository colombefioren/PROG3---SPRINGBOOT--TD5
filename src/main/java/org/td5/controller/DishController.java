package org.td5.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.td5.entity.Dish;
import org.td5.entity.Ingredient;
import org.td5.exception.NotFoundException;
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
  public ResponseEntity<?> updateIngredientsInDish(
      @PathVariable Integer id, @RequestBody(required = false) List<Ingredient> ingredientList) {
    if (ingredientList == null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("A list of ingredients must be provided");
    }
    try {
      return ResponseEntity.status(HttpStatus.OK)
          .body(service.updateIngredientsInDish(id, ingredientList));
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
