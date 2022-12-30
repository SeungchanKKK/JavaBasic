package com.sparta.delivery.model;

import com.sparta.delivery.dto.RestaurantDto;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Setter
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int minOrderPrice;

    @Column(nullable = false)
    private int deliveryFee;

    public Restaurant(RestaurantDto restaurantDto){
        this.name=restaurantDto.getName();
        this.minOrderPrice= restaurantDto.getMinOrderPrice();
        this.deliveryFee= restaurantDto.getDeliveryFee();
    }
}
