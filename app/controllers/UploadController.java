/*
    TODO: move code to VideoController
    (no one is allowed to move it !!)
 */

package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

@Security.Authenticated(Secured.class)
public class UploadController extends Controller {

    // TODO: bad way of writing files (check /assets))
    private static String systemPath = null;

    // TODO: handle upload fail
    public static Result uploadVideoClip() {
        // TODO: replace getFiles
        FilePart fp = request().body().asMultipartFormData().getFiles().get(0);
        File f = fp.getFile();

        FileInputStream fis = null;
        byte[] contents = new byte[(int) f.length()];
        try {
            fis = new FileInputStream(f);
            fis.read(contents);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String videoClipPath = systemPath + "sd.webm";
        try {
            f = new File(videoClipPath);
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(contents);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ok();
    }

}
