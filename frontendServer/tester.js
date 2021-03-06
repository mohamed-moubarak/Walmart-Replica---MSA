
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

function editInfo( email, password, newPassword, firstname, lastname, picturePath, gender ){
    var gsRequest           = new Object( );
    gsRequest.action        = "editInfo";
    var gsRequestData       = new Object( );
    gsRequestData.email     = email;
    gsRequestData.password  = password;
    gsRequestData.newPassword  = newPassword;
    gsRequestData.firstname  = firstname;
    gsRequestData.lastname  = lastname;
    gsRequestData.picturePath  = picturePath;
    gsRequestData.gender  = gender;
    gsRequest.data          = gsRequestData;
    var strJSON = JSON.stringify(gsRequest);
    sendRequest( strJSON, editInfoResponse );
}

function editInfoResponse( err,httpResponse,body ){
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

function attemptLogout( sessionID ){
    var gsRequest           =   new Object( );
    gsRequest.action        =   "attemptLogout";
    var gsRequestData       =   new Object( );
    gsRequestData.sessionID =   sessionID;
    gsRequest.data          =   gsRequestData;
    var strJSON = JSON.stringify(gsRequest);    
    sendRequest( strJSON, attemptLogoutResponse );
}

function attemptLogoutResponse( err,httpResponse,body ){
    console.log( body );
}

// add users to the database
// addUser("abc@g.com","abcpass", "hesham", "wardany");
// addUser("hany@a.com","hanypass", "ahmad", "hany");
// addUser("bassem@y.com","bassempass", "mohamad", "bassem");
addUser("test@s.net","attwapassword", "test", "test");

// "sessionID":"2bae123f-0034-4b90-99f3-30304272698d" user abc@g.com

// attemptLogin("abc@g.com","abcpass");

// attemptLogout("7b44e59f-be0a-46cc-979f-c45e8bd1e713");

// attemptLogin("hany@a.com","hanypass");
// attemptLogin("bassem@y.com","bassempass");
// // attemptLogin("attwa@s.com","attwapass");

// editInfo("heshamww@g.com","heshampass", "heshamnewpass", "hesham", "el-wardany", "dummy/path/", "MALE");




