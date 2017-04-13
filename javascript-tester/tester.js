
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

function sendRequest( strJSON, callbackfunc ){
	console.log( "trying to send " + strJSON );
	sendRequest_win8(strJSON, callbackfunc);
}

function addUser( email, password, firstname, lastname ){
    var gsRequest           = new Object( );
    gsRequest.action        = "addUser";
    var gsRequestData       = new Object( );
    gsRequestData.email     = email;
    gsRequestData.password  = password;
    gsRequestData.firstname  = firstname;
    gsRequestData.lastname  = lastname;
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

// To start sending messages:
//addUser("mohamed@m.com","johnpass", "john", "7amada");
attemptLogin("mohamed@m.com","johnpass");



      

                  
