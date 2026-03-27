package org.td5.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.td5.entity.enums.DishTypeEnum;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dish {
    private Integer id;
    private String name;
    private Double price;
    @JsonIgnore
    private DishTypeEnum dishType;
    @JsonIgnore
    private List<DishIngredient> dishIngredients;
}
