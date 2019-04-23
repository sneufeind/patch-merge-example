package demo.sneufeind.patchmergeexample.model;

import lombok.Data;

import javax.persistence.*;

//@Entity
//@Table(name = "Books")
@Data
public class Book {

    private Integer pages;
    private String title;
    private String isbn;

}
