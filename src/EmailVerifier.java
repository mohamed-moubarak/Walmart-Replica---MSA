
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class EmailVerifier{

    protected static Pattern _pattern;
    
    protected static String  EMAIL_PATTERN ="^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
    
    public static boolean verify( String strEmail ){
        if( _pattern == null )
            _pattern = java.util.regex.Pattern.compile( EMAIL_PATTERN );
        Matcher matcher = _pattern.matcher( strEmail );
        return matcher.matches( );
    }

}