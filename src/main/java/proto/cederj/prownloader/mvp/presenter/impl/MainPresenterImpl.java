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
package proto.cederj.prownloader.mvp.presenter.impl;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import proto.cederj.prownloader.mvp.model.Course;
import proto.cederj.prownloader.mvp.model.Lesson;
import proto.cederj.prownloader.mvp.presenter.MainPresenter;
import proto.cederj.prownloader.mvp.view.MainView;
import proto.cederj.prownloader.persistence.CourseDao;
import proto.cederj.prownloader.persistence.DaoFactory;
import proto.cederj.prownloader.persistence.LessonDao;
import proto.cederj.prownloader.util.DataRipperUtil;

public class MainPresenterImpl implements MainPresenter {

    private DaoFactory factory;
    private MainView view;
    private DataRipperUtil ripperUtil;

    public MainPresenterImpl(DaoFactory factory, MainView view) {
        this.factory = factory;
        this.view = view;
        this.ripperUtil = new DataRipperUtil();
    }

    @Override
    public void initialize() throws Exception {
        view.initialize();

        CourseDao dao = factory.getCourseDao();
        List<Course> courses = dao.getAll();
        if (courses.isEmpty()) {
            courses = ripperUtil.getIndexFromCederj();
            for (Course course : courses) {
                dao.save(course);
            }
        }
        view.setMainPresenter(this);
        onSelectCourse(1);
        //onSelectLesson(1);
    }

    @Override
    public void onSelectCourse(long courseId) {
        CourseDao cd = factory.getCourseDao();
        Course course = cd.getById(courseId);
        showCourse(course);
    }

    @Override
    public void onSelectLesson(long lessonId) {
        LessonDao ld = factory.getLessonDao();
        Lesson lesson = ld.getById(lessonId);
        showLesson(lesson);
    }

    @Override
    public void onDownloadLessonVideoFile(long lessonId) {
        LessonDao ld = factory.getLessonDao();
        Lesson lesson = ld.getById(lessonId);
        try {
            if (lesson != null && lesson.isUpdated()) {
                Course course = lesson.getCourse();
                ripperUtil.getVideoFileFromRio(lesson, course.getDefaultFolder(), null);
                ld.update(lesson);
                showLesson(lesson);
            }
        } catch (Exception ex) {
            Logger.getLogger(MainPresenterImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onDownloadCourseVideoFile(long courseId) {
        CourseDao cd = factory.getCourseDao();
        LessonDao ld = factory.getLessonDao();
        Course course = cd.getById(courseId);
        try {
            if (course != null) {
                List<Lesson> lessons = course.getLessons();
                for (Lesson lesson : lessons) {
                    if (lesson.isUpdated()) {
                        ripperUtil.getVideoFileFromRio(lesson, course.getDefaultFolder(), null);
                        ld.update(lesson);
                    }
                }
                cd.update(course);
                showCourse(course);
            }
        } catch (Exception ex) {
            Logger.getLogger(MainPresenterImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onDownloadCourseMetadata(long courseId) {
        CourseDao cd = factory.getCourseDao();
        LessonDao ld = factory.getLessonDao();
        Course course = cd.getById(courseId);
        try {
            if (course != null) {
                List<Lesson> lessons = course.getLessons();
                for (Lesson lesson : lessons) {
                    ripperUtil.getMetadataFromRio(lesson);
                    ld.update(lesson);
                }
                cd.update(course);
                showCourse(course);
            }
        } catch (Exception ex) {
            Logger.getLogger(MainPresenterImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onDownloadLessonMetadata(long lessonId) {
        LessonDao ld = factory.getLessonDao();
        Lesson lesson = ld.getById(lessonId);
        try {
            if (lesson != null) {
                ripperUtil.getMetadataFromRio(lesson);
                ld.update(lesson);
                showLesson(lesson);
            }
        } catch (Exception ex) {
            Logger.getLogger(MainPresenterImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onOpenFolder(long courseId) {
        CourseDao cd = factory.getCourseDao();
        Course course = cd.getById(courseId);
        if (course != null) {
            try {
                Desktop desktop = Desktop.getDesktop();
                File folder = ripperUtil.getFolder(course.getDefaultFolder());
                if (!folder.exists()) {
                    folder.mkdir();
                }
                desktop.open(folder);
            } catch (URISyntaxException ex) {
                Logger.getLogger(MainPresenterImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MainPresenterImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void onPlayVideo(long lessonId) {
        LessonDao ld = factory.getLessonDao();
        Lesson lesson = ld.getById(lessonId);

        if (lesson != null && lesson.hasLocalVideoFile()) {
            try {
                Desktop desktop = Desktop.getDesktop();
                File file = getVideoLocalFile(lesson);
                if (file.exists()) {
                    desktop.open(file);
                }
            } catch (URISyntaxException ex) {
                Logger.getLogger(MainPresenterImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MainPresenterImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void showCourse(Course course) {
        if (course != null) {
            view.setCourseId(course.getId());
            view.setCourseName(course.getName());
            view.setCourseLessonsAmount(course.getLessons().size());
            view.setCourseProfessors(course.getProfessors());
            view.setCourseVideoDownloadDate(course.getLastVideoDownloadDate());
            view.setCourseMetadataLastUpdate(course.getLastModifiedMetadata());
            view.setCourseFolder(course.getDefaultFolder());
            view.showCourse();
        }
    }

    private void showLesson(Lesson lesson) {
        if (lesson != null) {
            try {
                //check if file exists on disk and update the model lesson
                File file = getVideoLocalFile(lesson);
                if (!file.exists() && lesson.hasLocalVideoFile()) {
                    lesson.removeLocalVideoFile();
                }
            } catch (URISyntaxException ex) {
            }
            
            view.setLessonId(lesson.getId());
            view.setLessonTitle(lesson.getTitle());
            view.setLessonDescription(lesson.getDescription());
            view.setLessonProfessors(lesson.getAuthors());
            view.setLessonVideoFilename(lesson.getFirstVideoLocalFilename());
            view.setLessonVideoResolution(lesson.getFirstVideoResolution());
            view.setLessonVideoAspectRatio(lesson.getFirstVideoAspectratio());
            view.setLessonVideoBitrate(lesson.getFirstVideoBitrate());
            view.setLessonVideoDuration(lesson.getDuration());
            view.showLesson();

        }
    }

    private File getVideoLocalFile(Lesson lesson) throws URISyntaxException {
        Course course = lesson.getCourse();
        File file = ripperUtil.getFolder(course.getDefaultFolder() + File.separator + lesson.getFirstVideoLocalFilename());
        return file;
    }
}
