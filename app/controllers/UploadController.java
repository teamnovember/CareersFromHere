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
import views.html.emails.registration_invite;
import views.html.emails.upload_admin_notify;
import views.html.emails.upload_alumni_notify;

@Security.Authenticated(Secured.class)
public class UploadController extends Controller {

    private static String prefixPath = "/assets/clips/";
//    private static String systemPath = "/home/tdn26/videos/";
    private static String systemPath = "/Users/tdn/Documents/workspace-uni/group_project/videos/";

    /**
     * Parses the data from an upload request and creates a Video object that is saved into the database.
     * Upload makes use of the avconv tool to merge wav with webm files.
     * @return Redirect to main page
     */
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

            if (i % 2 == 0) {
                Integer qid = Integer.parseInt(request().body().asMultipartFormData().asFormUrlEncoded().get("video-questionId")[i / 2]);
                questionsId.add(qid);

                Double duration = Double.parseDouble(request().body().asMultipartFormData().asFormUrlEncoded().get("video-duration")[i / 2]);
                durationVideo.add(duration);
            }

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

        createAndSaveVideo(title, description, thumbnailPath, audioPaths, oldVideoPaths, videoPaths, questionsId, durationVideo);

        flash("success", "You video was uploaded successfully. It has been sent to your school's admins for approval");
        return redirect("/");
    }

    /**
     * Creates a Video object from the set of arguments and saves it to the database.
     * @param title Title of video (metadata)
     * @param description Description of video (metadata)
     * @param thumbnailPath File system path for the video thumbnail
     * @param audioPaths File system paths for audio files (wav)
     * @param oldVideoPaths File system paths for old(no sound) video files (webm)
     * @param videoPaths File system paths for video files (webm after merge with wav)
     * @param questionsId Ids of the questions corresponding to each pair of audio and video files
     * @param durationVideo Total duration of the recording
     */
    private static void createAndSaveVideo(String title, String description, String thumbnailPath, ArrayList<String> audioPaths, ArrayList<String> oldVideoPaths, ArrayList<String> videoPaths, ArrayList<Integer> questionsId, ArrayList<Double> durationVideo) {
        UserDAOImpl udao = new UserDAOImpl();
        Alumni user = (Alumni) udao.getUserFromContext();

        QuestionDAO qdao = new QuestionDAO();
        List<Question> questions = qdao.getActiveQuestions(user.getSchool());

        Video v = new Video(user, title, description, prefixPath + thumbnailPath);

        for (int i = 0; i < videoPaths.size(); ++i) {
            // merge webm with wav into webm
            // send "y" in case the file has to be replaced
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

        sendUploadEmails(user.getSchool(), user, v);
    }

    private static void sendUploadEmails(School school, Alumni alumni, Video video) {
        Email aluMail = new Email();
        aluMail.setSubject("Careers From Here: Upload Confirmation");
        aluMail.setFrom("Careers From Here <careersfromhere@gmail.com>");
        aluMail.addTo(alumni.getName() + " <" + alumni.getEmail() + ">");
        aluMail.setBodyHtml(upload_alumni_notify.render(alumni).toString());
        MailerPlugin.send(aluMail);

        List<Admin> admins = school.getAdmins();
        for (Admin x : admins) {
            Email adminMail = new Email();
            adminMail.setSubject("Careers From Here: New Video has been uploaded to your school");
            adminMail.setFrom("Careers From Here <careersfromhere@gmail.com>");
            adminMail.addTo(x.getName() + " <" + x.getEmail() + ">");
            aluMail.setBodyHtml(upload_admin_notify.render(x, video).toString());
            MailerPlugin.send(adminMail);
        }
    }

}
