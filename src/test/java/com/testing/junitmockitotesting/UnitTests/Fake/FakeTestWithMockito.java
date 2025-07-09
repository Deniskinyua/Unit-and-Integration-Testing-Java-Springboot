package com.testing.junitmockitotesting.UnitTests.Fake;

import com.testing.junitmockitotesting.UnitTests.Fake.Entities.Book;
import com.testing.junitmockitotesting.UnitTests.Fake.Exceptions.BookNotFoundException;
import com.testing.junitmockitotesting.UnitTests.Fake.Exceptions.DuplicateBookException;
import com.testing.junitmockitotesting.UnitTests.Fake.Repository.BookRepository;
import com.testing.junitmockitotesting.UnitTests.Fake.Services.BookService;
import com.testing.junitmockitotesting.UnitTests.Fake.Services.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FakeTestWithMockito {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        reset(bookRepository, emailService);
    }

    private  static List<Book> createSampleBooks(){
        return List.of(new Book(1, "Mocking Birds", "Denis", 2000, LocalDate.now()),
                new Book(2, "Mocking Me", "Denis", 5000, LocalDate.now()));
    }


    @Test
    void findAllBooks_WhenBooksExist_ReturnsBooks() {
        // Arrange
        List<Book> expectedBooks = createSampleBooks();
        when(bookRepository.findAllBooks()).thenReturn(expectedBooks);
        // Act
        List<Book> actualBooks = bookService.findAllBooks();
        // Assert
        assertThat(actualBooks)
                .isNotNull()
                .hasSize(2)
                .containsExactlyElementsOf(expectedBooks);

        verify(bookRepository, times(1)).findAllBooks();
        verifyNoInteractions(emailService);
    }

    @Test
    void findAllBooks_WhenNoBooksExist_ReturnsEmptyList() {
        // Arrange
        when(bookRepository.findAllBooks()).thenReturn(List.of());
        // Act
        List<Book> result = bookService.findAllBooks();
        // Assert
        assertThat(result)
                .isNotNull()
                .isEmpty();
        verify(bookRepository, times(1)).findAllBooks();
    }

    @Test
    void findAllBooks_WhenRepositoryFails_ThrowsException() {
        // Arrange
        when(bookRepository.findAllBooks()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThatThrownBy(() -> bookService.findAllBooks())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Database error");
    }

    @Test
    void saveAndReturn_WhenNewBook_SavesAndReturnsBook(){
        //ARRANGE
        Book providedBook = new Book(1, "Test Book", "Author", 100, LocalDate.now());
        when(bookRepository.saveAndReturn(providedBook)).thenReturn(providedBook);
        when(bookRepository.existsById(1)).thenReturn(false);
        //ACT;
        Book returnedBook = bookService.saveAndReturn(providedBook);
        //Assert
        assertThat(returnedBook)
                .isNotNull()
                .isEqualTo(providedBook);

        verify(bookRepository).saveAndReturn(providedBook);
        verify(bookRepository).existsById(1);
    }

    @Test
    void saveAndReturn_WhenProvidedWithNullInput_ThrowsExceptionWithMessage(){
        //ARRANGE
        assertThatThrownBy(()->bookService.saveAndReturn(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Book is null.Provide a valid book");
    }

    @Test
    void saveAndReturn_WhenProvidedWithDuplicateBook_ThrowsExceptionWithMessage(){
        Book duplicateBook = new Book(1, "Test Book", "Author", 100, LocalDate.now());

        //simulate that the book already exists
        when(bookRepository.existsById(duplicateBook.getId())).thenReturn(true);

        assertThatThrownBy(()-> bookService.saveAndReturn(duplicateBook))
                .isInstanceOf(DuplicateBookException.class)
                .hasMessage("Book with ID " + duplicateBook.getId() + " already exists");

        verify(bookRepository).existsById(duplicateBook.getId());
        verify(bookRepository, never()).saveAndReturn(any());
    }

    @Test
    void saveAndReturn_WhenRepositoryFails_ThrowsException(){
        Book book = new Book(1, "Test Book", "Author", 100, LocalDate.now());
        // Arrange
        when(bookRepository.saveAndReturn(book)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThatThrownBy(() -> bookService.saveAndReturn(book))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Database error");
    }

    @Test
    void findById_WhenGivenExistingBookId_ReturnsBook(){
        Book book = new Book(1, "Test Book", "Author", 100, LocalDate.now());
        when(bookRepository.findBookById(book.getId())).thenReturn(Optional.of(book));

       Book returnedBook =  bookService.findBookById(book.getId());

       assertThat(returnedBook)
               .isNotNull()
               .isEqualTo(book);

       verify(bookRepository).findBookById(book.getId());
    }

    @Test
    void findById_WhenGivenNonExistingBookId_ThrowsExceptionWithMessage(){
        Book book = new Book(1, "Test Book", "Author", 100, LocalDate.now());
        when(bookRepository.findBookById(book.getId()))
                .thenThrow(new BookNotFoundException("Book with id"+book.getId()+"not found"));

        assertThatThrownBy(()-> bookService.findBookById(book.getId()))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("Book with id"+book.getId()+"not found");

        verify(bookRepository).findBookById(book.getId());
    }

    @Test
    void findById_WhenRepositoryFailsDuringFind_ThrowsException(){
        Book book = new Book(1, "Test Book", "Author", 100, LocalDate.now());
        when(bookRepository.findBookById(book.getId())).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThatThrownBy(() -> bookService.findBookById(book.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Database error");

        verify(bookRepository).findBookById(book.getId());
    }

    @Test
    void applyDiscount_WhenValidDiscount_ApplyDiscountToRecentBooks(){
        LocalDate recentDate = LocalDate.now().minusDays(3);

        Book recentBook = new Book(1, "Test Book", "Author", 100, recentDate);

        when(bookRepository.findByPublishedDateAfter(any()))
                .thenReturn(List.of(recentBook));

        when(bookRepository.saveAndReturn(any(Book.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        List<Book> discountedBooks = bookService.applyDiscount(10);

        assertThat(discountedBooks)
                .hasSize(1)
                .extracting(Book::getPrice)
                .containsExactly(90);

        verify(bookRepository).findByPublishedDateAfter(any());
        verify(bookRepository).saveAndReturn(recentBook);
    }

    @Test
    void applyDiscount_WhenInvalidDiscountIsNegative_ThrowException(){

        assertThatThrownBy(()->bookService.applyDiscount(-5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Discount must be between 0% and 100%");
    }

    @Test
    void applyDiscount_WhenInvalidDiscountIsOver100_ThrowException(){
        assertThatThrownBy(()-> bookService.applyDiscount(101))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Discount must be between 0% and 100%");
    }

}