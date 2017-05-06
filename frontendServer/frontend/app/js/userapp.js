function redirect(data) {
  if (typeof data.redirect == 'string') {
    window.location = data.redirect
    console.log("hello there");
  }
  console.log(data.redirect);
}

$('#login').submit(function (e) {
  e.preventDefault();
  console.log("hello world");
  let email = $('#inputEmail').val();
  let pass = $('#inputPassword').val();
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

$('#signup').submit(function (e) {
  e.preventDefault();
  console.log("hello world");
  let email = $('#signupInputEmail').val();
  let pass = $('#signupInputPassword').val();
  let firstName = $('#signupFirstName').val();
  let lastName = $('#signupLastName').val();

  let req = {
    url: '/register',
    type: 'POST',
    data: {
      email: email,
      password: pass,
      firstname: firstName,
      lastname: lastName
    },
    success: function (response) {
      redirect(response);
    }
  };
  $.ajax(req);
});
