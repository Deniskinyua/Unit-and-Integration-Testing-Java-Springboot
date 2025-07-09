package com.testing.junitmockitotesting.UnitTests.Fake.Repository;

import com.testing.junitmockitotesting.UnitTests.Fake.Entities.Book;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Optional<Book> findBookById(int id);

    List<Book> findAllBooks();

    void addBook(Book book);

    Book saveAndReturn(Book book);

    boolean existsById(int id);

    List<Book> findByPublishedDateAfter(LocalDate date);

}
