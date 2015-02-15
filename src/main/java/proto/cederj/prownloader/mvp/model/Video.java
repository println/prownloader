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
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
@Entity
public class Video implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String filename;
    private String resolution;
    private String aspectratio;
    private String bitrate;
    private String localFilename;

    @ManyToOne
    private Lesson lesson;

    public Video() {
    }

    public Video(String filename, String resolution, String aspectratio, String bitrate) {
        this.filename = filename;
        this.resolution = resolution;
        this.aspectratio = aspectratio;
        this.bitrate = bitrate;
    }

    public String getFilename() {
        return filename;
    }

    public String getResolution() {
        return resolution;
    }

    public String getAspectratio() {
        return aspectratio;
    }

    public String getBitrate() {
        return bitrate;
    }

    public String getLocalFilename() {
        return localFilename;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public void setLocalFilename(String localFilename) {
        this.localFilename = localFilename;
    }

    public void update(Video video) {
        this.aspectratio = video.aspectratio;
        this.bitrate = video.bitrate;
        this.filename = video.filename;
        this.resolution = video.resolution;
    }

    public void removeLocalFilename() {
        this.localFilename = null;
    }

    public boolean hasLocalFile() {
        if (localFilename == null) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return filename + " " + resolution + " " + aspectratio + " " + bitrate;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.filename);
        hash = 17 * hash + Objects.hashCode(this.resolution);
        hash = 17 * hash + Objects.hashCode(this.aspectratio);
        hash = 17 * hash + Objects.hashCode(this.bitrate);
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
        final Video other = (Video) obj;
        if (!Objects.equals(this.filename, other.filename)) {
            return false;
        }
        if (!Objects.equals(this.resolution, other.resolution)) {
            return false;
        }
        if (!Objects.equals(this.aspectratio, other.aspectratio)) {
            return false;
        }
        if (!Objects.equals(this.bitrate, other.bitrate)) {
            return false;
        }
        return true;
    }

}
