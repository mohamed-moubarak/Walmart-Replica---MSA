var amqp = require('amqplib/callback_api');
var express = require('express')
var app = express()
var path = require('path')
var bodyParser = require('body-parser')
var cookieParser = require('cookie-parser');
var expressSession = require('express-session');
var http = require("http");

app.use(express.static(__dirname + '/frontend'))

app.use(bodyParser.urlencoded({ extended: true }))
app.use(bodyParser.json())
app.use(cookieParser("sdd", { signed: true }));
app.use(expressSession({
  secret: 'somesecrettokenhere',
  resave: false,
  saveUninitialized: true
}));

app.use(function (req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE');
  res.header('Access-Control-Allow-Headers', 'Content-Type');
  next();
});

app.listen(3000, function () {
  console.log('FrontEnd app listening on port 3000!')
})

var publish = function (queueName, data) {
  amqp.connect('amqp://localhost:5673', function (err, conn) {
    conn.createChannel(function (err, ch) {
      ch.assertQueue(queueName, { durable: false });
      ch.sendToQueue(queueName, new Buffer(data));
      console.log(" [x] Sent %s", data);
    });
    // setTimeout(function () { conn.close(); process.exit(0) }, 500);
  });
}

// Handle GET REQUEST localhost/
app.get('/', function (req, res) {
  // if user not logged in (redirect to login)
  if (!req.session.sessionId) {
    res.redirect('/login');
    console.log("hala");
  } else {
    res.sendFile(path.join(__dirname + '/frontend/pages/landing.html'));
    console.log("halalal");
  }
})

// Handle GET REQUEST localhost/login
app.get('/login', function (req, res) {
  // if user has a valid session id then redirect to home
  if (req.session.sessionId) {
    res.redirect('/');
  } else {
    res.sendFile(path.join(__dirname + '/frontend/pages/login-multi.html'));
  }
})

// Handle POST REQUEST localhost/login
app.post('/login', function (req, res) {

  var randomID = Math.floor((Math.random() * 1000000) + 1);

  // login user
  let data = JSON.stringify(
    {
      "randomID": randomID,
      "action": "attemptLogin",
      "data": {
        "email": req.body.email,
        "password": req.body.password
      }
    }
  );

  publish("userApp", data);

  amqp.connect('amqp://localhost:5673', function (err, conn) {
    conn.createChannel(function (err, ch) {
      var q = 'userAppResponse';
      ch.assertQueue(q, { durable: false });
      console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", q);
      ch.consume(q, function (msg) {
        jsonObject = JSON.parse(msg.content.toString());
        console.log(jsonObject);
        if (jsonObject.randomID == randomID) {
          console.log("match!!");
          if (jsonObject.object.StatusID == "0") {
            console.log("read session");
            req.session.sessionId = jsonObject.object.responseData.sessionID;
            res.send({ redirect: '/' });
          }
          else {
            console.log("session");
            res.send({ redirect: '/login' });
          }
          conn.close();
        } else {
          console.log("no");
          publish(q, msg);
        }
      }, { noAck: true });
    });
  });
})

// Handle POST REQUEST localhost/register
app.post('/register', function (req, res) {

  var randomID = Math.floor((Math.random() * 1000000) + 1);

  // register user
  let data = JSON.stringify(
    {
      "randomID": randomID,
      "action": "addUser",
      "data": {
        "email": req.body.email,
        "password": req.body.password,
        "firstname": req.body.firstname,
        "lastname": req.body.lastname
      }
    }
  );

  // Publish to userApp queue
  amqp.connect('amqp://localhost:5673', function (err, conn) {
    conn.createChannel(function (err, ch) {
      var q = 'userApp';
      ch.assertQueue(q, { durable: false });
      ch.sendToQueue(q, new Buffer(data));
    });
    setTimeout(function () { conn.close(); process.exit(0) }, 500);
  });

  var receivedMessage;
  while (receivedMessage == null) {
    receivedMessage = inBoundMessages[randomID];
  }

  receivedJsonObject = JSON.parse(receivedMessage);

  if (receivedJsonObject.status == "200") {
    req.session.sessionId = receivedMessage.sessionId;
    res.send({ redirect: '/' });
  }
  else {
    res.send({ redirect: '/login' });
  }
})