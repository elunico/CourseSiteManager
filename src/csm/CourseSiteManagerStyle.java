package csm;

import csm.tam.style.TAMStyle;
import djf.components.AppStyleComponent;

/**
 * @author Thomas Povinelli
 *         Created 4/5/17
 *         In Homework4
 */
public class CourseSiteManagerStyle extends AppStyleComponent {

    public TAMStyle taStyle;

    public CourseSiteManagerStyle(CourseSiteManagerApp courseSiteManagerApp) {
        taStyle = new TAMStyle(courseSiteManagerApp);
    }
}
