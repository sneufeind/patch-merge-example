package demo.sneufeind.patchmergeexample;

import com.github.fge.jsonpatch.JsonPatchException;
import demo.sneufeind.patchmergeexample.model.Author;
import demo.sneufeind.patchmergeexample.model.GetAuthorsResponse;
import demo.sneufeind.patchmergeexample.util.JsonMergePatchUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RestApi {

    private final AuthorRepository authorRepository;

    @Autowired
    RestApi(final AuthorRepository authorRepository){
        this.authorRepository = authorRepository;
    }

    private Optional<Author> readAuthorById(final Long id){
        return this.authorRepository.findById(id);
    }

    @GetMapping(path = "/authors", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetAuthorsResponse> getAuthors(){
        final List<Author> authors = this.authorRepository.findAll();
        return ResponseEntity.ok(new GetAuthorsResponse(authors));
    }

    @GetMapping(path = "/authors/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Author> getAuthor(@PathVariable("id") final Long id){
        final Optional<Author> authorOpt = readAuthorById(id);
        return authorOpt.isPresent() ?
                ResponseEntity.ok(authorOpt.get()) :
                ResponseEntity.notFound().build();
    }

    @PatchMapping(path = "/authors/{id}", consumes = {"application/merge-patch+json", MediaType.APPLICATION_JSON_UTF8_VALUE}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Author> getAuthor(@PathVariable("id") final Long id, @RequestBody final String json) {
        final Optional<Author> authorOpt = readAuthorById(id);
        if(authorOpt.isEmpty())
            return ResponseEntity.notFound().build();

        try {
            final Author updatedAuthor = JsonMergePatchUtils.mergePatch(authorOpt.get(), json, Author.class);
            return ResponseEntity.ok(this.authorRepository.save(updatedAuthor));
        } catch (IOException | JsonPatchException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
