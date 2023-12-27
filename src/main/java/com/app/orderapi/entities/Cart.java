package com.app.orderapi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double totalPrice = 0.0;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "cart" , cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    private List<CartItem> cartItemList = new ArrayList<>();

    public void addToCartItemList(CartItem cartItem){
        cartItemList.add(cartItem);
    }
    public void removeFromCartItemList(CartItem cartItem){ cartItemList.remove(cartItem);}
}
