package org.td5.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.td5.entity.enums.DishTypeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Ingredient> getIngredients() {
        return dishIngredients.stream().map(DishIngredient::getIngredient).collect(Collectors.toList());
    }

    public void setDishIngredients(List<DishIngredient> newDishIngredients) {
        if (this.dishIngredients != null && !this.dishIngredients.isEmpty()) {
            this.dishIngredients.clear();
        }

        this.dishIngredients = newDishIngredients == null ? new ArrayList<>() : newDishIngredients;

        for (DishIngredient dishIngredient : this.dishIngredients) {
            if (dishIngredient != null) {
                dishIngredient.setDish(this);
            }
        }
    }
}
