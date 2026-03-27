package org.td5.repository;

import org.td5.entity.Ingredient;
import org.td5.entity.IngredientStock;
import org.td5.entity.enums.UnitType;

import java.time.Instant;
import java.util.List;

public interface IngredientRepository {
    List<Ingredient> findAll();
    Ingredient findById(Integer id);
    IngredientStock getIngredientStockById(Integer id, Instant at, UnitType unit);

}
