package org.td5.repository;

import org.td5.entity.Ingredient;

import java.util.List;

public interface IngredientRepository {
    List<Ingredient> getAllIngredients();
    Ingredient getIngredientById(Integer id);

}
