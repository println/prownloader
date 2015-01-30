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

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class Video {

    private final String filename;
    private final String resolution;
    private final String aspectratio;
    private final String bitrate;

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

    @Override
    public String toString() {
        return filename + " " + resolution + " " + aspectratio + " " + bitrate;
    }

}
