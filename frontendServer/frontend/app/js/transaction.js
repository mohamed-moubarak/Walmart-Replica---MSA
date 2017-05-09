$('#button1').click(function (e) {
  console.log("client side");
  e.preventDefault();
  let itemID = $('#button1').attr("name");
  let req = {
    url: '/transaction',
    type: 'POST',
    data: {
      itemID: itemID
    },
    success: function (response) {
      redirect(response);
    }
  };
  $.ajax(req);
  console.log("client side sent");
});
$('#button2').click(function (e) {
  e.preventDefault();
  let req = {
    url: '/buy',
    type: 'POST',
    data: {
    },
    success: function (response) {
      redirect(response);
    }
  };
  $.ajax(req);
});