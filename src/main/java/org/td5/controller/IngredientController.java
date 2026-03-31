package org.td5.controller;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.td5.entity.Ingredient;
import org.td5.entity.StockMovement;
import org.td5.entity.StockMovementBody;
import org.td5.entity.enums.UnitType;
import org.td5.exception.NotFoundException;
import org.td5.service.IngredientService;

@RestController
@AllArgsConstructor
@RequestMapping("/ingredients")
public class IngredientController {
  private final IngredientService service;

  @GetMapping
  public ResponseEntity<List<Ingredient>> getAllIngredients() {
    return ResponseEntity.status(HttpStatus.OK).body(service.getAllIngredients());
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getIngredientById(@PathVariable Integer id) {
    try {
      return ResponseEntity.status(HttpStatus.OK).body(service.getIngredientById(id));
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @GetMapping("/{id}/stock")
  public ResponseEntity<?> getIngredientStockValue(@PathVariable Integer id, @RequestParam(required = false) Instant at, @RequestParam(required = false) UnitType unit) {
    if(at == null || unit == null){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("Either mandatory query parameter `at` or `unit` is not provided");
    }
    try {
      return ResponseEntity.status(HttpStatus.OK).body(service.getIngredientStockValue(id, at, unit));
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @GetMapping("/{id}/stockMovements")
  public ResponseEntity<?> getIngredientStockMovements(@PathVariable Integer id, @RequestParam(required = false) Instant from, @RequestParam(required = false) Instant to) {
    if(from == null || to == null){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
              .body("Either mandatory query parameter `from` or `to` is not provided");
    }
    try{
      return ResponseEntity.status(HttpStatus.OK).body(service.getIngredientStockMovements(id,from,to));
    }catch (NotFoundException e){
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @PostMapping("/{id}/stockMovements")
  public ResponseEntity<?> postIngredientStockMovements(@PathVariable Integer id, @RequestParam(required = false) Instant from, @RequestBody(required = false) List<StockMovementBody> stockMovementBodies) {
    if(stockMovementBodies == null || stockMovementBodies.isEmpty()){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A body must be provided");
    }
    try{
      return ResponseEntity.status(HttpStatus.CREATED).body(service.postIngredientStockMovements(id,stockMovementBodies));
    }
    catch (NotFoundException e){
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    } catch (Exception e) {
return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());    }
  }
}
