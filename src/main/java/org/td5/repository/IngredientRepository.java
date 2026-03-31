package org.td5.repository;

import java.util.List;
import java.util.Optional;
import org.td5.entity.Ingredient;
import org.td5.entity.StockMovement;
import org.td5.entity.StockMovementBody;

public interface IngredientRepository {
  List<Ingredient> findAll();

  Optional<Ingredient> findById(Integer id);

  void createStockMovementsByIngredientId(Integer id, List<StockMovement> movements);
}
