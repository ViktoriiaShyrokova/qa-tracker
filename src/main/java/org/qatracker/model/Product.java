package org.qatracker.model;

public class Product {
    private static int counter;
    private String name;
    private double price;
    private int stock;
    private int instanceCount;  // у каждого объекта свое значение

    public Product(String name) {
        this.name = name;
        this.instanceCount++;   // увеличивает счетчик только для объекта
        counter++;
        //this.instanceCount = counter + 1;
    }

    public Product(String name, double price, int stock) {
        this.name = name;
        this.instanceCount++;   // увеличивает счетчик только для объекта
        counter++;
        setPrice(price);
        this.stock = stock;
        //this.instanceCount = counter + 1;
    }

    public int getInstanceCount() {
        return instanceCount;
    }

    public static int getCounter() {
        return counter;
    }

    public void setPrice(double price) {
        if(price < 0) throw new IllegalArgumentException("Price can not be negative!");
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public boolean isInStock() {
        return stock > 0;
    }

    public void applyDiscount(double percent) {
        setPrice(price*(1-percent/100));
    }
}
