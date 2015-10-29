package DIC.component.detailview;

import java.sql.Connection;

/**
 *
 * Created by Arnab Saha on 10/10/15.
 */
public class MapperEditorView extends SqlEditorView {
    public MapperEditorView(Connection connection) {
        super(connection);
    }

    @Override
    protected void executeSQL() {
        firePropertyChange("mapper", connection, editor.getText());
    }
}
