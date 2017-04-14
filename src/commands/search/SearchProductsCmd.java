package commands.search;

import org.json.*;

import java.sql.Connection;
import java.util.Map;

import commands.Command;

class SearchProductsCmd extends Command implements Runnable {

    public StringBuffer execute(Connection _connection,  Map<String, Object> mapUserData ) throws Exception {

        StringBuffer strbufResult;
        String       strSearchQuery;

        strSearchQuery = (String)mapUserData.get( "searchQuery" );

        if( strSearchQuery == null || strSearchQuery.trim( ).length( ) == 0 )
            return null;

        String allProducts = fetchAllProducts();

        JSONObject productsJSONObject = new JSONObject(allProducts);
        JSONArray productsJSONArray = productsJSONObject.getJSONArray("products");

        JSONArray result = search( strSearchQuery, productsJSONArray );

        strbufResult = makeJSONResponseEnvelope( 200, null, new StringBuffer("\"products\": "+ result) );

        return strbufResult;
    }

    protected JSONArray search( String query, JSONArray products) {
        JSONArray result = new JSONArray();

        for ( int i = 0; i < products.length(); i++ ) {
            JSONObject product = products.getJSONObject(i);

            if ( product.getString("name").toLowerCase().
                    contains(query.toLowerCase()) ) {
                result.put(product);
            }
        }

        return result;
    }

    protected String fetchAllProducts( ) {
        String products = new StringBuilder()
            .append("{ \"products\": ")
            .append("[")
            .append("{\"id\": 3, \"name\": \"Milk Cartoon\", \"description\": \"Skimmed milk catoon\", \"price\": 10.99, \"stock\": 300}")
            .append(",")
            .append("{\"id\": 14, \"name\": \"Kenwood coffee maker\", \"description\": \"Coffee maker\", \"price\": 75.99, \"stock\": 5000}")
            .append(",")
            .append("{\"id\": 30, \"name\": \"Samsung LED TV\", \"description\": \"Smasung TV\", \"price\": 1230.99, \"stock\": 100}")
            .append(",")
            .append("{\"id\": 57, \"name\": \"Milk Cartoon Full Cream\", \"description\": \"Full Cream milk catoon\", \"price\": 8.99, \"stock\": 200}")
            .append("]")
            .append("}")
            .toString();

        return products;
    }
}
