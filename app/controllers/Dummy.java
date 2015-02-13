package controllers;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.dummy_page;

import java.io.*;

public class Dummy extends Controller {

    public static Result dummy_page() {
        return ok(dummy_page.render());
    }

    public static Result upload() {
        Http.MultipartFormData.FilePart fp = request().body().asMultipartFormData().getFiles().get(0);

        File f = fp.getFile();
        FileInputStream fis = null;
        byte[] data = new byte[(int) f.length()];
        try {
            fis = new FileInputStream(f);

            fis.read(data);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File of = new File("/full-path-here/ABCDEF.webm");
        try {
            FileOutputStream fos = new FileOutputStream(of);
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ok("worked");
    }

}
