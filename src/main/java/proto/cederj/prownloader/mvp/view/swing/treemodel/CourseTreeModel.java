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
package proto.cederj.prownloader.mvp.view.swing.treemodel;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class CourseTreeModel implements TreeModel {

    private final String root;
    private final List<TreeModelListener> listeners;
    private final List<CourseNode> courses;

    public CourseTreeModel(List<CourseNode> courses) {
        this.root = "Course";
        this.courses = courses;
        this.listeners = new ArrayList<>();
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent == root) {
            return courses.get(index);
        }
        if (parent instanceof CourseNode) {
            return ((CourseNode) parent).getLessons().get(index);
        }
        throw new IllegalArgumentException("Invalid parent class" + parent.getClass().getSimpleName());
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent == root) {
            return courses.size();
        }
        if (parent instanceof CourseNode) {
            return ((CourseNode) parent).getLessons().size();
        }
        throw new IllegalArgumentException("Invalid parent class" + parent.getClass().getSimpleName());
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent == root) {
            return courses.indexOf(child);
        }
        if (parent instanceof CourseNode) {
            return ((CourseNode) parent).getLessons().indexOf(child);
        }
        return 0;
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public boolean isLeaf(Object node) {
        return node instanceof LessonNode;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {

    }

    @Override
    public void removeTreeModelListener(TreeModelListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void addTreeModelListener(TreeModelListener listener) {
        listeners.add(listener);
    }
    
    
}
