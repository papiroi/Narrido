/*
 * Copyright 2017 princessmelisa.
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
package com.mycompany.narrido;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author princessmelisa
 */
@Path("/aaccup")
public class NarridoAaccupResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response aaccupData() {
        final AaccupData data = new AaccupData();
        
        data.setMeanWorkingPcsPerLab(19.91D);
        
        data.setMeanStudentAccessHours(19.23D);
        data.setMeanPcDowntime(2.45D);
        
        data.setNumPcsWithIssues(20);
        data.setNumPcsWithoutIssues(50);
        data.setMeanWaitingTime(20.44D);
        
        data.setYoungPcs(60);
        data.setOldPcs(10);
        
        data.setMeanVisitsPerDay(2.5D);
        
        return Response.ok(data).build();
    }
}

class AaccupData {
    //A S6
    private Double meanWorkingPcsPerLab;
    
    //A I7
    private Double meanStudentAccessHours;
    private Double meanPcDowntime;
    
    //B I1
    private Integer numPcsWithoutIssues;
    private Integer numPcsWithIssues;
    private Double meanWaitingTime;
    
    //B I3
    private Integer youngPcs;
    private Integer oldPcs;
    
    //C I.7.2
    private Double meanVisitsPerDay;

    public AaccupData() {
    }

    public Double getMeanWorkingPcsPerLab() {
        return meanWorkingPcsPerLab;
    }

    public void setMeanWorkingPcsPerLab(Double meanWorkingPcsPerLab) {
        this.meanWorkingPcsPerLab = meanWorkingPcsPerLab;
    }

    public Double getMeanStudentAccessHours() {
        return meanStudentAccessHours;
    }

    public void setMeanStudentAccessHours(Double meanStudentAccessHours) {
        this.meanStudentAccessHours = meanStudentAccessHours;
    }

    public Double getMeanPcDowntime() {
        return meanPcDowntime;
    }

    public void setMeanPcDowntime(Double meanPcDowntime) {
        this.meanPcDowntime = meanPcDowntime;
    }

    public Integer getNumPcsWithoutIssues() {
        return numPcsWithoutIssues;
    }

    public void setNumPcsWithoutIssues(Integer numPcsWithoutIssues) {
        this.numPcsWithoutIssues = numPcsWithoutIssues;
    }

    public Integer getNumPcsWithIssues() {
        return numPcsWithIssues;
    }

    public void setNumPcsWithIssues(Integer numPcsWithIssues) {
        this.numPcsWithIssues = numPcsWithIssues;
    }

    public Double getMeanWaitingTime() {
        return meanWaitingTime;
    }

    public void setMeanWaitingTime(Double meanWaitingTime) {
        this.meanWaitingTime = meanWaitingTime;
    }

    public Integer getYoungPcs() {
        return youngPcs;
    }

    public void setYoungPcs(Integer youngPcs) {
        this.youngPcs = youngPcs;
    }

    public Integer getOldPcs() {
        return oldPcs;
    }

    public void setOldPcs(Integer oldPcs) {
        this.oldPcs = oldPcs;
    }

    public Double getMeanVisitsPerDay() {
        return meanVisitsPerDay;
    }

    public void setMeanVisitsPerDay(Double meanVisitsPerDay) {
        this.meanVisitsPerDay = meanVisitsPerDay;
    }
    
    
}