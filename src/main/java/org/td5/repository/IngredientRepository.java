package org.td5.repository;

import java.util.List;
import org.td5.entity.Ingredient;
import org.td5.entity.StockMovement;

public interface IngredientRepository {
    List<Ingredient> findAll();
    Ingredient findById(Integer id);
    List<StockMovement> findStockMovementsByIngredientId(Integer id);

}
