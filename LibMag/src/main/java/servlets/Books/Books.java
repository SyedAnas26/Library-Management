package servlets.Books;

public class Books {
    private int bookId;
    private String bookName;
    private String author;
    private int copies;
    private int totalCopies;
    private float price;
    private int publishedYear;

    public Books(int bookId, String bookName, String author, int copies, float price) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.author = author;
        this.copies = copies;
        this.price = price;
    }

    public Books() {
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }

}
