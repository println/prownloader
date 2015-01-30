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
package proto.cederj.prownloader.thirdparty.webripper;

import proto.cederj.prownloader.model.Course;
import proto.cederj.prownloader.model.Lesson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class CederjWebRipper implements WebRipper<Course> {

    private String userAgent;

    public CederjWebRipper(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public List<Course> extractFrom(String url, String htmlElement) throws IOException {
        Document d = Jsoup.connect(url).userAgent(userAgent).get();
        Elements e = d.select(htmlElement);
        List<Course> courses = new ArrayList<>();
        for (Element element : e) {
            Course course = extractCourse(element);
            courses.add(course);
        }
        return courses;
    }

    private Course extractCourse(Element e) {
        String[] label = e.select("span").html().split("-");
        String code = label[0].trim();
        String name = label[1].trim();
        Course course = new Course(code, name);

        Elements linkItems = e.select("li>a");
       for (Element link : linkItems) {
            String lessonUrl = link.attr("href");
            String lessonCode = link.html();
            Lesson lesson = new Lesson(lessonCode, lessonUrl);
            course.addLesson(lesson);
        }
        return course;
    }
}
