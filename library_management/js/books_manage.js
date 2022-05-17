$(document).ready(function () {

    $('[data-toggle="tooltip"]').tooltip();
    var actions =
        '<a class="add" title="Add" data-toggle="tooltip"><i class="material-icons"></i></a>' +
        '<a class="edit" title="Edit" data-toggle="tooltip"><i class="material-icons"></i></a>' +
        '<a class="delete" title="Delete" data-toggle="tooltip"><i class="material-icons"></i></a>';


    addRowstoTable(actions);

    $(".add-new").click(function () {
        $(this).attr("disabled", "disabled");
        var index = $("table tbody tr:last-child").index();
        var row = '<tr>' +
            '<td><input type="text" class="form-control" name="name" id="name"></td>' +
            '<td><input type="text" class="form-control" name="department" id="department"></td>' +
            '<td><input type="text" class="form-control" name="phone" id="phone"></td>' + '<td><input type="text" class="form-control" name="phone" id="phone"></td>' + '<td><input type="text" class="form-control" name="phone" id="phone"></td>' +
            '<td>' + actions + '</td>' +
            '</tr>';
        $("table").append(row);
        $("table tbody tr").eq(index + 1).find(".add, .edit").toggle();
        $('[data-toggle="tooltip"]').tooltip();
    });

    $(document).on("click", ".add", function () {
        var empty = false;
        var input = $(this).parents("tr").find('input[type="text"]');
        input.each(function () {
            if (!$(this).val()) {
                $(this).addClass("error");
                empty = true;
            } else {
                $(this).removeClass("error");
            }
        });
        $(this).parents("tr").find(".error").first().focus();
        if (!empty) {
            input.each(function () {
                $(this).parent("td").html($(this).val());
            });
            $(this).parents("tr").find(".add, .edit").toggle();
            $(".add-new").removeAttr("disabled");
        }
    });

    $(document).on("click", ".edit", function () {
        $(this).parents("tr").find("td:not(:last-child)").each(function () {
            $(this).html('<input type="text" class="form-control" value="' + $(this).text() + '">');
        });
        $(this).parents("tr").find(".add, .edit").toggle();
        $(".add-new").attr("disabled", "disabled");
    });

    $(document).on("click", ".delete", function () {
        $(this).parents("tr").remove();
        $(".add-new").removeAttr("disabled");
    });
});

function addRowstoTable($actions) {
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
            '<td>' + $actions + '</td>'
        '</tr>';
        $('table').append(row);
    });
} 