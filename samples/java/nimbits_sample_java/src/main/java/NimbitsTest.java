import com.nimbits.client.model.user.User;
import com.nimbits.client.model.user.UserModel;
import com.nimbits.io.Nimbits;

import java.util.Date;

/**
 * A base class for running tests
 *
 */

public abstract class NimbitsTest  {
    public User user;

    public static final String EMAIL_ADDRESS ="admin@example.com";
    public static final String INSTANCE_URL = "http://localhost:8080";
    public static final String PASSWORD = "password1234";
    protected static final Nimbits nimbits = new Nimbits.Builder()
            .email(EMAIL_ADDRESS).token(PASSWORD).instance(INSTANCE_URL).create();



    static void o(String msg) {
        System.out.println(new Date() + "  " + msg);
    }


    User verifyAdminUser() {
        //See if my user id and password get me my user
        User user;

        try {

            o("Trying to get existing user info");
            user = nimbits.getMe();
        } catch (Throwable throwable) {
            //user not found, let's create on - the first user will be an admin of the server
            o("Server returned error - creating user instead " + throwable.getMessage());
            user =  createUser(EMAIL_ADDRESS, PASSWORD);

        }

        o("Got User:" + user.toString());

        return nimbits.getMe();



    }

    /**
     * Creates a new user, if this is the first user on the system, it will be the admin
     *
     *
     */
    public User createUser(String email, String password) {

        User postObject = new UserModel(email, password);
        User newUser = nimbits.addUser(postObject);
        if (newUser == null || ! newUser.getEmail().getValue().equals(email)) {
            throw new RuntimeException("Could not create a new user");
        }
        else {
            o("Create new user" + newUser.getEmail());
            return newUser;
        }
    }

    public void execute() throws InterruptedException {
        //Get or Create this user account, when created it will be the system admin
        user = verifyAdminUser();

    }
}