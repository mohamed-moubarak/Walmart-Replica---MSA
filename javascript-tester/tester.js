var unirest = require('unirest');

function handleError( jqXHR, textStatus, errorThrown ) {
  console.log("Connection With Server Failed", textStatus + ' ' +  errorThrown);
}

function stripNewLineCharacter( strText ) {
  strText = strText.replace(/(?:\r\n|\r|\n)/g, '<br />');
  return strText;
}

function sendRequest( strJSON, callbackfunc ) {
  console.log( "trying to send " + strJSON );
  unirest.post('http://127.0.0.1:8080/')
    .header('Accept', 'application/json')
    .send({ "SrvReq": strJSON })
    .end(function (response) {
      console.log(response.body);
    });
}

function searchProducts( searchQuery ) {
  var gsRequest             = new Object( );
  gsRequest.action          = "searchProducts";
  var gsRequestData         = new Object( );
  gsRequestData.searchQuery = searchQuery;
  gsRequest.data            = gsRequestData;
  var strJSON               = JSON.stringify(gsRequest);
  sendRequest( strJSON, searchProductsResponse );
}

function searchProductsResponse( err, httpResponse, body ) {
  console.log( body );
}

searchProducts("Milk Cartoon");
