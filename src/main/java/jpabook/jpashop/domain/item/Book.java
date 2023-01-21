package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jpabook.jpashop.controller.BookForm;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("B")
@Getter @Setter
public class Book extends Item {


    private String author;
    private String isbn;

    public void change(BookForm form) {
        this.setName(form.getName());
        this.setPrice(form.getPrice());
        this.setStockQuantity(form.getStockQuantity());
        this.setAuthor(form.getAuthor());
        this.setIsbn(form.getIsbn());
    }
}
