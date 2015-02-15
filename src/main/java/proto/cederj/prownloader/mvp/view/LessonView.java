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
package proto.cederj.prownloader.mvp.view;

import java.util.List;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public interface LessonView {

    void setLessonId(long id);

    void setLessonTitle(String title);

    void setLessonDescription(String description);

    void setLessonProfessors(List<String> professors);

    void setLessonVideoFilename(String filename);

    void setLessonVideoResolution(String resolution);

    void setLessonVideoAspectRatio(String aspectRatio);

    void setLessonVideoBitrate(String bitrate);

    void setLessonVideoDuration(String duration);
}
