package DIC.component.rightview;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Arnab Saha
 * Date: 8/22/13
 * Time: 10:51 AM
 * To change this template use File | Settings | File Templates.
 */
public interface RightViewDisplay {
    public void init(String defaultTableName, Component component);

    public void addTab(String title, Component component);

    public void removeTab(int tabIndex);

    public void refresh(String tabName, int index, Component component);
}
