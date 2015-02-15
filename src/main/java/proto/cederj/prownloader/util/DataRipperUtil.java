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
package proto.cederj.prownloader.util;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import proto.cederj.prownloader.mvp.model.Course;
import proto.cederj.prownloader.mvp.model.Lesson;
import proto.cederj.prownloader.mvp.model.Video;
import proto.cederj.prownloader.thirdparty.pseudohttpclient.DownloadMonitor;
import proto.cederj.prownloader.thirdparty.pseudohttpclient.GenericPseudoHttpClient;
import proto.cederj.prownloader.thirdparty.pseudohttpclient.PseudoHttpClient;
import proto.cederj.prownloader.thirdparty.rio.RioServerResolver;
import proto.cederj.prownloader.thirdparty.webripper.CederjWebRipper;
import proto.cederj.prownloader.thirdparty.webripper.WebRipper;
import proto.cederj.prownloader.thirdparty.rio.FacadeRioXmlDecompiler;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class DataRipperUtil {

    public List<Course> getIndexFromCederj() throws Exception {
        Config config = new Config();
        WebRipper ripper = new CederjWebRipper(config.getUserAgent());
        List<Course> courses = ripper.extractFrom(config.getTracker(), config.getTrackerElement());
        return courses;
    }

    public void getAllMetaDataFromRio(List<Course> cs) throws Exception {
        for (Course course : cs) {
            getMetadataFromRio(course);
        }
    }

    public void getMetadataFromRio(Course course) throws Exception {
        List<Lesson> lessons = course.getLessons();
        for (Lesson lesson : lessons) {
            getMetadataFromRio(lesson);
        }
    }

    public void getMetadataFromRio(Lesson lesson) throws Exception {
        Config config = new Config();
        PseudoHttpClient client = new GenericPseudoHttpClient(config.getUserAgent());
        RioServerResolver resolver = new RioServerResolver(client, config.getRioRedirect(), config.getRioTransfer(), config.getRioServerMirrorXmlTagname());
        String xml = resolver.getDefaultXml(lesson.getSourceUrl());
        InputStream is = client.doGet(xml);
        FacadeRioXmlDecompiler decompile = new FacadeRioXmlDecompiler(is);
        lesson.setTitle(decompile.getTitle());
        lesson.setAuthors(decompile.getAuthor());
        lesson.setDescription(decompile.getDescription());
        lesson.setDuration(decompile.getDuration());
        lesson.setIndexFilename(decompile.getIndexFilename());
        lesson.setKeywords(decompile.getKeywords());
        lesson.setVideos(decompile.getRelatedVideos());
        lesson.setSyncFilename(decompile.getSyncFilename());
        lesson.setThumbnailFilename(decompile.getThumbnailFilename());
        lesson.setSourceVersion(decompile.getSourceVersion());
        lesson.setUpdated();

        Course course = lesson.getCourse();
        course.markMetadataUpdateDate();
    }

    public void getVideoFileFromRio(Course course, DownloadMonitor monitor) throws Exception {
        String foldername = course.getDefaultFolder();

        List<Lesson> lessons = course.getLessons();
        for (Lesson lesson : lessons) {
            getVideoFileFromRio(lesson, foldername, monitor);
        }
    }

    public void getVideoFileFromRio(Lesson lesson, String foldername, DownloadMonitor monitor) throws Exception {
        Config config = new Config();
        PseudoHttpClient client = new GenericPseudoHttpClient(config.getUserAgent());
        RioServerResolver resolver = new RioServerResolver(client, config.getRioRedirect(), config.getRioTransfer(), config.getRioServerMirrorXmlTagname());

        Video video = lesson.getRelatedVideos().get(0);
        String videoFileName = video.getFilename();
        String videoUrl = resolver.getFile(lesson.getSourceUrl(), videoFileName);
        String[] temp = videoFileName.split("\\.");
        String localVideoFilename = temp[0] + " - " + StringUtils.stripAccents(lesson.getTitle()) + "." + temp[1];
        video.setLocalFilename(localVideoFilename);
        
        
        File folder = getFolder(foldername);
        if (!folder.exists()) {
            folder.mkdir();
        }
        File videoFile = new File(folder, localVideoFilename);

        client.doDownload(videoUrl, videoFile, monitor);

        Course course = lesson.getCourse();
        course.markVideoDownloadDate();
    }

    public File getFolder(String foldername) throws URISyntaxException {
        File base = new File(Config.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
        File folder = new File(base, foldername);
        return folder;
    }
}
