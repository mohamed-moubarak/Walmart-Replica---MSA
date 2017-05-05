$('#login').submit(function (e){
  e.preventDefault();
  console.log("hello world");
  let email = $('#inputEmail').val();
  let pass =  $('#inputPassword').val();
  let req = {
        url: '/login',
        type: 'POST',
        data: {
          email: email,
          password: pass
        },
        success: function (response) {
          redirect(response);
        }
      };
  $.ajax(req);
});
