package org.td5.service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.td5.entity.Ingredient;
import org.td5.entity.StockMovement;
import org.td5.entity.StockMovementBody;
import org.td5.entity.StockValue;
import org.td5.entity.enums.UnitType;
import org.td5.exception.NotFoundException;
import org.td5.repository.IngredientRepository;

@Service
@AllArgsConstructor
public class IngredientService {
  private final IngredientRepository repository;

  public List<Ingredient> getAllIngredients() {
    return repository.findAll();
  }

  public Ingredient getIngredientById(Integer id) throws NotFoundException {
    return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Ingredient.id=" + id + " is not found"));
  }

  public StockValue getIngredientStockValue(Integer ingredientId, Instant at, UnitType unit) throws NotFoundException {
    Ingredient ingredient = repository.findById(ingredientId)
            .orElseThrow(() -> new NotFoundException("Ingredient.id=" + ingredientId + " is not found"));
    return ingredient.getStockValue(at, unit);
  }

  public List<StockMovement> getIngredientStockMovements(Integer id, Instant from, Instant to) throws NotFoundException {
    Ingredient ingredient = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Ingredient.id=" + id + " is not found"));
    return ingredient.getStockMovements(from,to);
  }

  public List<StockMovement> postIngredientStockMovements(Integer id, List<StockMovementBody> stockMovementBodies) throws NotFoundException {
    repository.findById(id).orElseThrow(() -> new NotFoundException("Ingredient.id=" + id + " is not found"));


    List<StockMovement> movements = stockMovementBodies.stream().map(stb ->
            StockMovement.builder().type(stb.getMovementType()).value(StockValue.builder().quantity(stb.getValue()).unit(stb.getUnit()).build()).build()).toList();
    return repository.createStockMovementsByIngredientId(id,movements);

  }
}
