package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {

    private Long id;

    //Item 공통 속성
    private String name;
    private int price;
    private int stockQuantity;

    //Book만 해당하는 속성
    private String author;
    private String isbn;

    public void change(Book item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.stockQuantity = item.getStockQuantity();
        this.author = item.getAuthor();
        this.isbn = item.getIsbn();
    }
}
