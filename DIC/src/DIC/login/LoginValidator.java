package DIC.login;


/**
 * Created by Arnab Saha on 10/11/15.
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
