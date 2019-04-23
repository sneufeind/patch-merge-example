package demo.sneufeind.patchmergeexample;

import com.github.fge.jsonpatch.JsonPatchException;
import demo.sneufeind.patchmergeexample.model.Author;
import demo.sneufeind.patchmergeexample.model.Book;
import demo.sneufeind.patchmergeexample.model.GetAuthorsResponse;
import demo.sneufeind.patchmergeexample.model.GetBooksResponse;
import demo.sneufeind.patchmergeexample.util.JsonMergePatchUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RestApi {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired
    RestApi(
            final AuthorRepository authorRepository,
            final BookRepository bookRepository
    ){
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    private Optional<Author> readAuthorById(final Long id){
        return this.authorRepository.findById(id);
    }

    private Optional<Book> readBookById(final Long id){
        return this.bookRepository.findById(id);
    }

    /////////////////////////////// AUTHORS //////////////////////////////////

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
    public ResponseEntity<Author> updateAuthor(@PathVariable("id") final Long id, @RequestBody final String json) {
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

    /////////////////////////////// BOOKS //////////////////////////////////

    @GetMapping(path = "/books", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<GetBooksResponse> getBooks(){
        final List<Book> books = this.bookRepository.findAll();
        return ResponseEntity.ok(new GetBooksResponse(books));
    }

    @GetMapping(path = "/books/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Book> getBook(@PathVariable("id") final Long id){
        final Optional<Book> bookOpt = readBookById(id);
        return bookOpt.isPresent() ?
                ResponseEntity.ok(bookOpt.get()) :
                ResponseEntity.notFound().build();
    }

    @PostMapping(path = "/books", consumes = {"application/merge-patch+json", MediaType.APPLICATION_JSON_UTF8_VALUE}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Book> createBook(@PathVariable("id") final Long id, @RequestBody final Book newBook) {
        final Optional<Book> bookOpt = readBookById(id);
        if(bookOpt.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(this.bookRepository.save(bookOpt.get()));
    }

    @PatchMapping(path = "/books/{id}", consumes = {"application/merge-patch+json", MediaType.APPLICATION_JSON_UTF8_VALUE}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Book> updateBook(@PathVariable("id") final Long id, @RequestBody final String json) {
        final Optional<Book> bookOpt = readBookById(id);
        if(bookOpt.isEmpty())
            return ResponseEntity.notFound().build();

        try {
            final Book updatedBook = JsonMergePatchUtils.mergePatch(bookOpt.get(), json, Book.class);
            this.authorRepository.save(updatedBook.getAuthor()); //TODO this is just necassary, becuase 'Author' is an entity
            return ResponseEntity.ok(this.bookRepository.save(updatedBook));
        } catch (IOException | JsonPatchException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
