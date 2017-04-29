/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.dao.ice;

import com.mycompany.narrido.pojo.NarridoAccessCode;
import com.mycompany.narrido.pojo.NarridoGroup;
import java.util.List;

/**
 *
 * @author princessmelisa
 */
public interface AccessCodeDao {
    public List<NarridoAccessCode> getCodes();
    public void newCode(NarridoGroup group, String type);
}
