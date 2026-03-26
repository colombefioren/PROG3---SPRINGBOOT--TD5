package org.td5.repository.implementation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.td5.configuration.DataSource;
import org.td5.entity.Ingredient;
import org.td5.entity.IngredientStock;
import org.td5.entity.enums.CategoryEnum;
import org.td5.entity.enums.UnitType;
import org.td5.repository.IngredientRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class IngredientRepositoryImpl implements IngredientRepository {
    private final DataSource dataSource;

    @Override
    public List<Ingredient> getAllIngredients() {
       String sql =
               """
select i.id as i_id, i.name as i_name, i.price as i_price, i.category as i_category
                            from ingredient i
                            order by i.id
""";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            conn = dataSource.getDBConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            List<Ingredient> ingredients = new ArrayList<>();

            while(rs.next()){
                ingredients.add(mapResultSetToIngredient(rs));
            }

            return ingredients;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            dataSource.attemptCloseDBConnection(rs, pstmt, conn);
        }
    }

    @Override
    public Ingredient getIngredientById(Integer id) {
        String sql = """
                  select i.id as i_id, i.name as i_name, i.price as i_price, i.category as i_category from ingredient i where i.id = ?
""";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            conn = dataSource.getDBConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if(rs.next()){
                return mapResultSetToIngredient(rs);
            }else{
                throw new RuntimeException("Ingredient.id=" + id + " is not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IngredientStock getIngredientStockById(Integer id, Instant at, UnitType unit) {
        return null;
    }

    private Ingredient mapResultSetToIngredient(ResultSet rs) throws SQLException {
        return Ingredient.builder()
                .id(rs.getInt("i_id"))
                .name(rs.getString("i_name"))
                .category(CategoryEnum.valueOf(rs.getString("i_category")))
                .price(rs.getDouble("i_price"))
                .build();
    }
}
