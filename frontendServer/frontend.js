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
app.use(cookieParser("sdd", {signed: true}));
app.use(expressSession({secret:'somesecrettokenhere',
	resave: false,
	saveUninitialized: true}));

app.use(function(req, res, next) {
	res.header("Access-Control-Allow-Origin", "*");
	res.header('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE');
	res.header('Access-Control-Allow-Headers', 'Content-Type');
	next();
});

app.listen(3000, function () {
	console.log('FrontEnd app listening on port 3000!')
})

app.get('/', function (req, res) {
  // if user not logged in (redirect to login)

  if(!req.session.sessionId){
  	res.redirect('/login');
  }else{
  	res.sendFile(path.join(__dirname + '/frontend/pages/index.html'));
  }
})

app.get('/login', function (req, res) {
  // if user has a valid session id then redirect to home
  if(req.session.sessionId){
  	res.redirect('/');
  }else{
  	res.sendFile(path.join(__dirname + '/frontend/pages/login-multi.html'));
  }
})

app.post('/login', function(req, res){
  // login user
  let data = JSON.stringify(
  {
  	"action": "attemptLogin",
  	"data": {
  		"email": req.body.email,
  		"password": req.body.password
  	}
  }
  );
  // sendPostRequest(data,
  //   function(result){
  //     let jsonObject = JSON.parse(result);
  //     if(jsonObject.response_status == 404){
  //       res.send({redirect: '/login'}); // send errors lw 7abeb (user not found)
  //     }else{
  //       req.session.sessionId = jsonObject.sessionId;
  //       req.session.userId = jsonObject.userId;
  //       res.send({redirect: '/'});
  //     }
  // });

  console.log(data);

  amqp.connect('amqp://localhost:5673', function(err, conn) {
  	conn.createChannel(function(err, ch) {
  		var q = 'userApp';
    // var msg = 'Hello World!';

    ch.assertQueue(q, {durable: false});
    // Note: on Node 6 Buffer.from(msg) should be used
    ch.sendToQueue(q, new Buffer(data));
    console.log(" [x] Sent %s", data);
});
  	setTimeout(function() { conn.close(); process.exit(0) }, 500);
  });
})


function sendPostRequest (data, onSuccess, onError){
	var options = {
    hostname: '127.0.0.1', // load balancer
    port: 5673,
    path: '/java',
    method: 'POST',
    headers: {
    	'Content-Type': 'application/json'
    }
};
var req = http.request(options, function(res) {
	res.setEncoding('utf8');
	res.on('data', function (body) {
		onSuccess(body);
	});
});
req.on('error', function(e) {
	onError('problem with request: ' + e.message);
});
  // write data to request body
  amqp.connect('amqp://localhost:5673', function(err, conn) {
  	conn.createChannel(function(err, ch) {
  		var q = 'userapp';
    // var msg = 'Hello World!';

    ch.assertQueue(q, {durable: false});
    // Note: on Node 6 Buffer.from(msg) should be used
    ch.sendToQueue(q, new Buffer(msg));
    console.log(" [x] Sent %s", msg);
});
  	setTimeout(function() { conn.close(); process.exit(0) }, 500);
  });
  req.write(data);
  req.end();
}
