package controllers;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.dummy_page;

public class Dummy extends Controller {

    public static Result dummy_page() {
        return ok(dummy_page.render());
    }

    public static Result upload() {
        DynamicForm requestData = Form.form().bindFromRequest();
        return badRequest(requestData.get("videoData"));
    }

}
