/*
 * Copyright 2017 Dell.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mycompany.narrido.helper;

import com.mycompany.narrido.pojo.NarridoLaboratory;
import com.mycompany.narrido.pojo.NarridoPc;
import com.mycompany.narrido.pojo.NarridoPc_;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author Dell
 */
public final class Aaccup {
    private Aaccup() {}
    
    public static Double averageWorkingPerLab() {
        List<NarridoLaboratory> labs = NarridoGeneric.getList(NarridoLaboratory.class);
        Double totalWorkingPcs = (double) NarridoGeneric.getList(NarridoPc.class, NarridoPc_.status, "WORKING").size();
        
        return totalWorkingPcs / labs.size();
    }
    
    public static Double percentPcWorking() {
        List<NarridoPc> pcs = NarridoGeneric.getList(NarridoPc.class);
        
        Double working = 0D;
        
        for(NarridoPc pc : pcs) {
            if("WORKING".equals(pc.getStatus())) working++;
        }
        
        return working / pcs.size() * 100;
    }
    
    public static void main(String[] args) {
        Calendar cl = GregorianCalendar.getInstance();
        cl.setTimeInMillis(12345);
        
        System.out.println(cl.get(Calendar.HOUR));
        System.out.println(percentPcWorking());
    }
}
