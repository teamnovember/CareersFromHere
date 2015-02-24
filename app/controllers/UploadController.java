/*
    TODO: move code to VideoController
    (no one is allowed to move it !!)
 */

package controllers;

import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;

@Security.Authenticated(Secured.class)
public class UploadController extends Controller {

    // TODO: bad way of writing files (check /assets))
    private static String systemPath = "/home/tdn26/video/";
//    private static String systemPath = "";

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

        String videoClipPath = systemPath + "-sd.webm";
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

    public static Result uploadVideo() {
        int count = request().body().asMultipartFormData().getFiles().size();

        ArrayList<String> audioPaths = new ArrayList<String>();
        ArrayList<String> videoPaths = new ArrayList<String>();

        for (int i = 0; i < count; ++i) {
            FilePart fp = request().body().asMultipartFormData().getFiles().get(i);
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

            // TODO: should test on filetype and not on i (given the order of append in recorder.js)
            String name = "";
            if (i % 2 == 1)
                name = request().body().asMultipartFormData().asFormUrlEncoded().get("audio-filename")[i / 2];
            else name = request().body().asMultipartFormData().asFormUrlEncoded().get("video-filename")[i / 2];

            String filePath = systemPath + name;

            if (i % 2 == 1) audioPaths.add(filePath);
            else videoPaths.add(filePath);

            try {
                f = new File(filePath);
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(contents);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        createAndUpdateVideo(audioPaths, videoPaths);

        // TODO: apparently redirect does not work
        return redirect("/");
    }

    // TODO: does not update database
    // TODO: bad implementation; need to pass user information from record page
    // TODO: should use form submission, but it is complicated to combine 2 post methods
    private static void createAndUpdateVideo(ArrayList<String> audioPaths, ArrayList<String> videoPaths) {
//        School s = new School("jas");
//        Alumni u = new Alumni("jau", "jap", "jau@jae.jat", s);
        UserDAOImpl udao = new UserDAOImpl();
        Alumni u = (Alumni) udao.getUser((long) 16);
        Video v = new Video(u, "jat", "jar", "jatp");

        ArrayList<Question> questions = new ArrayList<>();

        questions.add(0, new Question("People say nothing is impossible, but I do nothing every day.", 19.0, null));
        questions.add(1, new Question("Etc. – End of Thinking Capacity.", 37.0, null));
        questions.add(2, new Question("We live in the era of smart phones and stupid people.", 75.0, null));
        questions.add(3, new Question("Alarm Clocks: because every morning should begin with a heart attack.", 153.0, null));

        for (int i = 0; i < videoPaths.size(); ++i) {
            VideoClip c = new VideoClip(videoPaths.get(i), audioPaths.get(i), questions.get(i), 200.0);
            v.addClip(c);
        }

        v.save();
    }

}
