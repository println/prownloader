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
package proto.cederj.prownloader.persistence.jpa;

import proto.cederj.prownloader.mvp.model.Lesson;
import proto.cederj.prownloader.persistence.LessonDao;

public class LessonJpa implements LessonDao {

    private DataManager data;

    public LessonJpa(DataManager data) {
        this.data = data;
    }

    @Override
    public Lesson getById(long id) {
        return data.get(Lesson.class, id);
    }

    @Override
    public void save(Lesson o) {
        data.save(o);
    }

    @Override
    public void update(Lesson o) {
        data.update(o);
    }

    @Override
    public void delete(Lesson o) {
        data.delete(o);
    }

}
