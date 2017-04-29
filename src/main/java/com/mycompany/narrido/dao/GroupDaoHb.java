/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.dao;

import com.mycompany.narrido.dao.ice.GroupDao;
import com.mycompany.narrido.helper.SFH;
import com.mycompany.narrido.pojo.NarridoAccessCode;
import com.mycompany.narrido.pojo.NarridoFile;
import com.mycompany.narrido.pojo.NarridoGroup;
import com.mycompany.narrido.pojo.NarridoGroup_;
import com.mycompany.narrido.pojo.NarridoMembership;
import com.mycompany.narrido.pojo.NarridoPost;
import java.util.ArrayList;
import java.util.List;
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
public final class GroupDaoHb implements GroupDao{
    private static GroupDao instance;
    
    public static GroupDao getInstance(){
        if(instance == null){
            instance = new GroupDaoHb();
        }
        return instance;
    }

    @Deprecated
    @Override
    public NarridoGroup group(Integer groupId) {
        NarridoGroup group = null;
        Session sess = null;
        Transaction tx = null;
        
        try {
            sess = SFH.getSF().openSession();
            tx = sess.beginTransaction();
            CriteriaBuilder cb = sess.getCriteriaBuilder();
            CriteriaQuery<NarridoGroup> cq = cb.createQuery(NarridoGroup.class);
            
            Root<NarridoGroup> grp = cq.from(NarridoGroup.class);
            cq.where(cb.equal(grp.get(NarridoGroup_.groupId), groupId));
            
            TypedQuery<NarridoGroup> tq = sess.createQuery(cq);
            group = tq.getSingleResult();
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace(System.err);
            if(tx != null) tx.rollback();
        } finally {
            sess.close();
        }
        
        return group;
    }

    @Override
    public List<NarridoMembership> membership(NarridoGroup group) {
        Session sess = null;
        List<NarridoMembership> memz = new ArrayList<>();
        
        try {
            sess = SFH.getSF().openSession();
            sess.update(group);
            
            group.getMembership().size();
            memz = group.getMembership();
            
            //initialize userobject
            
        } catch (HibernateException he) {
            he.printStackTrace(System.err);
            throw he;
        } finally {
            if (sess != null) sess.close();
        }
        
        return memz;
    }

    @Override
    public List<NarridoAccessCode> accessCode(NarridoGroup group) {
        Session sess = null;
        List<NarridoAccessCode> codez = new ArrayList<>();
        
        try {
            sess = SFH.getSF().openSession();
            sess.update(group);
            
            group.getCodes().size();
            codez = group.getCodes();
        } catch (HibernateException he) {
            he.printStackTrace(System.err);
            throw he;
        } finally {
            if (sess != null) sess.close();
        }
        
        return codez;
    }

    @Override
    public List<NarridoPost> posts(NarridoGroup group) {
        List<NarridoPost> posts = new ArrayList<>();
        Session sess = null;
        
        try {
            sess = SFH.getSF().openSession();
            sess.update(group);
            
            group.getPosts().size();
            posts = group.getPosts();
            
            //sort by date
            posts.sort((p1, p2) -> p2.getDate().compareTo(p1.getDate()));
        } catch (HibernateException he) {
            he.printStackTrace(System.err);
            throw he;
        } finally {
            if (sess != null) sess.close();
        }
        
        return posts;
    }

}
