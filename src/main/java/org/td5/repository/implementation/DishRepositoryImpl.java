package org.td5.repository.implementation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.td5.configuration.DataSource;
import org.td5.entity.Dish;
import org.td5.entity.Ingredient;
import org.td5.repository.DishRepository;

import java.util.List;

@Repository
@AllArgsConstructor
public class DishRepositoryImpl implements DishRepository {
    private final DataSource dataSource;

    @Override
    public List<Dish> findAll() {
        return List.of();
    }

    @Override
    public Dish updateIngredientsInDish(Integer dishId, List<Ingredient> ingredientList) {
        return null;
    }
}
