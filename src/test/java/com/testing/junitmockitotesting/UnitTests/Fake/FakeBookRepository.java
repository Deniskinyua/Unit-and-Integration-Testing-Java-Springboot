package com.testing.junitmockitotesting.UnitTests.Fake;

import com.testing.junitmockitotesting.UnitTests.Fake.Entities.Book;
import com.testing.junitmockitotesting.UnitTests.Fake.Repository.BookRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeBookRepository implements BookRepository {

    //Fake Database
    List<Book> books = new ArrayList<>();

    @Override
    public Optional<Book> findBookById(int id) {
        return Optional.ofNullable(books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElse(null));
    }

    @Override
    public List<Book> findAllBooks() {
       return books;
    }

    @Override
    public void addBook(Book book) {
        books.add(book);
    }

    @Override
    public Book saveAndReturn(Book book) {
        books.add(book);
        return book;
    }

    @Override
    public boolean existsById(int id) {
        return false;
    }

    @Override
    public List<Book> findByPublishedDateAfter(LocalDate date) {
        return List.of();
    }
}
