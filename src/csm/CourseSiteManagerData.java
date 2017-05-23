package csm;

import csm.cdm.data.CDMData;
import csm.pde.data.PDEData;
import csm.rde.data.RDEData;
import csm.sde.data.SDEData;
import csm.tam.data.TAData;
import djf.components.AppDataComponent;

/**
 * @author Thomas Povinelli
 *         Created 4/5/17
 *         In Homework4
 */
public class CourseSiteManagerData implements AppDataComponent {
    private TAData taData;
    private CDMData cdmData;
    private PDEData pdeData;
    private SDEData sdeData;
    private RDEData rdeData;

    public CourseSiteManagerData(CourseSiteManagerApp courseSiteManagerApp) {
        taData = new TAData(courseSiteManagerApp);
        cdmData = new CDMData(courseSiteManagerApp);
        pdeData = new PDEData(courseSiteManagerApp);
        sdeData = new SDEData(courseSiteManagerApp);
        rdeData = new RDEData(courseSiteManagerApp);
    }

    public TAData getTaData() {
        return taData;
    }

    public CDMData getCdmData() {
        return cdmData;
    }

    public RDEData getRdeData() {
        return rdeData;
    }

    public PDEData getPdeData() {
        return pdeData;
    }

    @Override
    public void resetData() {
        taData.resetData();
        cdmData.resetData();
        pdeData.resetData();
        sdeData.resetData();
        rdeData.resetData();
    }

    public SDEData getSdeData() {
        return sdeData;
    }
}
