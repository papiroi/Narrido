/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.dao;
import com.mycompany.narrido.dao.ice.PcDao;
import com.mycompany.narrido.helper.NarridoGeneric;
import com.mycompany.narrido.helper.SFH;
import com.mycompany.narrido.pojo.NarridoPc;
import java.util.List;
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
    
}
