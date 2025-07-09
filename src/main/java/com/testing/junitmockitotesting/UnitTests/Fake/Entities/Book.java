package com.testing.junitmockitotesting.UnitTests.Fake.Entities;

import java.time.LocalDate;

public class Book {

    private int id;
    private String title;
    private String author;
    private int price;
    private LocalDate publishedDate;

    public Book(int id, String title, String author, int price, LocalDate publishedDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.publishedDate = publishedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void applyDiscount(int discountPercentage) {
        if(discountPercentage < 0 || discountPercentage > 100)
            throw  new IllegalArgumentException("Discount percentage must be between 0 and 100");
        this.price = (int) (this.price * (1 - (discountPercentage/100.0)));
    }
}
