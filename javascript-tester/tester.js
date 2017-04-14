
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
	request.post('http://127.0.0.1:8080/',
				{ form: { SrvReq: strJSON } },
				function(err,httpResponse,body){callbackfunc(err,httpResponse,body)}
	);
}

function sendRequest_win8( strJSON, callbackfunc ){

	unirest.post('http://127.0.0.1:8080/')
	.header('Accept', 'application/json')
	.send({ "SrvReq": strJSON })
	.end(function (response) {
		console.log(response.body);
	});
}

function sendRequest( strJSON, callbackfunc ){
	console.log( "trying to send " + strJSON );
	sendRequest_win8(strJSON, callbackfunc);
}

function addUser( email, password ){
    var gsRequest           = new Object( );
    gsRequest.action        = "addUserSimple";
    var gsRequestData       = new Object( );
    gsRequestData.email     = email;
    gsRequestData.password  = password;
    gsRequestData.firstName = "moh";
    gsRequestData.lastName  = "ali";
    gsRequest.data          = gsRequestData;
    var strJSON = JSON.stringify(gsRequest);
    sendRequest( strJSON, addUserResponse );
}

function addUserResponse( err,httpResponse,body ){
    console.log( body );
}

function attemptLogin( email, password ){
	var gsRequest           = 	new Object( );
    gsRequest.action        = 	"attemptLogin";
    var gsRequestData       = 	new Object( );
    gsRequestData.email	    = 	email;
    gsRequestData.password	= 	password;
    gsRequest.data          = 	gsRequestData;
    var strJSON = JSON.stringify(gsRequest);
    sendRequest( strJSON, attemptLoginResponse );
}

function attemptLoginResponse( err,httpResponse,body ){
    console.log( body );
}

function addProduct( name, description, price, stock ){
    var gsRequest             = new Object( );
    gsRequest.action          = "addProduct";
    var gsRequestData         = new Object( );
    gsRequestData.name        = name;
    gsRequestData.description = description;
    gsRequestData.price       = price;
    gsRequestData.stock       = stock;
    gsRequest.data            = gsRequestData;
    var strJSON               = JSON.stringify(gsRequest);
    sendRequest( strJSON, addProductResponse );
}

function addProductResponse( err, httpResponse, body ){
    console.log( body );
}

function updateStock( productID, stock ){
    var gsRequest             = new Object( );
    gsRequest.action          = "updateStock";
    var gsRequestData         = new Object( );
    gsRequestData.PID         = productID;
    gsRequestData.stock       = stock;
    gsRequest.data            = gsRequestData;
    var strJSON               = JSON.stringify(gsRequest);
    sendRequest( strJSON, updateStockResponse );
}

function updateStockResponse( err, httpResponse, body ){
    console.log( body );
}

function editInfo( email, password, newpassword, firstName, lastName, picturepath, gender ){
    var gsRequest             = new Object( );
    gsRequest.action          = "editInfo";
    var gsRequestData         = new Object( );
    gsRequestData.email       = email;
    gsRequestData.password    = password;
    gsRequestData.newpassword = newpassword;
    gsRequestData.firstName   = firstName;
    gsRequestData.lastName    = lastName;
    gsRequestData.picturepath = picturepath;
    gsRequestData.gender      = gender;
    gsRequest.data            = gsRequestData;
    var strJSON               = JSON.stringify(gsRequest);
    sendRequest( strJSON, editInfoResponse );
}

function editInfoResponse( err, httpResponse, body ){
    console.log( body );
}


// To start sending messages:
// addUser("mohamed@m.com","johnpass");
// attemptLogin("mohamed@m.com","johnpass");
// addProduct("Milk Cartoon","White milk", 10.99, 300);
// updateStock(2, 10);
editInfo("super@example.com", "123456789", "12345@hello", "John", "Doe", "", "male");
