package org.td5.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.td5.entity.Ingredient;
import org.td5.exception.NotFoundException;
import org.td5.service.IngredientService;

@RestController
@AllArgsConstructor
public class IngredientController {
  private final IngredientService service;

  @GetMapping("/ingredients")
  public ResponseEntity<List<Ingredient>> getAllIngredients() {
    return ResponseEntity.status(HttpStatus.OK).body(service.getAllIngredients());
  }

  @GetMapping("/ingredients/{id}")
  public ResponseEntity<?> getIngredientById(@PathVariable Integer id) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(service.getIngredientById(id));
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
