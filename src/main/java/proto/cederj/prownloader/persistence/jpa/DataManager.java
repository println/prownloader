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
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author Felipe Santos <fralph at ic.uff.br>
 */
public class DataManager {

    private final EntityManager em;

    public DataManager(EntityManager em) {
        this.em = em;
    }

    public void save(Object o) {
        synchronized (em) {
            EntityTransaction et = em.getTransaction();
            et.begin();
            em.persist(o);
            et.commit();
        }
    }

    public <T> T get(Class<T> entityType, Object key) {
        synchronized (em) {
            T object = em.find(entityType, key);
            return object;
        }
    }

    public <T> T getReference(Class<T> entityType, Object key) {
        synchronized (em) {
            T ref = em.getReference(entityType, key);
            return ref;
        }
    }

    public void update(Object o) {
        synchronized (em) {
            EntityTransaction et = em.getTransaction();
            et.begin();
            em.merge(o);
            et.commit();
        }
    }

    public void delete(Object o) {
        synchronized (em) {
            EntityTransaction et = em.getTransaction();
            et.begin();
            em.remove(em.contains(o) ? o : em.merge(o));
            et.commit();
        }
    }

    public CriteriaBuilder getCriteriaBuilder() {
        synchronized (em) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            return cb;
        }
    }

    public <T> List<T> getQuery(CriteriaQuery<T> query) {
        synchronized (em) {            
            TypedQuery<T> tquery = em.createQuery(query);
            List<T> result = tquery.getResultList();
            return result;
        }
    }

    public <T> T getQuerySingle(CriteriaQuery<T> query) {
        synchronized (em) {
            TypedQuery<T> tquery = em.createQuery(query);
            List<T> list = tquery.getResultList();
            if (list == null || list.isEmpty()) {
                return null;
            }
            return (T) list.get(0);
        }
    }

    public Query getQquery(String sql, Class klass) {
        synchronized (em) {
            Query query = em.createNativeQuery(sql, klass);
            return query;
        }
    }
}
