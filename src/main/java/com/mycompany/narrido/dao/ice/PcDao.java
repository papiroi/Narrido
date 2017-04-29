/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.narrido.dao.ice;

import com.mycompany.narrido.pojo.NarridoPc;
import java.util.List;

/**
 *
 * @author princessmelisa
 */
public interface PcDao {
    List<NarridoPc> getAllPcs();
    NarridoPc getPc(String pcName);
}
