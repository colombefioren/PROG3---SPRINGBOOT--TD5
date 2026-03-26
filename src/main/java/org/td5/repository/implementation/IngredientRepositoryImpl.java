package org.td5.repository.implementation;

import org.springframework.stereotype.Repository;
import org.td5.entity.Ingredient;
import org.td5.entity.IngredientStock;
import org.td5.entity.enums.UnitType;
import org.td5.repository.IngredientRepository;

import java.time.Instant;
import java.util.List;

@Repository
public class IngredientRepositoryImpl implements IngredientRepository {
    @Override
    public List<Ingredient> getAllIngredients() {
       String sql =
               """
select i.id as i_id, i.name as i_name, i.price as i_price, i.category as i_category
                            from ingredient i
                            order by i.id
""";

    }

    @Override
    public Ingredient getIngredientById(Integer id) {
        return null;
    }

    @Override
    public IngredientStock getIngredientStockById(Integer id, Instant at, UnitType unit) {
        return null;
    }
}
