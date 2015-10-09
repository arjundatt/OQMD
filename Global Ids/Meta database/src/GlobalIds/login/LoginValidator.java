package DIC.login;


/**
 * Created with IntelliJ IDEA.
 * User: Globalids
 * Date: 8/7/13
 * Time: 4:44 PM
 */
public class LoginValidator {

    static final String uId = "Arnab";
    static final String pass = "Arnab";

    public static boolean validate(String id, String password) {
        if (id.equals(uId) && password.equals(pass))
            return true;
        return false;
    }
}
