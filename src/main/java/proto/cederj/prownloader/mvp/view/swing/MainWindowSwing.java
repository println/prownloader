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
package proto.cederj.prownloader.mvp.view.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import proto.cederj.prownloader.mvp.presenter.MainPresenter;
import proto.cederj.prownloader.mvp.view.MainView;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class MainWindowSwing extends javax.swing.JFrame implements MainView {

    private javax.swing.JPanel treeJPanel;
    private javax.swing.JPanel descJPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private MainPresenter presenter;

    private CoursePanelSwing coursePanel;
    private LessonPanelSwing lessonPanel;

    public MainWindowSwing() {
        this.coursePanel = new CoursePanelSwing(this);
        this.lessonPanel = new LessonPanelSwing(this);
    }

    @Override
    public void initialize() {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 0, 0);
        FlowLayout flowLayout1 = new FlowLayout(FlowLayout.LEFT, 0, 0);
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT, 0, 0);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Prownloader");
        getContentPane().setPreferredSize(new Dimension(700, 300));
        setLayout(flowLayout);
        setResizable(false);
        getAccessibleContext().setAccessibleDescription("Proto's video-lesson downloader");
        pack();
        setLocationRelativeTo(null);

        treeJPanel = new JPanel();
        treeJPanel.setPreferredSize(new Dimension(300, 300));
        treeJPanel.setBackground(Color.blue);
        treeJPanel.setLayout(flowLayout1);

        descJPanel = new JPanel();
        descJPanel.setPreferredSize(new Dimension(400, 300));
        descJPanel.setBackground(Color.red);
        descJPanel.setLayout(flowLayout2);

        add(treeJPanel);
        add(descJPanel);

        setVisible(true);
    }

    public void setTree(JPanel jPanel) {
        treeJPanel.add(jPanel);
        setVisible(true);
    }

    public void setDescription(JPanel jPanel) {
        descJPanel.add(jPanel);
        setVisible(true);
    }

    @Override
    public void freeze() {
        setEnabled(false);
    }

    @Override
    public void unfreeze() {
        setEnabled(true);
    }

    @Override
    public void setMainPresenter(MainPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showCourse() {
        descJPanel.removeAll();
        descJPanel.add(coursePanel);
        setVisible(true);
    }

    @Override
    public void showLesson() {
        descJPanel.removeAll();
        descJPanel.add(lessonPanel);
        setVisible(true);
    }

    @Override
    public void onSelectCourse(long courseId) {
        presenter.onSelectCourse(courseId);
    }

    @Override
    public void onSelectLesson(long lessonId) {
        presenter.onSelectLesson(lessonId);
    }

    @Override
    public void onDownloadCourseVideoFile(long courseId) {
        presenter.onDownloadCourseVideoFile(courseId);
    }

    @Override
    public void onDownloadLessonVideoFile(long lessonId) {
        presenter.onDownloadLessonVideoFile(lessonId);
    }

    @Override
    public void onDownloadCourseMetadata(long courseId) {
        presenter.onDownloadCourseMetadata(courseId);
    }

    @Override
    public void onDownloadLessonMetadata(long lessonId) {
        presenter.onDownloadLessonMetadata(lessonId);
    }

    @Override
    public void onOpenFolder(long courseId) {
        presenter.onOpenFolder(courseId);
    }

    @Override
    public void onPlayVideo(long lessonId) {
        presenter.onPlayVideo(lessonId);
    }

    @Override
    public void setCourseId(long id) {
        coursePanel.setCourseId(id);
    }

    @Override
    public void setCourseName(String name) {
        coursePanel.setCourseName(name);
    }

    @Override
    public void setCourseLessonsAmount(int count) {
        coursePanel.setCourseLessonsAmount(count);
    }

    @Override
    public void setCourseProfessors(List<String> professors) {
        coursePanel.setCourseProfessors(professors);
    }

    @Override
    public void setCourseFolder(String folder) {
        coursePanel.setCourseFolder(folder);
    }

    @Override
    public void setCourseMetadataLastUpdate(Calendar calendar) {
        coursePanel.setCourseMetadataLastUpdate(calendar);
    }

    @Override
    public void setCourseVideoDownloadDate(Calendar calendar) {
        coursePanel.setCourseVideoDownloadDate(calendar);
    }

    @Override
    public void setLessonId(long id) {
        lessonPanel.setLessonId(id);
    }

    @Override
    public void setLessonTitle(String title) {
        lessonPanel.setLessonTitle(title);
    }

    @Override
    public void setLessonDescription(String description) {
        lessonPanel.setLessonDescription(description);
    }

    @Override
    public void setLessonProfessors(List<String> professors) {
        lessonPanel.setLessonProfessors(professors);
    }

    @Override
    public void setLessonVideoFilename(String filename) {
        lessonPanel.setLessonVideoFilename(filename);
    }

    @Override
    public void setLessonVideoResolution(String resolution) {
        lessonPanel.setLessonVideoResolution(resolution);
    }

    @Override
    public void setLessonVideoAspectRatio(String aspectRatio) {
        lessonPanel.setLessonVideoAspectRatio(aspectRatio);
    }

    @Override
    public void setLessonVideoBitrate(String bitrate) {
        lessonPanel.setLessonVideoBitrate(bitrate);
    }

    @Override
    public void setLessonVideoDuration(String duration) {
        lessonPanel.setLessonVideoDuration(duration);
    }
}
