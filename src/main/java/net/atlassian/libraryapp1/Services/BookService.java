package net.atlassian.libraryapp1.Services;

import net.atlassian.libraryapp1.Exceptions.*;
import net.atlassian.libraryapp1.Model.Book;
import net.atlassian.libraryapp1.Model.BooksOfLibrary;
import net.atlassian.libraryapp1.Model.User;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;

import java.util.Objects;

import static net.atlassian.libraryapp1.Services.FileSystemService.getPathToFile;

public class BookService {

    public static ObjectRepository<Book> bookRepository;
    public static ObjectRepository<User> userRepository;

    public static void initDatabase() {
        Nitrite database = Nitrite.builder()
                .filePath(getPathToFile("BooksDataBase.db").toFile())
                .openOrCreate("test", "test");

        bookRepository = database.getRepository(Book.class);
    }

    public static void addBook(String title, String author, String genre, String libraryName) throws EmptyTitleFieldException, EmptyAuthorFieldException, EmptyGenreFieldException, EmptyLibraryNameFieldException {
        checkEmptyFields(title, author, genre, libraryName);
        String borrowedDate = "";
        String returnedDate = "";
        String username = "";
        bookRepository.insert(new Book(title, author, genre, libraryName, borrowedDate, returnedDate, username));
    }

    private static void checkEmptyFields(String title, String author, String genre, String libraryName) throws EmptyTitleFieldException, EmptyAuthorFieldException, EmptyGenreFieldException, EmptyLibraryNameFieldException {
        if (title == "") {
            throw new EmptyTitleFieldException();
        } else {
            if (author == "") {
                throw new EmptyAuthorFieldException();
            } else {
                if (genre == "") {
                    throw new EmptyGenreFieldException();
                } else {
                    if (libraryName == "") {
                        throw new EmptyLibraryNameFieldException();
                    }
                }
            }
        }
    }

    public static void checkLibrary(String libraryName) throws LibraryDoesNotExistException, BooksDoesNotExistInLibrary {

        checkLibraryExist(libraryName);
        checkBooksExistInLibrary(libraryName);

    }

    private static void checkBooksExistInLibrary(String libraryName) throws BooksDoesNotExistInLibrary {

        int sw = 0;
        for (Book book : bookRepository.find()) {
            if (Objects.equals(libraryName, book.getLibraryName()) && Objects.equals(book.getUserName(), "")) {
                sw = 1;
            }
        }
        if (sw == 0) {
            throw new BooksDoesNotExistInLibrary();
        }
    }

    private static void checkLibraryExist(String libraryName) throws LibraryDoesNotExistException {

        int sw = 0;
        for (User user : UserService.userRepository.find()) {
            if (Objects.equals(libraryName, user.getName())) {
                sw = 1;
            }
        }
        if (sw == 0) {
            throw new LibraryDoesNotExistException();
        }
    }

}
