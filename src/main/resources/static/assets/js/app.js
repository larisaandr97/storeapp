jQuery(function ($) {


    let inputs = $('input[name="quantity"]');

    $('.quantity').on('input', function () {

        let index = inputs.index(this);

        let row = $('tr[id^=' + this.id + ']');

        let price = row.find("td:nth-child(4)").text();

        let quantity = $(this).val();
        let modified = row.find("td:nth-child(5)").text(price * quantity);

        // Compute total sum
        let sum = 0.0;
        $('#items tbody tr').each(function (index, tr) {
            sum += parseFloat($(this).find("td:nth-child(5)").text());
        });
        $('#totalAmount').text("Total amount: " + sum);
    });

    let list = ['one', 'two', 'three', 'four', 'five'];
    list.forEach(function (element, index) {
        document.getElementById(element).addEventListener("click", function () {
            let cls = document.getElementById(element).className;
            if (cls.includes("bi-star")) {
                for (let i = 0; i <= index; i++) {
                    document.getElementById(list[i]).classList.remove("bi-star");
                    document.getElementById(list[i]).classList.add("bi-star-fill");
                }
                for (let i = index + 1; i <= 4; i++) {
                    document.getElementById(list[i]).classList.remove("bi-star-fill");
                    document.getElementById(list[i]).classList.add("bi-star");
                }
            } else {
                for (let i = 0; i <= index; i++) {
                    document.getElementById(list[i]).classList.remove("bi-star-fill");
                    document.getElementById(list[i]).classList.add("bi-star");
                }
                for (let i = index + 1; i <= 4; i++) {
                    document.getElementById(list[i]).classList.remove("bi-star");
                    document.getElementById(list[i]).classList.add("bi-star-fill");
                }
            }
            $('#rating').val(index + 1);
        });
    });
    let rating = $('#ratingProduct').val();
    let list2 = ['one1', 'two2', 'three3', 'four4', 'five5'];
    for (let i = 0; i < rating; i++) {
        document.getElementById(list2[i]).classList.remove("bi-star");
        document.getElementById(list2[i]).classList.add("bi-star-fill");
    }
    let sameUser = $('#sameUser').val();
    if (sameUser == 'YES') {
        $('#myToast').toast('show');
    }

});


//
// $(document).ready(function () {
//     function getCookie(c_name) {
//         if (document.cookie.length > 0) {
//             c_start = document.cookie.indexOf(c_name + "=");
//             if (c_start != -1) {
//                 c_start = c_start + c_name.length + 1;
//                 c_end = document.cookie.indexOf(";", c_start);
//                 if (c_end == -1) c_end = document.cookie.length;
//                 return unescape(document.cookie.substring(c_start, c_end));
//             }
//         }
//         return "";
//     }
//
//     $(function () {
//         $.ajaxSetup({
//             headers: {
//                 "X-CSRFToken": getCookie("csrftoken")
//             }
//         });
//     });
// });

// function updateCartQuantities() {
//     $("table > tbody > tr").each(function () {
//         let currentRow = $(this); //Do not search the whole HTML tree twice, use a subtree instead
//         // let price = currentRow.find("td:nth-child(4)").text();
//         let productId = currentRow.find("td:nth-child(1)").text();
//         console.log("ID:" + productId);
//         let quantity = $('#' + productId).val();
//         console.log("quantity:" + quantity);
//         $.ajax({
//             url: '/cart/update/productId=' + productId + '&quantity=' + quantity,
//             type: 'POST',
//             success: function (result) {
//             }
//         });
//         // alert(currentRow.find(".FieldNameID").text() + " " + currentRow.fint("#OperatorID").text());
//     });
// }

//$(document).ready(function () {
// $('.toast').toast('show');
// $('#updateCart').addEventListener('click', function () {
//     console.log('HIIIIIIIIIII');
//     $("table > tbody > tr").each(function () {
//         let currentRow = $(this); //Do not search the whole HTML tree twice, use a subtree instead
//         // let price = currentRow.find("td:nth-child(4)").text();
//         let productId = currentRow.find("td:nth-child(1)").text();
//         console.log("ID:" + productId);
//         let quantity = $('#' + productId).val();
//         console.log("quantity:" + quantity);
//         $.ajax({
//             url: '/cart/update/productId=' + productId + '&quantity=' + quantity,
//             type: 'POST',
//             success: function (result) {
//             }
//         });
//         // alert(currentRow.find(".FieldNameID").text() + " " + currentRow.fint("#OperatorID").text());
//     });
// });
//});

// function updateCartQuantities() {
//     $("table > tbody > tr").each(function () {
//         let currentRow = $(this); //Do not search the whole HTML tree twice, use a subtree instead
//         // let price = currentRow.find("td:nth-child(4)").text();
//         let productId = currentRow.find("td:nth-child(1)").text();
//         console.log("ID:" + productId);
//         let quantity = $('#' + productId).val();
//         console.log("quantity:" + quantity);
//         $.ajax({
//             url: '/cart/update/productId=' + productId + '&quantity=' + quantity,
//             type: 'POST',
//             success: function (result) {
//             }
//         });
//         // alert(currentRow.find(".FieldNameID").text() + " " + currentRow.fint("#OperatorID").text());
//     });
// }

/*function deleteReview() {
    let id = $('#reviewId').val();
    $.ajax({
        url: '/reviews/delete/' + id,
        type: 'DELETE',
        success: function (result) {
            alert("Review deleted");
        }
    });
}*/
