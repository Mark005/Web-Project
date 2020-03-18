package util;

import com.nncompany.api.model.User;

public class UserKeeper {

    private static final ThreadLocal<User> threadLocalScope = new  ThreadLocal<>();

    public final static User getLoggedUser() {
        return threadLocalScope.get();
    }

    public final static void setLoggedUser(User user) {
        threadLocalScope.set(user);
    }
}
