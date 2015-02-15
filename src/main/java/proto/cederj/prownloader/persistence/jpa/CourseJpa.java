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

import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import proto.cederj.prownloader.mvp.model.Course;
import proto.cederj.prownloader.persistence.CourseDao;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class CourseJpa implements CourseDao {

    private DataManager data;

    public CourseJpa(DataManager dataManager) {
        this.data = dataManager;
    }

    @Override
    public void save(Course o) {
        data.save(o);
    }

    @Override
    public void update(Course o) {
        data.update(o);
    }

    @Override
    public void delete(Course o) {
        data.delete(o);
    }

    @Override
    public List<Course> getAll() {
        CriteriaBuilder cb = data.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = cb.createQuery(Course.class);
        Root<Course> root = criteriaQuery.from(Course.class);
        criteriaQuery.select(root);
        List<Course> courses = data.getQuery(criteriaQuery);
        return courses;
    }

    @Override
    public Course getById(long id) {
        return data.get(Course.class, id);
    }

}
