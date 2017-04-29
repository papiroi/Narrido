/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.dao;

import com.mycompany.narrido.dao.ice.AccessCodeDao;
import com.mycompany.narrido.helper.NarridoCode;
import com.mycompany.narrido.helper.NarridoGeneric;
import com.mycompany.narrido.pojo.NarridoAccessCode;
import com.mycompany.narrido.pojo.NarridoGroup;
import java.util.List;

/**
 *
 * @author princessmelisa
 */
public final class AccessCodeDaoHb implements AccessCodeDao{
    private static AccessCodeDao instance;
    
    public static AccessCodeDao getInstance(){
        if(instance == null){
            instance = new AccessCodeDaoHb();
        }
        return instance;
    }

    @Override
    public List<NarridoAccessCode> getCodes() {
        return NarridoGeneric.getList(NarridoAccessCode.class);
    }

    @Override
    public void newCode(NarridoGroup group, String type) {
        NarridoAccessCode code = new NarridoAccessCode();
        
        code.setType(type);
        code.setAccessCode(NarridoCode.getCode());
        code.setGroup(group);
        
        NarridoGeneric.saveThing(code);
        
    }
    
}
