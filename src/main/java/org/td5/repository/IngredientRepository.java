package org.td5.repository;

import java.util.List;
import java.util.Optional;
import org.td5.entity.Ingredient;

public interface IngredientRepository {
  List<Ingredient> findAll();

  Optional<Ingredient> findById(Integer id);
}
