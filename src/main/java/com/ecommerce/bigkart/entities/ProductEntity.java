package com.ecommerce.bigkart.entities;

import com.fasterxml.jackson.annotation.JsonTypeId;

import javax.annotation.processing.Generated;


public class ProductEntity {


    private int id;
    private String name;
    private double price;
    private int quantity;

    public ProductEntity(){}

    public ProductEntity(int id, String name, double price, int quantity){
        this.id=id;
        this.name=name;
        this.price=price;
        this.quantity=quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
