package org.td5.repository.implementation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.td5.configuration.DataSource;
import org.td5.entity.Dish;
import org.td5.entity.DishIngredient;
import org.td5.entity.Ingredient;
import org.td5.entity.enums.CategoryEnum;
import org.td5.entity.enums.UnitType;
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

        try(Connection conn = dataSource.getDBConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
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
        String sql = """
 select di.id as di_id, di.id_dish, di.id_ingredient, di.quantity_required as di_quantity_required, di.unit as di_unit from dish_ingredient di where di.id_dish = ?
""";

        List<DishIngredient> dishIngredients = new ArrayList<>();

        try(Connection conn = dataSource.getDBConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ){

            ps.setInt(1, dishId);

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    dishIngredients.add(mapResultSetToDishIngredient(rs));
                }
            }

            return dishIngredients;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Ingredient findIngredientById(Integer id) {
        String sql =
                """
                                  select i.id as i_id, i.name as i_name, i.price as i_price, i.category as i_category from ingredient i where i.id = ?
                """;

        Ingredient ingredient = null;

        try (Connection conn = dataSource.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    ingredient = mapResultSetToIngredient(rs);
                }
            }

            return ingredient;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Dish mapResultSetToDish(ResultSet rs) throws SQLException {
        return Dish.builder()
                .id(rs.getInt("d_id"))
                .name(rs.getString("d_name"))
                .price(rs.getDouble("d_price"))
                .dishIngredients(findDishIngredientsByDishId(rs.getInt("d_id")))
                .build();
    }
    private Ingredient mapResultSetToIngredient(ResultSet rs) throws SQLException {
        return Ingredient.builder()
                .id(rs.getInt("i_id"))
                .name(rs.getString("i_name"))
                .category(CategoryEnum.valueOf(rs.getString("i_category")))
                .price(rs.getDouble("i_price"))
                .build();
    }


    private DishIngredient mapResultSetToDishIngredient(ResultSet rs) throws SQLException {
        return DishIngredient.builder()
                .id(rs.getInt("di_id"))
                .quantityRequired(rs.getDouble("di_quantity_required"))
                .unit(UnitType.valueOf(rs.getString("di_unit")))
                        .ingredient(findIngredientById(rs.getInt("di_ingredient"))).build();
    }
}
