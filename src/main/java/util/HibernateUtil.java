package util;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }

    public static Session getHibernateSession() {
        // factory = new Configuration().configure().buildSessionFactory();
        final Session session = sessionFactory.openSession();
        return session;
    }

    public static void basicCreate(Object obj) {
        Session session = null;
        Transaction tx = null;
        try {
            session = getHibernateSession();
            tx = session.getTransaction();
            session.save(obj);
            tx.commit();
        } catch (Exception e) {
            if (tx != null)
                tx.rollback();
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public static List<Object> basicRead(String entityName) {
        Session session = null;
        List<Object> rtnList = null;
        String queryString = "FROM " + entityName;
        try {
            session = getHibernateSession();
            Query<Object> qry = session.createQuery(queryString);
            rtnList = qry.list();
        } catch (Exception e) {
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return rtnList;
    }

    public static <T> Object basicReadById(Class<T> entity, Long id) {
        Session session = null;
        Object rtnObj = null;
        try {
            session = getHibernateSession();
            rtnObj = session.get(entity, id);
        } catch (Exception e) {
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return rtnObj;
    }

    public static List<Object> basicRead(String entityName, HashMap<String, Object> paramsMap) {
        if (paramsMap.isEmpty() || paramsMap == null) {
            return basicRead(entityName);
        }

        Session session = null;
        List<Object> rtnList = null;
        String queryString = "FROM " + entityName + " WHERE ";
        try {
            for (String k : paramsMap.keySet()) {
                queryString += (k + "= :" + k);
            }
            session = getHibernateSession();
            Query<Object> qry = session.createQuery(queryString);
            paramsMap.entrySet().forEach(entry -> qry.setParameter(entry.getKey(), entry.getValue()));
            rtnList = qry.list();
        } catch (Exception e) {
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return rtnList;
    }

    public static List<Object> basicRead(String entityName, HashMap<String, Object> paramsMap, String additionClause) {
        if (paramsMap.isEmpty() || paramsMap == null) {
            return basicRead(entityName);
        }
        if (additionClause == null) {
            return basicRead(entityName, paramsMap);
        }
        Session session = null;
        List<Object> rtnList = null;
        String queryString = "FROM " + entityName + " WHERE ";
        try {
            for (String k : paramsMap.keySet()) {
                queryString += (k + "= :" + k);
            }
            session = getHibernateSession();
            queryString += additionClause;
            Query<Object> qry = session.createQuery(queryString);
            paramsMap.entrySet().forEach(entry -> qry.setParameter(entry.getKey(), entry.getValue()));
            rtnList = qry.list();
        } catch (Exception e) {
            return null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return rtnList;
    }

    public static void basicUpdate(String entityName, Object obj) {
        Session session = null;
        Transaction tx = null;
        try {
            session = getHibernateSession();
            tx = session.beginTransaction();
            session.update(entityName, obj);
            // session.update(entityName, obj);
            session.getTransaction().commit();
            // tx.commit();
            // session.flush();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (session != null) {
                // session.flush();
                session.close();
            }
        }
    }

    public static void basicDelete(String entityName, Object obj) {
        Session session = null;
        Transaction tx = null;
        try {
            session = getHibernateSession();
            tx = session.beginTransaction();
            session.delete(entityName, obj);
            session.getTransaction().commit();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
