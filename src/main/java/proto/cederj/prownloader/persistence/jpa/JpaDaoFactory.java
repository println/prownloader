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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import proto.cederj.prownloader.persistence.CourseDao;
import proto.cederj.prownloader.persistence.DaoFactory;
import proto.cederj.prownloader.persistence.LessonDao;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class JpaDaoFactory implements DaoFactory, AutoCloseable {

    private static JpaDaoFactory instance;
    @PersistenceContext
    private EntityManagerFactory emf;
    private EntityManager em;

    private JpaDaoFactory(EntityManagerFactory emf) throws Exception {
        try {
            this.emf = emf;
            this.em = emf.createEntityManager();
        } catch (Exception ex) {
            close();
            throw ex;
        }
    }

    public static JpaDaoFactory newInstance(EntityManagerFactory emf) throws Exception {
        if (instance == null) {
            instance = new JpaDaoFactory(emf);
        }
        return instance;
    }

    public static JpaDaoFactory getInstance() {
        if (instance == null) {
            throw new RuntimeException("Factory need be created as a new instance!");
        }
        return instance;
    }

    @Override
    public void close() throws Exception {
        try {
            em.close();
        } catch (Exception e) {
        }
        try {
            emf.close();
        } catch (Exception e) {
        }
    }

    private DataManager getDataManager() {
        return new DataManager(em);
    }

    @Override
    public CourseDao getCourseDao() {
        return new CourseJpa(getDataManager());
    }

    @Override
    public LessonDao getLessonDao() {
        return new LessonJpa(getDataManager());
    }
}
