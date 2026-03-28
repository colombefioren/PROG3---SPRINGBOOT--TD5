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
import org.td5.entity.Ingredient;
import org.td5.entity.StockMovement;
import org.td5.entity.StockValue;
import org.td5.entity.enums.CategoryEnum;
import org.td5.entity.enums.MovementTypeEnum;
import org.td5.entity.enums.UnitType;
import org.td5.repository.IngredientRepository;

@Repository
@AllArgsConstructor
public class IngredientRepositoryImpl implements IngredientRepository {
  private final DataSource dataSource;

  @Override
  public List<Ingredient> findAll() {
    String sql =
"""
select i.id as i_id, i.name as i_name, i.price as i_price, i.category as i_category
                            from ingredient i
                            order by i.id
""";

    List<Ingredient> ingredients = new ArrayList<>();

    try (Connection conn = dataSource.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {

      while (rs.next()) {
        ingredients.add(mapResultSetToIngredient(rs));
      }
      return ingredients;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<Ingredient> findById(Integer id) {
    String sql =
        """
                              select i.id as i_id, i.name as i_name, i.price as i_price, i.category as i_category from ingredient i where i.id = ?
            """;

    try (Connection conn = dataSource.getDBConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, id);

      try (ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
          return Optional.of(mapResultSetToIngredient(rs));
        }
      }

      return Optional.empty();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private Ingredient mapResultSetToIngredient(ResultSet rs) throws SQLException {
    return Ingredient.builder()
        .id(rs.getInt("i_id"))
        .name(rs.getString("i_name"))
        .category(CategoryEnum.valueOf(rs.getString("i_category")))
        .price(rs.getDouble("i_price"))
        .stockMovementList(findStockMovementsByIngredientId(rs.getInt("i_id")))
        .build();
  }
}
