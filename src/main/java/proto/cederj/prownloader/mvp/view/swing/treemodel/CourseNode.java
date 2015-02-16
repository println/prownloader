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
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CourseNode implements Node {

    private final long id;
    private final String name;
    private final List<LessonNode> lessons;

    public CourseNode(long id, String name) {
        this.id = id;
        this.name = name;
        this.lessons = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public String toString() {
        return getName();
    }

    public void addLesson(LessonNode lesson) {
        lessons.add(lesson);
    }

    public List<LessonNode> getLessons() {
        return Collections.unmodifiableList(lessons);
    }

    public void update(CourseNode courseNode) {
        List<LessonNode> lessonNodes = courseNode.getLessons();
        for (LessonNode lessonNode : lessonNodes) {
            for (LessonNode lesson : lessons) {
                if (lesson.equals(lessonNode)) {
                    lesson.update(lessonNode);
                    break;
                }
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 29 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CourseNode other = (CourseNode) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
}
