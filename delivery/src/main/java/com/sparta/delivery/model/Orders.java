package com.sparta.delivery.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String restaurantName;

    @Column(nullable = false)
    private int totalPrice;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "orders_id")
    private List<OrderItem> foods;

    public Orders(Restaurant restaurant, int totalPrice , List<OrderItem> orderItemList){
      this.restaurantName= restaurant.getName();
      this.totalPrice= totalPrice;
      this.foods= orderItemList;
    }
}