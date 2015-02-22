package controllers;

import models.User;
import models.UserDAOImpl;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by Louise on 21/02/2015.
 */
public class AlumniSecured extends Security.Authenticator {

    @Override
    public String getUsername(Http.Context ctx) {
        String email = ctx.session().get("email");
        UserDAOImpl dao = new UserDAOImpl();
        User u = dao.getUserByEmail(email);
        if (u.getDiscriminator().equals("alumni")) {
            return email;
        } else {
            return null;
        }
    }

    //TODO: probably should redirect somewhere else...
    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return redirect("/login");
    }
}
