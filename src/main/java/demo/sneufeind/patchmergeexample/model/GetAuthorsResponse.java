package demo.sneufeind.patchmergeexample.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAuthorsResponse {

    private List<Author> authors;

}
