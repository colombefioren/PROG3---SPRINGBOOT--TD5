package org.td5.repository.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.td5.configuration.DataSource;
import org.td5.entity.Dish;
import org.td5.entity.Ingredient;
import org.td5.repository.DishIngredientRepository;
import org.td5.repository.DishRepository;

@Repository
@AllArgsConstructor
public class DishRepositoryImpl implements DishRepository {
  private final DataSource dataSource;
  private final DishIngredientRepository dishIngredientRepository;

  @Override
  public List<Dish> findAll() {
    String sql =
        """
                            select d.id as d_id, d.name as d_name, d.dish_type, d.selling_price as d_price from dish d order by d.id
                        """;

    List<Dish> dishes = new ArrayList<>();

    try (Connection conn = dataSource.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {

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

    String deleteSql = "delete from dish_ingredient where id_dish = ?";
    String insertSql = "insert into dish_ingredient (id_dish, id_ingredient) values (?, ?)";

    try (Connection conn = dataSource.getDBConnection()) {

      try (PreparedStatement deletePs = conn.prepareStatement(deleteSql)) {
        deletePs.setInt(1, dishId);
        deletePs.executeUpdate();
      }
      try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {

        for (Ingredient ingredient : ingredientList) {
          if (ingredient != null && ingredient.getId() != null) {
            insertPs.setInt(1, dishId);
            insertPs.setInt(2, ingredient.getId());
            insertPs.addBatch();
          }
        }
        insertPs.executeBatch();
      }

      return findById(dishId).orElse(null);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<Dish> findById(Integer id) {
    String sql =
        """
                      select d.id as d_id, d.name as d_name, d.dish_type, d.selling_price as d_price from dish d where d.id = ?
                    """;
    try (Connection conn = dataSource.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(sql); ) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapResultSetToDish(rs));
        }
      }
      return Optional.empty();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private Dish mapResultSetToDish(ResultSet rs) throws SQLException {
    return Dish.builder()
        .id(rs.getInt("d_id"))
        .name(rs.getString("d_name"))
        .price(rs.getDouble("d_price"))
        .dishIngredients(dishIngredientRepository.findByDishId(rs.getInt("d_id")))
        .build();
  }
}
