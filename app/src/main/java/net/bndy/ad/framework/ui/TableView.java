package net.bndy.ad.framework.ui;

import android.content.Context;
import android.util.AttributeSet;

import net.bndy.ad.framework.ui.table.AdvanceTableAdapter;

import de.codecrafters.tableview.SortableTableView;

public class TableView extends SortableTableView {
    public TableView(final Context context) {
        super(context, null);
    }

    public TableView(final Context context, final AttributeSet attributes) {
        super(context, attributes, android.R.attr.listViewStyle);
    }

    public void setAdapter(AdvanceTableAdapter adapter) {
        adapter.adapt(this);
    }
}
