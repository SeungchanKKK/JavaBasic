package com.sparta.delivery.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @ManyToOne(cascade = CascadeType.ALL)
    private Food food;

    @ManyToOne
    private Orders orders;

    public OrderItem(OrderItem tempOrderItem,Food food, int quantity){
       this.quantity= tempOrderItem.getQuantity();
       this.name= food.getName();
       this.price= food.getPrice()*quantity;
       this.food= food;
    }
}
