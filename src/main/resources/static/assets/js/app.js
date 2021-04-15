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

});