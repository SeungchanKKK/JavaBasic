package com.sparta.delivery.model;


import com.sparta.delivery.dto.FoodDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @ManyToOne
    @JoinColumn(name="restaurant_id", nullable = false)
    private Restaurant restaurant;

    public Food(FoodDto foodDto, Restaurant restaurant){
        this.name= foodDto.getName();
        this.price=foodDto.getPrice();
        this.restaurant= restaurant;
    }
}
