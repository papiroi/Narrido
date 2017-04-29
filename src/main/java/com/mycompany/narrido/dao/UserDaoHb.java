/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.dao;

import com.mycompany.narrido.dao.ice.UserDao;
import com.mycompany.narrido.helper.NarridoGeneric;
import com.mycompany.narrido.helper.SFH;
import com.mycompany.narrido.pojo.NarridoFile;
import com.mycompany.narrido.pojo.NarridoFile_;
import com.mycompany.narrido.pojo.NarridoGroup;
import com.mycompany.narrido.pojo.NarridoMembership;
import com.mycompany.narrido.pojo.NarridoUser;
import com.mycompany.narrido.pojo.NarridoUser_;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author princessmelisa
 */
public final class UserDaoHb implements UserDao{

    private static UserDaoHb instance;
    
    public static UserDaoHb getInstance(){
        if(instance == null){
            instance = new UserDaoHb();
        }
        return instance;
    }

    private UserDaoHb() {}
    
    @Override
    public void saveUser(NarridoUser user) {
        Transaction tx = null;
        try(Session sess = SFH.getSF().openSession()) {
            tx = sess.beginTransaction();
            sess.save(user);
            tx.commit();
        }catch(HibernateException e) {
            e.printStackTrace(System.err);
            if(tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public List<NarridoUser> users() {
        return NarridoGeneric.getList(NarridoUser.class);
    }

    @Override
    public NarridoUser user(Integer idNumber) {
        NarridoUser user = null;
        Transaction tx = null;
        Session sess = null;
        try {
            sess = SFH.getSF().openSession();
            tx = sess.beginTransaction();
            
            CriteriaBuilder builder = sess.getCriteriaBuilder();
            CriteriaQuery<NarridoUser> cq = builder.createQuery(NarridoUser.class);
            Root<NarridoUser> nUser = cq.from(NarridoUser.class);
            cq.where(
                builder.equal(nUser.get(NarridoUser_.idNumber), idNumber)
            );
            
            TypedQuery<NarridoUser> q = sess.createQuery(cq);
            user = q.getSingleResult();
            tx.commit();
        } catch (HibernateException | NoResultException e) {
            e.printStackTrace(System.err);
            if(tx != null) tx.rollback();
        }finally{
            if(sess != null) sess.close();
        }
        return user;
    }

    @Override
    public List<NarridoGroup> ownedGroups(NarridoUser user) {
        return groupz(user, true);
    }

    @Override
    public List<NarridoGroup> joinedGroups(NarridoUser user) {
        return groupz(user, false);
    }
    
    private List<NarridoGroup> groupz(NarridoUser user, boolean isOwning) {
        List<NarridoGroup> groups = new ArrayList<>();
        
        final Session sess = SFH.getSF().openSession();
        
        try {
            
            sess.update(user); //reattach the user into Session
            
            if(isOwning) {
                user.getOwnedGroups().size();
                groups = user.getOwnedGroups();
            } else {
                List<NarridoGroup> group2 = new ArrayList<>();
                
                user.getJoinedGroups().size();
                for(NarridoMembership membership : user.getJoinedGroups()){
                    if(membership.getConfirmed()) {
                        membership.getGroup().getGroupId();
                        group2.add(membership.getGroup());
                    }
                }
                
                if(group2.size() > 0) groups = group2;
            }
        } catch (HibernateException e) {
            e.printStackTrace(System.err);
        } finally {
            sess.close();
        }
        
        return groups;
    }

    @Override
    public List<NarridoFile> myFiles(NarridoUser user) {
        List<NarridoFile> files = new ArrayList<>();
        final Session sess = SFH.getSF().openSession();
        
        try {
            CriteriaBuilder cb = sess.getCriteriaBuilder();
            CriteriaQuery<NarridoFile> cq = cb.createQuery(NarridoFile.class);
            Root<NarridoFile> root = cq.from(NarridoFile.class);
            cq.where(cb.and(
                    cb.and(
                            cb.equal(root.get(NarridoFile_.uploader), user),
                            cb.isNull(root.get(NarridoFile_.group)))
                    ),
                    cb.equal(root.get(NarridoFile_.fileType), "file")
            );
                    
            
            TypedQuery<NarridoFile> tq = sess.createQuery(cq);
            files = tq.getResultList();
        } catch (HibernateException e) {
            e.printStackTrace(System.err);
        } finally {
            sess.close();
        }
        
        return files;
    }
    
}
