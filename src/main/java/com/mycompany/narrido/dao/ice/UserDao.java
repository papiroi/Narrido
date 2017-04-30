/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.dao.ice;

import com.mycompany.narrido.pojo.NarridoFile;
import com.mycompany.narrido.pojo.NarridoGroup;
import com.mycompany.narrido.pojo.NarridoJob;
import com.mycompany.narrido.pojo.NarridoUser;
import java.util.List;

/**
 *
 * @author princessmelisa
 */
public interface UserDao {
    public void saveUser(NarridoUser user);
    public List<NarridoUser> users();
    public NarridoUser user(Integer idNumber);
    public List<NarridoGroup> ownedGroups(NarridoUser user);
    public List<NarridoGroup> joinedGroups(NarridoUser user);
    public List<NarridoFile> myFiles(NarridoUser user);
    public List<NarridoJob> mySubmittedJobs(NarridoUser user, String status);
}
