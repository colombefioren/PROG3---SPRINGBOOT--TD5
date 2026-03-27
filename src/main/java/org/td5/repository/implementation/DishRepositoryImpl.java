package org.td5.repository.implementation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.td5.configuration.DataSource;
import org.td5.entity.Dish;
import org.td5.entity.DishIngredient;
import org.td5.entity.Ingredient;
import org.td5.repository.DishRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class DishRepositoryImpl implements DishRepository {
    private final DataSource dataSource;

    @Override
    public List<Dish> findAll() {
        String sql =
                """
                            select d.id as d_id, d.name as d_name, d.dish_type, d.selling_price as d_price from dish d order by d.id
                        """;

        List<Dish> dishes = new ArrayList<>();

        try(Connection con = dataSource.getDBConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
            ){

            while (rs.next()) {
                dishes.add(mapResultSetToDish(rs));
            }
            return dishes;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Dish updateIngredientsInDish(Integer dishId, List<Ingredient> ingredientList) {
        return null;
    }

    @Override
    public List<DishIngredient> findDishIngredientsByDishId(Integer dishId) {
        return List.of();
    }

    private Dish mapResultSetToDish(ResultSet rs) throws SQLException {
        return Dish.builder()
                .id(rs.getInt("d_id"))
                .name(rs.getString("d_name"))
                .price(rs.getDouble("d_price"))
                .build();
    }
}
