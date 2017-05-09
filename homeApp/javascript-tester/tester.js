var amqp = require('amqplib/callback_api');
// run using node.js pick one of the below libraries
//var request = require('request');
var unirest = require('unirest');
//var http    =  require('http');

function handleError( jqXHR, textStatus, errorThrown ){
    console.log("Connection With Server Failed", textStatus + ' ' +  errorThrown);
}

function stripNewLineCharacter( strText ){
	strText = strText.replace(/(?:\r\n|\r|\n)/g, '<br />');
	return strText;
}


function sendRequest_vista( strJSON, callbackfunc ){

    request.cookie("");
    request.post('http://localhost:8080/',
        { form: { SrvReq: strJSON } },
        function(err,httpResponse,body){callbackfunc(err,httpResponse,body)}
        );
}

function sendRequest_win8( strJSON, callbackfunc ){

	unirest.post('http://localhost:8080/')
	.header('Accept', 'application/json')
	.header('Connection', 'Keep-Alive')
	.send({ "SrvReq": strJSON })
	.end(function (response) {
		console.log(response);
	});
}

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


function sendRequest( strJSON, callbackfunc ){
	console.log( "trying to send " + strJSON );
	sendRequest_win8(strJSON, callbackfunc);
}


function getPromotions(userID){
    var gsRequest           = new Object( );
    gsRequest.action        = "getpromotions";
    var gsRequestData       = new Object( );
    gsRequestData.userID     = userID;
    gsRequest.data          = gsRequestData;
    var strJSON = JSON.stringify(gsRequest);
    sendRequest( strJSON, getPromotionsResponse );
}

function getPromotionsResponse( err,httpResponse,body ){
    console.log( body );
}

function getPromotionsrabbit(userID){
    var gsRequest           = new Object( );
    gsRequest.action        = "getpromotions";
    var gsRequestData       = new Object( );
    gsRequestData.userID     = userID;
    gsRequest.data          = gsRequestData;
    var strJSON = JSON.stringify(gsRequest);
    publish("homeApp", strJSON);
    amqp.connect('amqp://localhost:5673', function (err, conn) {
      conn.createChannel(function (err, ch) {
        var q = 'homeAppResponse';
        ch.assertQueue(q, { durable: false });
        console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", q);
        ch.consume(q, function (msg) {
          jsonObject = JSON.parse(msg.content.toString());
          console.log(jsonObject);

        }, { noAck: true });
      });
    });
    // sendRequest( strJSON, getPromotionsResponse );
}

function getPromotionsResponserabbit( err,httpResponse,body ){
    console.log( body );
}

function getAllProducts(userID){
    var gsRequest           = new Object( );
    gsRequest.action        = "getallproducts";
    var gsRequestData       = new Object( );
    gsRequestData.userID     = userID;
    gsRequest.data          = gsRequestData;
    var strJSON = JSON.stringify(gsRequest);
    sendRequest( strJSON, getAllProductsResponse );
}

function getAllProductsResponse( err,httpResponse,body ){
    console.log( body );
}

function getAllProductsrabbit(userID){
    var gsRequest           = new Object( );
    gsRequest.action        = "getallproducts";
    var gsRequestData       = new Object( );
    gsRequestData.userID     = userID;
    gsRequest.data          = gsRequestData;
    var strJSON = JSON.stringify(gsRequest);
    publish("homeApp", strJSON);
    amqp.connect('amqp://localhost:5673', function (err, conn) {
      conn.createChannel(function (err, ch) {
        var q = 'homeAppResponse';
        ch.assertQueue(q, { durable: false });
        console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", q);
        ch.consume(q, function (msg) {
          jsonObject = JSON.parse(msg.content.toString());
          console.log(jsonObject);

        }, { noAck: true });
      });
    });
    // sendRequest( strJSON, getAllProductsResponse );
}

function getAllProductsResponserabbit( err,httpResponse,body ){
    console.log( body );
}

getPromotions("1");
// getAllProducts("1");


// getPromotionsrabbit("1");
// getAllProductsrabbit("1");
