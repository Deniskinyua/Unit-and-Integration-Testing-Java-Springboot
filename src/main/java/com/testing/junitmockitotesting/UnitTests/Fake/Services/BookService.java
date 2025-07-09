package com.testing.junitmockitotesting.UnitTests.Fake.Services;

import com.testing.junitmockitotesting.UnitTests.Fake.Entities.Book;
import com.testing.junitmockitotesting.UnitTests.Fake.Exceptions.BookNotFoundException;
import com.testing.junitmockitotesting.UnitTests.Fake.Exceptions.DuplicateBookException;
import com.testing.junitmockitotesting.UnitTests.Fake.Repository.BookRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public class BookService {

    private final BookRepository bookRepository;
    private final EmailService emailService;
    private static final int RECENT_PUBLICATION_DAYS = 7;

    public BookService(BookRepository bookRepository, EmailService emailService){
        this.bookRepository = bookRepository;
        this.emailService = emailService;
    }

    // Add a book
    @Transactional
    public Book saveAndReturn(Book book) {
        if(book == null)throw new IllegalArgumentException("Book is null.Provide a valid book");
        if (bookRepository.existsById(book.getId())) {
            throw new DuplicateBookException("Book with ID " + book.getId() + " already exists");
        }
        return bookRepository.saveAndReturn(book);
    }


    // Return all books
    public List<Book> findAllBooks(){
        return bookRepository.findAllBooks();
    }

    //
    public Book findBookById(int id){
        return bookRepository.findBookById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id"+id+"not found"));
    }

    public List<Book> applyDiscount(int discountPercentage){
        if(discountPercentage < 0 || discountPercentage > 100)
            throw  new IllegalArgumentException("Discount must be between 0% and 100%");

        LocalDate cutOffDate = LocalDate.now().minusDays(RECENT_PUBLICATION_DAYS);
        List<Book> booksForDiscount = bookRepository.findByPublishedDateAfter(cutOffDate);

        booksForDiscount.forEach(book->{
            book.applyDiscount(discountPercentage);
            bookRepository.saveAndReturn(book);
        });

        return booksForDiscount;
    }


    //Email Messages

}
