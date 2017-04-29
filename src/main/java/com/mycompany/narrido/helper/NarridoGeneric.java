/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.helper;

import static com.mycompany.narrido.helper.SFH.getSF;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author princessmelisa
 */
public final class NarridoGeneric {
    
    public static enum NarridoOperation{
        SAVE,
        SAVE_UPDATE,
        UPDATE,
        DELETE
    };
    
    public static enum NarridoGet{
        SINGLE,
        LIST
    };

    private NarridoGeneric() {
    }

    public static <T> List<T> getList(Class<T> clazz) {
        List<T> list = null;
        Transaction tx = null;
        try (Session sess = getSF().openSession()) {
            tx = sess.beginTransaction();
            CriteriaBuilder cb = sess.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery(clazz);
            Root<T> obj = cq.from(clazz);
            cq.select(obj);
            TypedQuery<T> q = sess.createQuery(cq);
            list = q.getResultList();
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace(System.err);
            if (tx != null) {
                tx.rollback();
            }
        }

        return list;
    }

    /**
     * Generic data manipulation method
     * @param op the operation to be performed
     * @param obj the object to be persisted
     * @return the id for save operation, else null
     */
    public static Integer doThing(NarridoOperation op, Object... obj) {
        Session sess = SFH.getSF().openSession();
        Transaction tx = null;
        Integer returnId = null;
        try {
            tx = sess.beginTransaction();
            for (Object o : obj) {
                if (null == op) {
                    sess.save(o);
                } else switch (op) {
                    case SAVE_UPDATE:
                        sess.saveOrUpdate(o);
                        break;
                    case UPDATE:
                        sess.update(o);
                        break;
                    case DELETE:
                        sess.delete(o);
                        break;
                    default:
                        returnId = (Integer) sess.save(o);
                        break;
                }
            }
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace(System.err);
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            sess.close();
        }
        
        return returnId;
    }
    
    public static Integer saveThing(Object... obj){
        return doThing(NarridoOperation.SAVE, obj);
    }

    public static <T> T getSingle(Class<T> clazz, SingularAttribute sa, Object val) {
        T obj = null;
        Session sess = SFH.getSF().openSession();
        Transaction tx = null;

        try {
            sess.setDefaultReadOnly(true);
            CriteriaBuilder cb = sess.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(clazz);

            Root<T> root = cq.from(clazz);
            cq.where(cb.equal(root.get(sa), val));

            TypedQuery<T> tq = sess.createQuery(cq);
            obj = tq.getSingleResult();
        } catch (HibernateException | NoResultException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            sess.close();
        }

        return obj;
    }
    
    public static <T> List<T> getList(Class<T> clazz, SingularAttribute sa, Object val) {
        List<T> obj = null;
        Session sess = SFH.getSF().openSession();
        Transaction tx = null;

        try {
            sess.setDefaultReadOnly(true);
            CriteriaBuilder cb = sess.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(clazz);

            Root<T> root = cq.from(clazz);
            cq.where(cb.equal(root.get(sa), val));

            TypedQuery<T> tq = sess.createQuery(cq);
            obj = tq.getResultList();
        } catch (HibernateException | NoResultException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            sess.close();
        }

        return obj;
    }
    
    public static String getStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        
        e.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
