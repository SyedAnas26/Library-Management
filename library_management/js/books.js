$(document).ready(function () {
    addRowstoTable();
});

function addRowstoTable() {

    var data = [{ "bookId": 1, "bookName": "She: A History of Adventure", "author": "H. Rider Haggard", "availability": "Available" },
    { "bookId": 2, "bookName": "The Lion, The Witch, and the Wardrobe", "author": "C. S. Lewis", "availability": "Available" },
    { "bookId": 3, "bookName": "Alice's Adventures in Wonderland", "author": "Lewis Carroll", "availability": "Not Available" },
    { "bookId": 4, "bookName": "The Hobbit", "author": "J. R. R. Tolkien", "availability": "Available" },
    { "bookId": 5, "bookName": "And Then There Were None", "author": "Agatha Christie", "availability": "Not Available" },
    { "bookId": 6, "bookName": "The Little Prince", "author": "Antoine de Saint-Exupéry", "availability": "Available" }]

    $.each(data, function (index, value) {
        var row = '<tr>' +
            '<td>' + value.bookId + '</td>' +
            '<td>' + value.bookName + '</td>' +
            '<td>' + value.author + '</td>' +
            '<td>' + value.availability + '</td>' +
            '</tr>';
        $('table').append(row);
    });
} 