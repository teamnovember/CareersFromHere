package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.dummy_page;

public class Dummy extends Controller {

    public static Result dummy_page() {
        return ok(dummy_page.render());
    }

}
