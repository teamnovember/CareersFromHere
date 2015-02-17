package test;

import org.junit.*;
import play.test.*;
import play.libs.F.*;

import static play.test.Helpers.*;

import models.Video;
import models.Student;
import models.Alumni;
import models.School;

/**
 * Created by saravanan on 12/02/2015.
 */
public class VideoTest {
    @Test(expected=IllegalArgumentException.class)
    public void testStudentCannotUploadVideo() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                School s = new School("School Name");
                Student u = new Student("Name", "pass", "fake@gmail.com", s);
                Video v = new Video(u, "Title", "Description", "thumb_path.png");
            }
        });
    }

    @Test
    public void testAlumniCanUploadVideo() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                School s = new School("School Name");
                Alumni u = new Alumni("Name", "pass", "fake@gmail.com", s);
                Video v = new Video(u, "Title", "Description", "thumb_path.png");
            }
        });
    }

    @Test(expected=IllegalArgumentException.class)
    public void testVideoMustHaveTitle() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
                School s = new School("School Name");
                Alumni u = new Alumni("Name", "pass", "fake@gmail.com", s);
                Video v = new Video(u, null, "Description", "thumb_path.png");
            }
        });
    }
}
