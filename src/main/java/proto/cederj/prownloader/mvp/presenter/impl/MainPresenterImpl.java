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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import proto.cederj.prownloader.mvp.model.Course;
import proto.cederj.prownloader.mvp.model.Lesson;
import proto.cederj.prownloader.mvp.presenter.DialogPresenter;
import proto.cederj.prownloader.mvp.presenter.DynamicDialogCallbackListener;
import proto.cederj.prownloader.mvp.presenter.DynamicDialogPresenter;
import proto.cederj.prownloader.mvp.presenter.MainPresenter;
import proto.cederj.prownloader.mvp.view.MainView;
import proto.cederj.prownloader.mvp.view.swing.treemodel.CourseNode;
import proto.cederj.prownloader.mvp.view.swing.treemodel.LessonNode;
import proto.cederj.prownloader.persistence.CourseDao;
import proto.cederj.prownloader.persistence.DaoFactory;
import proto.cederj.prownloader.persistence.LessonDao;
import proto.cederj.prownloader.thirdparty.pseudohttpclient.DownloadMonitor;
import proto.cederj.prownloader.util.DataRipperUtil;

public class MainPresenterImpl implements MainPresenter {

    private final DaoFactory factory;
    private final MainView view;
    private final DialogPresenter dialog;
    private final DynamicDialogPresenter dynamicDialog;
    private final DataRipperUtil ripperUtil;
    private boolean running;

    public MainPresenterImpl(DaoFactory factory, MainView view, DialogPresenter dialog, DynamicDialogPresenter dynamicDialog) {
        this.factory = factory;
        this.view = view;
        this.dialog = dialog;
        this.dynamicDialog = dynamicDialog;
        this.ripperUtil = new DataRipperUtil();
    }

    @Override
    public void initialize() throws Exception {
        view.initialize();
        CourseDao dao = factory.getCourseDao();
        List<Course> courses = dao.getAll();
        if (courses.isEmpty()) {
            startThreadToGetIndexFromCederj();
            courses = dao.getAll();
            if(courses.isEmpty()){
                System.exit(0);
            }
        }
        view.setMainPresenter(this);
        showNavigation(courses);
        onSelectCourse(1);
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
        startThreadToGetLessonVideo(lessonId);
        LessonDao ld = factory.getLessonDao();
        Lesson lesson = ld.getById(lessonId);
        if (lesson == null) {
            return;
        }
        showLesson(lesson);
    }

    @Override
    public void onDownloadCourseVideoFile(long courseId) {
        startThreadToGetCourseVideo(courseId);
        CourseDao cd = factory.getCourseDao();
        Course course = cd.getById(courseId);
        if (course == null) {
            return;
        }
        showCourse(course);
    }

    @Override
    public void onDownloadCourseMetadata(long courseId) {
        startThreadToGetCourseMetadata(courseId);
        CourseDao cd = factory.getCourseDao();
        Course course = cd.getById(courseId);

        if (course == null) {
            return;
        }

        updateNavigator(course);
        showCourse(course);
    }

    @Override
    public void onDownloadLessonMetadata(long lessonId) {
        startThreadToGetLessonMetadata(lessonId);
        LessonDao ld = factory.getLessonDao();
        Lesson lesson = ld.getById(lessonId);

        if (lesson == null) {
            return;
        }

        Course course = lesson.getCourse();
        updateNavigator(course);
        showLesson(lesson);
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
                //check if file exists on disk and update the lesson model
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

    private void showNavigation(List<Course> courses) {
        List<CourseNode> trees = new ArrayList<>();
        for (Course course : courses) {
            CourseNode ct = createCourseNode(course);
            trees.add(ct);
        }
        view.setNavigationList(trees);
    }

    private void updateNavigator(Course course) {
        CourseNode ct = createCourseNode(course);
        view.updateNavigationList(ct);
    }

    private CourseNode createCourseNode(Course course) {
        CourseNode courseNode = new CourseNode(course.getId(), course.getName());
        List<Lesson> lessons = course.getLessons();
        for (Lesson lesson : lessons) {
            LessonNode lt = new LessonNode(lesson.getId(), lesson.getTitle());
            courseNode.addLesson(lt);
        }
        return courseNode;
    }

    private File getVideoLocalFile(Lesson lesson) throws URISyntaxException {
        Course course = lesson.getCourse();
        File file = ripperUtil.getFolder(course.getDefaultFolder() + File.separator + lesson.getFirstVideoLocalFilename());
        return file;
    }

    private void startThreadToGetIndexFromCederj() {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    CourseDao dao = factory.getCourseDao();
                    List<Course> courses = ripperUtil.getIndexFromCederj();
                    for (Course course : courses) {
                        dao.save(course);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(MainPresenterImpl.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    dialog.close();
                }
            }
        };
        Thread t = new Thread(run);
        t.setName("Getting course index...");
        t.setDaemon(true);
        t.start();
        dialog.setWindowTitle("Downloading Index...");
        dialog.setInfo("Recuperando dados do site do CEDERJ...");
        dialog.show();
    }

    private void startThreadToGetCourseMetadata(final long id) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    running = true;
                    CourseDao cd = factory.getCourseDao();
                    LessonDao ld = factory.getLessonDao();
                    Course course = cd.getById(id);

                    if (course != null) {
                        List<Lesson> lessons = course.getLessons();
                        int i = 1;
                        int total = lessons.size();
                        for (Lesson lesson : lessons) {
                            if (!running) {
                                break;
                            }
                            dynamicDialog.setDynamicMessage(lesson.getCode() + " (" + i++ + "/" + total + ")");
                            ripperUtil.getMetadataFromRio(lesson);
                            ld.update(lesson);
                        }
                        cd.update(course);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(MainPresenterImpl.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    running = false;
                    dynamicDialog.close();
                }
            }
        };
        Thread t = new Thread(run);
        t.setName("Getting Metadata...");
        t.setDaemon(true);
        t.start();
        dynamicDialog.setDynamicCallbackListener(new DynamicDialogCallbackListener() {
            @Override
            public void onCancel() {
                running = false;
            }
        });
        dynamicDialog.setWindowTitle("Downloading Metadata...");
        dynamicDialog.setInfo("Recuperando metadados do RIO Server(RNP)...");
        dynamicDialog.show();
    }

    private void startThreadToGetLessonMetadata(final long id) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    running = true;
                    CourseDao cd = factory.getCourseDao();
                    LessonDao ld = factory.getLessonDao();
                    Lesson lesson = ld.getById(id);
                    if (lesson != null) {
                        Course course = lesson.getCourse();
                        if (running) {
                            dynamicDialog.setDynamicMessage(lesson.getCode() + " (1/1)");
                            ripperUtil.getMetadataFromRio(lesson);
                            ld.update(lesson);
                            cd.update(course);
                        }
                    }
                } catch (Exception ex) {
                    Logger.getLogger(MainPresenterImpl.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    running = false;
                    dynamicDialog.close();
                }
            }
        };
        Thread t = new Thread(run);
        t.setName("Getting Metadata...");
        t.setDaemon(true);
        t.start();
        dynamicDialog.setDynamicCallbackListener(new DynamicDialogCallbackListener() {
            @Override
            public void onCancel() {
                running = false;
            }
        });
        dynamicDialog.setWindowTitle("Downloading Metadata...");
        dynamicDialog.setInfo("Recuperando metadados do RIO Server(RNP)...");
        dynamicDialog.show();
    }

    private void startThreadToGetCourseVideo(final long id) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    running = true;
                    CourseDao cd = factory.getCourseDao();
                    LessonDao ld = factory.getLessonDao();
                    Course course = cd.getById(id);
                    if (course != null) {
                        List<Lesson> lessons = course.getLessons();
                        int i = 1;
                        final int total = lessons.size();
                        for (final Lesson lesson : lessons) {

                            if (!running) {
                                break;
                            }
                            if (lesson.isUpdated()) {
                                final int index = i++;
                                DownloadMonitor monitor = new DownloadMonitor() {
                                    @Override
                                    public void status(long fileSize, long downloaded, int percent, String url) {
                                        dynamicDialog.setDynamicMessage(percent + "% " + lesson.getFirstVideoFilename() + " (" + index + "/" + total + ")");
                                    }
                                };
                                ripperUtil.getVideoFileFromRio(lesson, course.getDefaultFolder(), monitor);
                                if (running) {
                                    ld.update(lesson);
                                }
                            }
                        }
                        cd.update(course);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(MainPresenterImpl.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    running = false;
                    dynamicDialog.close();
                }
            }
        };
        Thread t = new Thread(run);
        t.setName("Getting videos...");
        t.setDaemon(true);
        t.start();
        dynamicDialog.setDynamicCallbackListener(new DynamicDialogCallbackListener() {
            @Override
            public void onCancel() {
                running = false;
                ripperUtil.stopDownload();
            }
        });
        dynamicDialog.setWindowTitle("Downloading Video...");
        dynamicDialog.setInfo("Baixando vídeos do RIO Server(RNP)...");
        dynamicDialog.show();

    }

    private void startThreadToGetLessonVideo(final long id) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    running = true;
                    CourseDao cd = factory.getCourseDao();
                    LessonDao ld = factory.getLessonDao();
                    final Lesson lesson = ld.getById(id);
                    if (lesson != null) {
                        Course course = lesson.getCourse();
                        if (running) {
                            if (lesson.isUpdated()) {
                                DownloadMonitor monitor = new DownloadMonitor() {
                                    @Override
                                    public void status(long fileSize, long downloaded, int percent, String url) {
                                        dynamicDialog.setDynamicMessage(percent + "% " + lesson.getFirstVideoFilename() + " (1/1)");
                                    }
                                };
                                ripperUtil.getVideoFileFromRio(lesson, course.getDefaultFolder(), monitor);
                                if (running) {
                                    ld.update(lesson);
                                }
                            }
                        }
                        cd.update(course);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(MainPresenterImpl.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    running = false;
                    dynamicDialog.close();
                }
            }
        };
        Thread t = new Thread(run);
        t.setName("Getting video...");
        t.setDaemon(true);
        t.start();
        dynamicDialog.setDynamicCallbackListener(new DynamicDialogCallbackListener() {
            @Override
            public void onCancel() {
                running = false;
                ripperUtil.stopDownload();
            }
        });
        dynamicDialog.setWindowTitle("Downloading Video...");
        dynamicDialog.setInfo("Baixando vídeo do RIO Server(RNP)...");
        dynamicDialog.show();

    }
}
