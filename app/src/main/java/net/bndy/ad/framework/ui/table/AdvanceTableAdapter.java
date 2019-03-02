package net.bndy.ad.framework.ui.table;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.codecrafters.tableview.SortState;
import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableHeaderAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.listeners.TableDataLongClickListener;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.providers.SortStateViewProvider;

public class AdvanceTableAdapter<TRowData> {

    private static final int DEFAULT_COLUMN_WIDTH_IN_DP = 200;

    private Context context;
    private List<TRowData> data;
    private List<AdvanceTableColumnDefinition<TRowData>> AdvanceTableColumnDefinitions;
    private RowClickListener<TRowData> rowClickListener;
    private RowLongClickListener<TRowData> rowLongClickListener;
    private int iconSortable;
    private int iconSortedASC;
    private int iconSortedDESC;

    public AdvanceTableAdapter(Context context, List<TRowData> data, AdvanceTableColumnDefinition<TRowData>... AdvanceTableColumnDefinitions) {
        this.data = data;
        this.context = context;
        this.AdvanceTableColumnDefinitions = Arrays.asList(AdvanceTableColumnDefinitions);
        for(AdvanceTableColumnDefinition c: this.AdvanceTableColumnDefinitions) {
            c.setContext(this.context);
        }
    }

    public int getIconSortable() {
        return iconSortable;
    }

    public AdvanceTableAdapter setIconSortable(int iconSortable) {
        this.iconSortable = iconSortable;
        return this;
    }

    public int getIconSortedASC() {
        return iconSortedASC;
    }

    public AdvanceTableAdapter setIconSortedASC(int iconSortedASC) {
        this.iconSortedASC = iconSortedASC;
        return this;
    }

    public int getIconSortedDESC() {
        return iconSortedDESC;
    }

    public AdvanceTableAdapter setIconSortedDESC(int iconSortedDESC) {
        this.iconSortedDESC = iconSortedDESC;
        return this;
    }

    public void adapt(final SortableTableView tableView) {
        tableView.setColumnCount(this.AdvanceTableColumnDefinitions.size());

        // set the sort icons
        if (this.iconSortable > 0 && this.iconSortedASC > 0 && this.iconSortedDESC > 0) {
            tableView.setHeaderSortStateViewProvider(new SortStateViewProvider() {
                @Override
                public int getSortStateViewResource(SortState state) {
                    switch (state) {
                        case SORTABLE:
                            return iconSortable;
                        case SORTED_ASC:
                            return iconSortedASC;
                        case SORTED_DESC:
                            return iconSortedDESC;
                        default:
                            return 0;
                    }
                }
            });

        }

        // header settings
        boolean hasSetColumnWidth = false;
        boolean hasSetColumnWeight = false;
        TableColumnDpWidthModel columnDpModel = new TableColumnDpWidthModel(context,
                tableView.getColumnCount(),
                DEFAULT_COLUMN_WIDTH_IN_DP);
        TableColumnWeightModel columnWeightModel = new TableColumnWeightModel(tableView.getColumnCount());
        final List<String> headerTitles = new ArrayList<>();
        for(int i = 0; i<this.getAdvanceTableColumnDefinitions().size(); i++) {
            AdvanceTableColumnDefinition c = this.getAdvanceTableColumnDefinitions().get(i);
            headerTitles.add(c.getHeader());
            if (c.isSortable()) {
                tableView.setColumnComparator(i, c.getSortComparator());
            }

            if (c.getWidth() > 0) {
                hasSetColumnWidth = true;
                columnDpModel.setColumnWidth(i, c.getWidth());
            }

            if (c.getWeight() > 0) {
                hasSetColumnWeight = true;
                columnWeightModel.setColumnWeight(i, c.getWeight());
            }
        }

        if (hasSetColumnWeight || hasSetColumnWidth) {
            tableView.setColumnModel(hasSetColumnWidth ? columnDpModel : columnWeightModel);
        }

        // set header
        tableView.setHeaderAdapter(new CustomTableHeaderAdapter(this.context));

        // events
        if (this.rowClickListener != null) {
            tableView.addDataClickListener(new TableDataClickListener<TRowData>() {
                @Override
                public void onDataClicked(int rowIndex, TRowData clickedData) {
                    rowClickListener.onRowClick(rowIndex, clickedData);
                    tableView.getDataAdapter().notifyDataSetChanged();
                }
            });
        }

        if (this.rowLongClickListener != null) {
            tableView.addDataLongClickListener(new TableDataLongClickListener<TRowData>() {
                @Override
                public boolean onDataLongClicked(int rowIndex, TRowData clickedData) {
                    boolean result = rowLongClickListener.onRowLongClick(rowIndex, clickedData);
                    tableView.getDataAdapter().notifyDataSetChanged();
                    return result;
                }
            });
        }

        // set data
        tableView.setDataAdapter(new CustomTableDataAdapter(this.context, this.getData(), this, tableView));

    }

    public List<AdvanceTableColumnDefinition<TRowData>> getAdvanceTableColumnDefinitions() {
        return AdvanceTableColumnDefinitions;
    }

    public List<TRowData> getData() {
        return data;
    }

    public void setData(List<TRowData> data) {
        this.data = data;
    }

    public void notifyDataChanged(SortableTableView tableView) {
        tableView.getDataAdapter().notifyDataSetChanged();
    }

    public AdvanceTableAdapter setRowClickListener(RowClickListener<TRowData> listener) {
        this.rowClickListener = listener;
        return this;
    }

    public AdvanceTableAdapter setRowLongClickListener(RowLongClickListener<TRowData> listener) {
        this.rowLongClickListener = listener;
        return this;
    }

    @FunctionalInterface
    public interface CellFormatter<T> {
        View format(T t);
    }
    @FunctionalInterface
    public interface CellTextFormatter<T> {
        String format(T t);
    }

    @FunctionalInterface
    public interface RowClickListener<TRowData> {
        void onRowClick(int rowIndex, TRowData clickedData);
    }

    @FunctionalInterface
    public interface RowLongClickListener<TRowData> {
        boolean onRowLongClick(int rowIndex, TRowData clickedData);
    }

    public class CustomTableHeaderAdapter extends TableHeaderAdapter {

        public CustomTableHeaderAdapter(final Context context) {
            super(context);
        }

        @Override
        public View getHeaderView(int AdvanceTableColumnDefinitionIndex, ViewGroup parentView) {
            AdvanceTableColumnDefinition c = getAdvanceTableColumnDefinitions().get(AdvanceTableColumnDefinitionIndex);
            String headerText = c.getHeader();
            View headerView;
            if (c.getHeaderFormatter() == null) {
                TextView tv = new TextView(this.getContext());
                tv.setText(headerText);
                tv.setPadding(0,0,0,0);
                headerView = tv;
            } else {
                headerView = c.getHeaderFormatter().format(headerText);
            }
            return headerView;
        }
    }

    public class CustomTableDataAdapter extends TableDataAdapter<TRowData> {

        private AdvanceTableAdapter advanceTableAdapter;

        public CustomTableDataAdapter(final Context context, final List<TRowData> data, final AdvanceTableAdapter<TRowData> advanceTableAdapter, final TableView<TRowData> tableView) {
            super(context, data);
            this.advanceTableAdapter = advanceTableAdapter;
            tableView.addDataClickListener(new InternalDataClickListener());
            tableView.addDataLongClickListener(new InternalDataLongClickListener());
//            tableView.addDataClickListener(new TableDataClickListener<TRowData>() {
//                @Override
//                public void onDataClicked(int rowIndex, TRowData clickedData) {
//                    advanceTableAdapter.rowClickListener.onRowClick(rowIndex, clickedData);
//                    tableView.getDataAdapter().notifyDataSetChanged();
//                }
//            });
//            tableView.addDataLongClickListener(new TableDataLongClickListener<TRowData>() {
//                @Override
//                public boolean onDataLongClicked(int rowIndex, TRowData clickedData) {
//                    boolean result = advanceTableAdapter.rowLongClickListener.onRowLongClick(rowIndex, clickedData);
//                    tableView.getDataAdapter().notifyDataSetChanged();
//                    return result;
//                }
//            });
        }

        @Override
        public View getCellView(int rowIndex, int AdvanceTableColumnDefinitionIndex, ViewGroup parentView) {
            TRowData rowData = getData().get(rowIndex);
            AdvanceTableColumnDefinition c = getAdvanceTableColumnDefinitions().get(AdvanceTableColumnDefinitionIndex);
            if (c.getCellFormatter()!=null) {
                return c.getCellFormatter().format(rowData);
            } else {
                if (c.getCellTextFormatter() != null) {
                    TextView tv = new TextView(this.getContext());
                    tv.setText(c.getCellTextFormatter().format(rowData));
                    return tv;
                }
            }
            return null;
        }

        private class InternalDataLongClickListener implements TableDataLongClickListener<TRowData> {

            @Override
            public boolean onDataLongClicked(final int rowIndex, final TRowData clickedData) {
                notifyDataSetChanged();
                return true;
            }
        }

        private class InternalDataClickListener implements TableDataClickListener<TRowData> {

            @Override
            public void onDataClicked(final int rowIndex, final TRowData clickedData) {
                notifyDataSetChanged();
            }
        }
    }
}
