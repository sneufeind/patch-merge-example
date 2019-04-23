package demo.sneufeind.patchmergeexample.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "Authors")
@Data
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "givenname")
    private String givenname;
    @Column(name = "name")
    private String surname;

}
