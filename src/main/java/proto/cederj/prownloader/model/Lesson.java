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
package proto.cederj.prownloader.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class Lesson {

    private final String code;
    private String sourceUrl;

    private String title;
    private String description;
    private String keywords;    
    
    private String author;
    private String duration;
    
    private String thumbnailFilename;
    private String syncFilename;
    private String indexFilename;
    private final List<Video> relatedVideos;    

    public Lesson(String code, String sourceUrl) {
        this.code = code;
        this.sourceUrl = sourceUrl;
        this.relatedVideos = new ArrayList<>();
    }
        
    public void addVideo(Video video){
        relatedVideos.add(video);
    }
    
    public void addVideo(List<Video> video){
        relatedVideos.addAll(video);
    }

    public String getCode() {
        return code;
    }

    public String getSourceUrl() {
        return sourceUrl;
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

    public String getAuthor() {
        return author;
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

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
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

    public void setAuthor(String author) {
        this.author = author;
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
}
