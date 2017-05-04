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

import com.mycompany.narrido.pojo.NarridoJob;
import com.mycompany.narrido.pojo.NarridoUser;
import java.util.Date;

/**
 *
 * @author Dell
 */
public class JobSummaryData {
    private String inCharge;
    private String pcNumber;
    private String reports;
    private Date dateReported;
    private String diagnostics;
    private String status;
    private String application;

    public JobSummaryData() {
    }
    
    public JobSummaryData(NarridoJob job) {
        NarridoUser user = job.getHandledBy();
        this.inCharge = user != null 
                ? (user.getFirstName() + " " + user.getLastName())
                : "N/A";
        
        this.pcNumber = job.getPc().getPcNumber();
        this.reports = job.getReport();
        this.dateReported = job.getDateReported();
        this.diagnostics = job.getFindings();
        this.status = job.getStatus();
        this.application = job.getAction();
    }

    public String getInCharge() {
        return inCharge;
    }

    public void setInCharge(String inCharge) {
        this.inCharge = inCharge;
    }

    public String getPcNumber() {
        return pcNumber;
    }

    public void setPcNumber(String pcNumber) {
        this.pcNumber = pcNumber;
    }

    public String getReports() {
        return reports;
    }

    public void setReports(String reports) {
        this.reports = reports;
    }

    public Date getDateReported() {
        return dateReported;
    }

    public void setDateReported(Date dateReported) {
        this.dateReported = dateReported;
    }

    public String getDiagnostics() {
        return diagnostics;
    }

    public void setDiagnostics(String diagnostics) {
        this.diagnostics = diagnostics;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
    
}
