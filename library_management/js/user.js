$(document).ready(function () {
    addRowstoTable();
});

function addRowstoTable() {

    var data = [{ "userId": 1, "userName": "Syed Saddique", "booksCount": "5" },
    { "userId": 2, "userName": "Ranjith.R", "booksCount": "3" },
    { "userId": 3, "userName": "Yuvan Kumar", "booksCount": "4" },
    { "userId": 4, "userName": "Vasanth", "booksCount": "10" },
    { "userId": 5, "userName": "Vicky", "booksCount": "6" },
    { "userId": 6, "userName": "Rahul", "booksCount": "2" },
    { "userId": 7, "userName": "Jegan", "booksCount": "9" }];

    $.each(data, function (index, value) {
        var row = '<tr>' +
            '<td>' + value.userId + '</td>' +
            '<td>' + value.userName + '</td>' +
            '<td>' + value.booksCount + '</td>' +
            '</tr>';
        $('table').append(row);
    });
} 