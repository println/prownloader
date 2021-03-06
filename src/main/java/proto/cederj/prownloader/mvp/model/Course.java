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
package proto.cederj.prownloader.mvp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
@Entity
public class Course implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String code;
    private String name;
    private String defaultFolder;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar lastModifiedMetadata;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Calendar lastVideoDownloadDate;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    List<Lesson> lessons;

    public Course() {
    }

    public Course(String code, String name) {
        this.code = code;
        this.name = name;
        this.lastModifiedMetadata = Calendar.getInstance();
        this.lastVideoDownloadDate = Calendar.getInstance();
        this.defaultFolder = code + " - " + StringUtils.stripAccents(name);
        this.lessons = new ArrayList<>();
    }

    public void addLesson(Lesson lesson) {
        if (!lessons.contains(lesson)) {
            lessons.add(lesson);
            lesson.setCourse(this);
        }
    }

    public long getId() {
        return id;
    }

    public Calendar getLastModifiedMetadata() {
        return lastModifiedMetadata;
    }

    public Calendar getLastVideoDownloadDate() {
        return lastVideoDownloadDate;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public List<String> getProfessors() {
        Set<String> professors = new HashSet<>();
        for (Lesson lesson : lessons) {
            List<String> authors = lesson.getAuthors();
            for (String author : authors) {
                professors.add(author);
            }
        }
        List<String> list = new ArrayList<>(professors);
        return list;
    }

    public String getDefaultFolder() {
        return defaultFolder;
    }

    public void markMetadataUpdateDate() {
        lastModifiedMetadata = Calendar.getInstance();
    }

    public void markVideoDownloadDate() {
        lastVideoDownloadDate = Calendar.getInstance();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.code);
        hash = 31 * hash + Objects.hashCode(this.name);
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
        final Course other = (Course) obj;
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

}
