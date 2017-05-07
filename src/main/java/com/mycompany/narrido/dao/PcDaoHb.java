/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.dao;
import com.mycompany.narrido.dao.ice.PcDao;
import com.mycompany.narrido.helper.NarridoGeneric;
import com.mycompany.narrido.helper.SFH;
import static com.mycompany.narrido.pojo.NarridoFile_.group;
import com.mycompany.narrido.pojo.NarridoPc;
import com.mycompany.narrido.pojo.NarridoPc_;
import java.util.Date;
import java.util.List;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author princessmelisa
 */
public class PcDaoHb implements PcDao{
    private static PcDaoHb instance;
    
    public static PcDaoHb getInstance(){
        if(instance == null){
            instance = new PcDaoHb();
        }
        return instance;
    }
    
    private PcDaoHb(){}
    
    @Override
    public List<NarridoPc> getAllPcs() {
        return NarridoGeneric.getList(NarridoPc.class);
    }

    @Override
    public NarridoPc getPc(String pcName) {
        Transaction tx = null;
        NarridoPc pc = null;
        try(Session sess = SFH.getSF().openSession()){
            tx = sess.beginTransaction();
            Criteria c = sess.createCriteria(NarridoPc.class);
            Criterion cr = Restrictions.eq("pcNumber", pcName);
            c.add(cr);
            pc = (NarridoPc)c.list().get(0);
            
            tx.commit();
            sess.close();
        }catch(HibernateException he){
            he.printStackTrace(System.err);
            if(tx != null) tx.rollback();
            throw he;
        }
        return pc;
    }

    @Override
    public List<NarridoPc> getMrPc(String mr, Date date) {
        List<NarridoPc> pcs = null;
        Session sess = null;
        Transaction tx = null;
        
        try {
            sess = SFH.getSF().openSession();
            tx = sess.beginTransaction();
            CriteriaBuilder cb = sess.getCriteriaBuilder();
            CriteriaQuery<NarridoPc> cq = cb.createQuery(NarridoPc.class);
            
            Root<NarridoPc> grp = cq.from(NarridoPc.class);
            cq.where(
                    cb.and(
                            cb.equal(grp.get(NarridoPc_.reMr), mr)
                    )
            );
            
            TypedQuery<NarridoPc> tq = sess.createQuery(cq);
            pcs = tq.getResultList();
            tx.commit();
        } catch (HibernateException e) {
            e.printStackTrace(System.err);
            if(tx != null) tx.rollback();
        } finally {
            sess.close();
        }
        
        return pcs;
    }
    
}
