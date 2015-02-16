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
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
@Entity
public class Lesson implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String code;
    private String sourceUrl;
    private int sourceVersion;

    private String title;
    private String description;
    private String keywords;

    @ElementCollection
    private List<String> authors;

    private String duration;

    private String thumbnailFilename;
    private String syncFilename;
    private String indexFilename;

    private boolean isUpdated;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Video> relatedVideos;

    @ManyToOne
    private Course course;

    public Lesson() {
    }

    public Lesson(String code, String sourceUrl) {
        this.code = code;
        this.sourceUrl = sourceUrl;
        defaltValues();
    }

    public String getCode() {
        return code;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public int getSourceVersion() {
        return sourceVersion;
    }

    public String getTitle() {

        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getKeywords() {
        return keywords;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getDuration() {
        return duration;
    }

    public String getThumbnailFilename() {
        return thumbnailFilename;
    }

    public String getSyncFilename() {
        return syncFilename;
    }

    public String getIndexFilename() {
        return indexFilename;
    }

    public List<Video> getRelatedVideos() {
        return relatedVideos;
    }

    public long getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public String getFirstVideoFilename() {
        if (!hasVideos()) {
            return getNotAvailble();
        }
        Video video = relatedVideos.get(0);
        return video.getFilename();
    }

    public String getFirstVideoLocalFilename() {
        if (!hasLocalVideoFile()) {
            return getNotAvailble();
        }
        Video video = relatedVideos.get(0);
        return video.getLocalFilename();
    }

    public String getFirstVideoResolution() {
        if (!hasVideos()) {
            return getNotAvailble();
        }
        Video video = relatedVideos.get(0);
        return video.getResolution();
    }

    public String getFirstVideoAspectratio() {
        if (!hasVideos()) {
            return getNotAvailble();
        }
        Video video = relatedVideos.get(0);
        return video.getAspectratio();
    }

    public String getFirstVideoBitrate() {
        if (!hasVideos()) {
            return getNotAvailble();
        }
        Video video = relatedVideos.get(0);
        return video.getBitrate();
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public void setSourceVersion(int sourceVersion) {
        this.sourceVersion = sourceVersion;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setThumbnailFilename(String thumbnailFilename) {
        this.thumbnailFilename = thumbnailFilename;
    }

    public void setSyncFilename(String syncFilename) {
        this.syncFilename = syncFilename;
    }

    public void setIndexFilename(String indexFilename) {
        this.indexFilename = indexFilename;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public boolean hasLocalVideoFile() {
        if (!hasVideos()) {
            return false;
        }
        Video video = relatedVideos.get(0);
        return video.hasLocalFile();
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated() {
        isUpdated = true;
    }

    public void removeLocalVideoFile() {
        if (hasVideos()) {
            Video video = relatedVideos.get(0);
            video.removeLocalFilename();
        }
    }

    public void setVideos(List<Video> videos) {
        for (Video video : videos) {
            if (!relatedVideos.contains(video)) {
                relatedVideos.add(video);
                video.setLesson(this);
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.code);
        hash = 61 * hash + Objects.hashCode(this.sourceUrl);
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
        final Lesson other = (Lesson) obj;
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        if (!Objects.equals(this.sourceUrl, other.sourceUrl)) {
            return false;
        }
        return true;
    }

    private void defaltValues() {
        this.relatedVideos = new ArrayList<>();
        this.authors = new ArrayList<>();
        this.authors.add(getNotAvailble());
        this.title = getNotAvailble();
        this.description = getNotAvailble();
        this.keywords = getNotAvailble();
        this.duration = getNotAvailble();
        this.thumbnailFilename = getNotAvailble();
        this.syncFilename = getNotAvailble();
        this.indexFilename = getNotAvailble();
    }

    private String getNotAvailble() {
        return "n/a";
    }

    private boolean hasVideos() {
        if (relatedVideos == null || relatedVideos.isEmpty()) {
            return false;
        }
        return true;
    }
}
