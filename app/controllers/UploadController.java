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

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import play.libs.mailer.*;

@Security.Authenticated(Secured.class)
public class UploadController extends Controller {

    private static String prefixPath = "/assets/clips/";
//    private static String systemPath = "/home/tdn26/videos/";
    private static String systemPath = "/Users/tdn/Documents/workspace-uni/group_project/videos/";

    public static Result uploadVideo() {
        int count = request().body().asMultipartFormData().getFiles().size();

        ArrayList<String> audioPaths = new ArrayList<String>();
        ArrayList<String> oldVideoPaths = new ArrayList<String>();
        ArrayList<String> videoPaths = new ArrayList<String>();
        ArrayList<Integer> questionsId = new ArrayList<Integer>();
        ArrayList<Double> durationVideo = new ArrayList<Double>();

        String title = request().body().asMultipartFormData().asFormUrlEncoded().get("video-title")[0];
        String description = request().body().asMultipartFormData().asFormUrlEncoded().get("video-description")[0];

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

            // TODO: do not rely on i
            if (i % 2 == 0) {
                Integer qid = Integer.parseInt(request().body().asMultipartFormData().asFormUrlEncoded().get("video-questionId")[i / 2]);
                questionsId.add(qid);

                Double duration = Double.parseDouble(request().body().asMultipartFormData().asFormUrlEncoded().get("video-duration")[i / 2]);
                durationVideo.add(duration);
            }

            // TODO: should test on filetype and not on i (given the order of append in recorder.js)
            String name = "";
            if (i % 2 == 1)
                name = request().body().asMultipartFormData().asFormUrlEncoded().get("audio-filename")[i / 2];
            else name = request().body().asMultipartFormData().asFormUrlEncoded().get("video-filename")[i / 2];

            String oldFilePath = systemPath + "old-" + name;
            String filePath = systemPath + name;

            if (i % 2 == 1) audioPaths.add(filePath);
            else {
                oldVideoPaths.add(oldFilePath);
                videoPaths.add(filePath);

                filePath = oldFilePath;
            }

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

        String name = request().body().asMultipartFormData().asFormUrlEncoded().get("thumbnail-filename")[0];
        String thumbnailPath = systemPath + name;
        try {
//            String cmd = "avconv -i " + oldVideoPaths.get(0) + " -vframes 1 -y " + thumbnailPath;
            String cmd = "ffmpeg -i " + oldVideoPaths.get(0) + " -vf  thumbnail -frames:v 1 " + thumbnailPath;
            OutputStream os = Runtime.getRuntime().exec(cmd).getOutputStream();
            os.write("y".getBytes());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        createAndUpdateVideo(title, description, thumbnailPath, audioPaths, oldVideoPaths, videoPaths, questionsId, durationVideo);

        // TODO: redirect useless
        return redirect("/");
    }

    // TODO: does not update database
    // TODO: bad implementation; need to pass user information from record page
    // TODO: should use form submission, but it is complicated to combine 2 post methods
    private static void createAndUpdateVideo(String title, String description, String thumbnailPath, ArrayList<String> audioPaths, ArrayList<String> oldVideoPaths, ArrayList<String> videoPaths, ArrayList<Integer> questionsId, ArrayList<Double> durationVideo) {
        UserDAOImpl udao = new UserDAOImpl();
        Alumni user = (Alumni) udao.getUserFromContext();

        QuestionDAO qdao = new QuestionDAO();
        List<Question> questions = qdao.getActiveQuestions(user.getSchool());

        Video v = new Video(user, title, description, prefixPath + thumbnailPath);

        for (int i = 0; i < videoPaths.size(); ++i) {
            // merge webm with wav into webm
            // send "y" in case the file has to be replaced
            // this runs in async mode, so there is going to be a delay before you can see the video
            // TODO: try to keep quality of video
            try {
//                OutputStream os = Runtime.getRuntime().exec("avconv -i " + audioPaths.get(i) + " -itsoffset -00:00:00 -i " + oldVideoPaths.get(i) + " -map 0:0 -map 1:0 " + videoPaths.get(i)).getOutputStream();
                OutputStream os = Runtime.getRuntime().exec("ffmpeg -i " + audioPaths.get(i) + " -itsoffset -00:00:00 -i " + oldVideoPaths.get(i) + " -map 0:0 -map 1:0 " + videoPaths.get(i)).getOutputStream();
                os.write("y".getBytes());
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            VideoClip c = new VideoClip(prefixPath + videoPaths.get(i), prefixPath + audioPaths.get(i), questions.get(questionsId.get(i)), durationVideo.get(i));
            v.addClip(c);
        }

        v.save();
    }

    //TODO: make sure this works!
    private Result sendUploadEmails(School school, Alumni alumni) {
        Email aluMail = new Email();
        aluMail.setSubject("Thank you for uploading to CareersFromHere");
        aluMail.setFrom("Careers From Here FROM <careersfromhere@gmail.com>");
        aluMail.addTo("TO <" + alumni.getEmail() + ">");
        aluMail.setBodyText("");//todo
        String alumId = MailerPlugin.send(aluMail);

        List<Admin> admins = school.getAdmins();
        for (Admin x : admins) {
            Email adminMail = new Email();
            adminMail.setSubject("CareersFromHere: New Video has been uploaded");
            adminMail.setFrom("Careers From Here FROM <careersfromhere@gmail.com>");
            adminMail.addTo("TO <" + x.getEmail() + ">");
            adminMail.setBodyText("");//todo
            String id2 = MailerPlugin.send(adminMail);


        }

        return ok("Email " + alumId + " sent! and admins notified");
    }

}
