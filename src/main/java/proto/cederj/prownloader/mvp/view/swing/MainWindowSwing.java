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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Calendar;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import proto.cederj.prownloader.mvp.presenter.MainPresenter;
import proto.cederj.prownloader.mvp.view.MainView;
import proto.cederj.prownloader.mvp.view.swing.treemodel.CourseNode;
import proto.cederj.prownloader.mvp.view.swing.treemodel.CourseTreeModel;
import proto.cederj.prownloader.mvp.view.swing.treemodel.Node;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class MainWindowSwing extends javax.swing.JFrame implements MainView {

    private javax.swing.JScrollPane treeJPanel;
    private javax.swing.JPanel descJPanel;
    private MainPresenter presenter;

    private CoursePanelSwing coursePanel;
    private LessonPanelSwing lessonPanel;

    //tree
    private List<CourseNode> nodes;
    private CourseTreeModel treeModel;
    private JTree tree;

    public MainWindowSwing() {
        this.coursePanel = new CoursePanelSwing(this);
        this.lessonPanel = new LessonPanelSwing(this);
    }

    @Override
    public void initialize() {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 0, 0);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Prownloader");
        getContentPane().setPreferredSize(new Dimension(800, 300));
        setLayout(flowLayout);
        setResizable(false);
        getAccessibleContext().setAccessibleDescription("Proto's video-lesson downloader");
        pack();
        setLocationRelativeTo(null);

        treeJPanel = getTreeScrollPanel();
        descJPanel = getDescriptionPanel();

        add(treeJPanel, BorderLayout.CENTER);
        add(descJPanel);

        setVisible(true);
    }

    private JScrollPane getTreeScrollPanel() {
        //FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 0, 0);
        int v = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
        int h = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
        JScrollPane panel = new JScrollPane(v, h);
        panel.setPreferredSize(new Dimension(300, 300));
        panel.setBorder(BorderFactory.createEmptyBorder());
        //panel.getViewport().setLayout(flowLayout);
        return panel;
    }

    private JPanel getTreePanel() {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 0, 0);
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 300));
        panel.setBackground(Color.blue);
        panel.setLayout(flowLayout);
        return panel;
    }

    private JTree getTree(TreeModel model) {
        final JTree jTree = new JTree(model);
        jTree.setRootVisible(false);
        jTree.setShowsRootHandles(true);
        jTree.setLargeModel(true);
        jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        jTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                int[] rows = jTree.getSelectionRows();

                if (rows.length == 0) {
                    return;
                }
                TreePath path = jTree.getPathForRow(rows[0]);
                Object component = path.getLastPathComponent();
                Node node = (Node) component;
                if (node == null) {
                    return;
                }
                if (node.isLeaf()) {
                    onSelectLesson(node.getId());
                } else {
                    onSelectCourse(node.getId());
                }
            }
        });
        return jTree;
    }

    private JPanel getDescriptionPanel() {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 0, 0);
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(500, 300));
        panel.setLayout(flowLayout);
        return panel;
    }

    private void setTree(TreeModel model) {
        this.tree = getTree(model);
        treeJPanel.getViewport().removeAll();
        treeJPanel.setViewportView(tree);
        setVisible(true);
    }

    private void setDescription(JPanel jPanel) {
        descJPanel.removeAll();
        descJPanel.add(jPanel);
        descJPanel.repaint();
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
        setDescription(coursePanel);
    }

    @Override
    public void showLesson() {
        setDescription(lessonPanel);
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

    @Override
    public void setNavigationList(List<CourseNode> nodes) {
        this.nodes = nodes;
        this.treeModel = new CourseTreeModel(nodes);
        setTree(treeModel);       
    }

    @Override
    public void updateNavigationList(CourseNode updatedNode) {
        if (nodes.contains(updatedNode)) {
            int index = nodes.indexOf(updatedNode);
            CourseNode node = nodes.get(index);
            node.update(updatedNode);
        }
        tree.repaint();
    }
}
