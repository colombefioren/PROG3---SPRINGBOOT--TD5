package org.td5.repository.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.td5.configuration.DataSource;
import org.td5.entity.DishIngredient;
import org.td5.entity.enums.UnitType;
import org.td5.repository.DishIngredientRepository;
import org.td5.repository.IngredientRepository;

@Repository
@AllArgsConstructor
public class DishIngredientRepositoryImpl implements DishIngredientRepository {
  private final DataSource dataSource;
  private final IngredientRepository ingredientRepository;

  @Override
  public List<DishIngredient> findByDishId(Integer dishId) {
    String sql =
        """
                 select di.id as di_id, di.id_dish, di.id_ingredient, di.quantity_required as di_quantity_required, di.unit as di_unit from dish_ingredient di where di.id_dish = ?
                """;

    List<DishIngredient> dishIngredients = new ArrayList<>();

    try (Connection conn = dataSource.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(sql); ) {

      ps.setInt(1, dishId);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          dishIngredients.add(mapResultSetToDishIngredient(rs));
        }
      }

      return dishIngredients;

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void saveAll(List<DishIngredient> dishIngredients) {}

  @Override
  public void deleteByDishId(Integer dishId) {}

    private DishIngredient mapResultSetToDishIngredient(ResultSet rs) throws SQLException {
        return DishIngredient.builder()
                .id(rs.getInt("di_id"))
                .quantityRequired(rs.getDouble("di_quantity_required"))
                .unit(rs.getString("di_unit") != null ? UnitType.valueOf(rs.getString("di_unit")) : null)
                .ingredient(ingredientRepository.findById(rs.getInt("id_ingredient")).orElse(null))
                .build();
    }
}
