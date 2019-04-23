package demo.sneufeind.patchmergeexample.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Books")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;
    @OneToOne
    @JoinColumn(name="author_id", nullable=false)
    private Author author;

}
