//package com.testing.junitmockitotesting.UnitTests.Fake;
//
//import com.testing.junitmockitotesting.UnitTests.Fake.Entities.Book;
//import com.testing.junitmockitotesting.UnitTests.Fake.Repository.BookRepository;
//import com.testing.junitmockitotesting.UnitTests.Fake.Services.BookService;
//import com.testing.junitmockitotesting.UnitTests.Fake.Services.EmailService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class FakeTest {
//
//    //System Under Test (SUT)
//    private BookService bookService;
//    private BookRepository fakeBookRepository;
//
//   @Mock
//   private EmailService emailService;
//   @Mock
//   private BookRepository bookRepository;
//
//    @BeforeEach
//    public void setUp(){
//        fakeBookRepository = new FakeBookRepository();
//        emailService = new EmailService();
//        bookService = new BookService(fakeBookRepository,emailService );
//        bookRepository = mock(BookRepository.class);
//    }
//
//    /**
//     * !TESTING WITHOUT MOCKITO */
//    @Test
//    public void findBookById_ShouldReturnBookWhenExistsElseNull(){
//        //ARRANGE
//        Book expectedBook = new Book(1, "Mocking Birds", "Denis", 2000, LocalDate.now());
//        fakeBookRepository.addBook(expectedBook);
//        // ACT
//        Book returnedBook = fakeBookRepository.findBookById(1);
//        Book bookNotFound = fakeBookRepository.findBookById(999);
//        //ASSERT
//        assertNotNull(returnedBook, "Book should be found");
//        assertEquals(expectedBook.getId(), returnedBook.getId());
//        assertEquals(expectedBook.getTitle(), returnedBook.getTitle());
//        assertNull(bookNotFound,"Should return null hen book does not exist");
//    }
//
//    @Test void findAllBooks_ShouldReturnAllBooksWhenExists() {
//        //ARRANGE
//        Book expectedBook = new Book(1, "Mocking Birds", "Denis", 2000, LocalDate.now());
//        fakeBookRepository.addBook(expectedBook);
//        // ACT
//        List<Book> returnedBooks = fakeBookRepository.findAllBooks();
//        //ASSERT
//        assertEquals(1, returnedBooks.size());
//        assertIterableEquals(List.of(expectedBook), returnedBooks);
//        assertNotNull(returnedBooks, "Should not return null when books exist");
//        assertTrue(returnedBooks.contains(expectedBook), "should return book");
//    }
//    @Test void findAllBooks_ShouldReturnEmptyListWhenNoBooksExist(){
//        //ACT
//        List<Book> expectNullList = fakeBookRepository.findAllBooks();
//        //ASSERT
//        assertTrue(expectNullList.isEmpty());
//    }
//
//    @Test void addBook_ShouldAddProvidedBook() {
//        //ARRANGE
//        Book expectedBook = new Book(1, "Mocking Birds", "Denis", 2000, LocalDate.now());
//        //ACT
//        fakeBookRepository.addBook(expectedBook);
//        Book returnedBook = fakeBookRepository.findBookById(1);
//        //ASSERT
//        assertEquals(expectedBook, returnedBook);
//        assertSame(expectedBook, returnedBook);
//    }
//
//    /**
//     * !TESTING WITH MOCKITO */
//    @Test void addBook_ShouldAddProvidedBookWithMockito(){
////        bookService = new BookService(bookRepository,emailService);
////        Book expectedBook = new Book(1, "Mocking Birds", "Denis", 2000, LocalDate.now());
////
////        bookService.addBook(expectedBook);
////        List<Book> books = new ArrayList<>();
////        books.add(expectedBook);
////
////        when(bookRepository.addBook(expectedBook)).thenReturn("")
////
////        int idOfReturnedBook = bookService.findBookById(1).getId();
////
////        assertEquals(expectedBook.getId(), idOfReturnedBook);
//
//    }
//
//    @Test void findAllBooks_ShouldReturnBooksWhenExist(){
//        bookService = new BookService(bookRepository, emailService);
//        Book expectedBook1 = new Book(1, "Mocking Birds", "Denis", 2000, LocalDate.now());
//        //Book expectedBook2 = new Book(1, "Mocking Birds", "Denis", 2000, LocalDate.now());
//        when(bookRepository.findAllBooks()).thenReturn( List.of(expectedBook1));
//
//        List<Book> mockBooks = bookService.findAllBooks();
//
//        //ASSERT
//        assertNotNull(mockBooks);
//        assertEquals(1, mockBooks.size());
//
//        assertEquals(expectedBook1.getId(), mockBooks.get(0).getId());
//        assertEquals(expectedBook1.getTitle(), mockBooks.get(0).getTitle());
//
//        verify(bookRepository).findAllBooks();
//        //verifyNoInteractions(emailService);
//
//
//
//    }
////    @Test void findAllBooks_ShouldReturnEmptyListWhenNoBooksExist(){
////
////    }
//
//}
