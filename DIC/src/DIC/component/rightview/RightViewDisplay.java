package DIC.component.rightview;

import java.awt.*;

/**
 * Created by Arnab Saha on 10/11/15.
 */

public interface RightViewDisplay {
    public void init(String defaultTableName, Component component);

    public void addTab(String title, Component component,boolean closeButton);

    public boolean removeTab(int tabIndex);

    public void refresh(String tabName, int index, Component component);
}
