/*
 * Copyright 2015 Felipe Santos <live.proto at hotmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package proto.cederj.prownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.commons.lang3.StringUtils;
import proto.cederj.prownloader.model.Course;
import proto.cederj.prownloader.model.Lesson;
import proto.cederj.prownloader.thirdparty.pseudohttpclient.DownloadMonitor;
import proto.cederj.prownloader.thirdparty.pseudohttpclient.GenericPseudoHttpClient;
import proto.cederj.prownloader.thirdparty.pseudohttpclient.PseudoHttpClient;
import proto.cederj.prownloader.thirdparty.rio.RioServerResolver;
import proto.cederj.prownloader.thirdparty.webripper.CederjWebRipper;
import proto.cederj.prownloader.thirdparty.webripper.WebRipper;
import proto.cederj.prownloader.util.Config;
import proto.cederj.prownloader.thirdparty.rio.FacadeRioXmlDecompiler;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class Prownloader implements DownloadMonitor {

    private List<Course> courses;

    public void initialize() throws Exception {
        Config config = new Config();
        WebRipper ripper = new CederjWebRipper(config.getUserAgent());
        courses = ripper.extractFrom(config.getTracker(), config.getTrackerElement());

        getAllMetaData(courses);
        //Course course = courses.get(2);
        //getMetadata(course);
        //getVideoFile(course);
    }

    private void getAllMetaData(List<Course> list) throws Exception {
        for (Course course : list) {
            System.out.print("\n-------");
            System.out.print(course.getName());
            System.out.println("-------");
            getMetadata(course);
        }
    }

    private void getMetadata(Course course) throws Exception {
        List<Lesson> lessons = course.getLessons();
        for (Lesson lesson : lessons) {
            getMetadata(lesson);
        }
    }

    private void getMetadata(Lesson lesson) throws Exception {
        Config config = new Config();
        PseudoHttpClient client = new GenericPseudoHttpClient(config.getUserAgent());
        RioServerResolver resolver = new RioServerResolver(client, config.getRioRedirect(), config.getRioTransfer(), config.getRioServerMirrorXmlTagname());
        String xml = resolver.getDefaultXml(lesson.getSourceUrl());
        InputStream is = client.doGet(xml);
        FacadeRioXmlDecompiler decompile = new FacadeRioXmlDecompiler(is);
        lesson.setTitle(decompile.getTitle());
        lesson.setAuthor(decompile.getAuthor());
        lesson.setDescription(decompile.getDescription());
        lesson.setDuration(decompile.getDuration());
        lesson.setIndexFilename(decompile.getIndexFilename());
        lesson.setKeywords(decompile.getKeywords());
        lesson.addVideo(decompile.getRelatedVideos());
        lesson.setSyncFilename(decompile.getSyncFilename());
        lesson.setThumbnailFilename(decompile.getThumbnailFilename());

        System.out.println(lesson.getTitle());
    }

    private void getVideoFile(Course course) {
        try {
            String foldername = course.getCode() + " - " + StringUtils.stripAccents(course.getName());
            File folder = getFolder(foldername);
            if (!folder.exists()) {
                folder.mkdir();
            }
            List<Lesson> lessons = course.getLessons();
            for (Lesson lesson : lessons) {
                //getVideoFile(lesson, folder);
            }

        } catch (Exception ex) {
            Logger.getLogger(Prownloader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void getVideoFile(Lesson lesson, File folder) throws Exception {
        Config config = new Config();
        PseudoHttpClient client = new GenericPseudoHttpClient(config.getUserAgent());
        RioServerResolver resolver = new RioServerResolver(client, config.getRioRedirect(), config.getRioTransfer(), config.getRioServerMirrorXmlTagname());
        String videoFileName = lesson.getRelatedVideos().get(0).getFilename();
        String videoUrl = resolver.getFile(lesson.getSourceUrl(), videoFileName);
        String[] temp = videoFileName.split("\\.");
        String newVideoFileName = temp[0] + " - " + StringUtils.stripAccents(lesson.getTitle()) + "." + temp[1];
        File videoFile = new File(folder, newVideoFileName);

        client.doDownload(videoUrl, videoFile, this);

    }

    private File getFolder(String foldername) throws URISyntaxException {
        File base = new File(Config.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
        File folder = new File(base, foldername);
        return folder;
    }

    @Override
    public void status(long fileSize, long downloaded, int percent, String url) {
        if (percent == 100) {
            System.out.println(url);
        }
    }

}
