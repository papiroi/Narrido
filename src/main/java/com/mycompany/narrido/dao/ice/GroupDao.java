/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.dao.ice;

import com.mycompany.narrido.pojo.NarridoAccessCode;
import com.mycompany.narrido.pojo.NarridoFile;
import com.mycompany.narrido.pojo.NarridoGroup;
import com.mycompany.narrido.pojo.NarridoMembership;
import com.mycompany.narrido.pojo.NarridoPost;
import java.util.List;

/**
 *
 * @author princessmelisa
 */
public interface GroupDao {
    public NarridoGroup group(Integer groupId);
    public List<NarridoMembership> membership(NarridoGroup group);
    public List<NarridoAccessCode> accessCode(NarridoGroup group);
    public List<NarridoPost> posts(NarridoGroup group);
}
