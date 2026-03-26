package org.td5.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.td5.entity.enums.CategoryEnum;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {
    private Integer id;
    private String name;
    private CategoryEnum category;
    private Double price;
    @JsonIgnore
    private List<StockMovement> stockMovementList;
}
