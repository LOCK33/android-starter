package net.bndy.ad.framework.ui.table;

import android.content.Context;

import java.util.Comparator;

public class AdvanceTableColumnDefinition<TRowData> {

    private Context context;
    private String header;
    private AdvanceTableAdapter.CellFormatter<String> headerFormatter;
    private boolean sortable;
    private int width;
    private int weight;
    private Comparator<TRowData> sortComparator;
    private AdvanceTableAdapter.CellFormatter<TRowData> cellFormatter;
    private AdvanceTableAdapter.CellTextFormatter<TRowData> cellTextFormatter;

    public Context getContext() {
        return context;
    }

    public AdvanceTableColumnDefinition setContext(Context context) {
        this.context = context;
        return this;
    }

    public String getHeader() {
        return header;
    }

    public AdvanceTableColumnDefinition setHeader(String header) {
        this.header = header;
        return this;
    }

    public boolean isSortable() {
        return sortable;
    }

    public AdvanceTableColumnDefinition setSortable(boolean sortable) {
        this.sortable = sortable;
        return this;
    }

    public Comparator<TRowData> getSortComparator() {
        return sortComparator;
    }

    public AdvanceTableColumnDefinition setSortComparator(Comparator<TRowData> sortComparator) {
        this.sortComparator = sortComparator;
        return this;
    }

    public AdvanceTableAdapter.CellFormatter<TRowData> getCellFormatter() {
        return cellFormatter;
    }

    public AdvanceTableColumnDefinition setCellFormatter(AdvanceTableAdapter.CellFormatter<TRowData> cellFormatter) {
        this.cellFormatter = cellFormatter;
        return this;
    }

    public AdvanceTableAdapter.CellFormatter<String> getHeaderFormatter() {
        return headerFormatter;
    }

    public AdvanceTableColumnDefinition setHeaderFormatter(AdvanceTableAdapter.CellFormatter<String> headerFormatter) {
        this.headerFormatter = headerFormatter;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public AdvanceTableColumnDefinition setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getWeight() {
        return weight;
    }

    public AdvanceTableColumnDefinition setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public AdvanceTableAdapter.CellTextFormatter<TRowData> getCellTextFormatter() {
        return cellTextFormatter;
    }

    public AdvanceTableColumnDefinition setCellTextFormatter(AdvanceTableAdapter.CellTextFormatter<TRowData> cellTextFormatter) {
        this.cellTextFormatter = cellTextFormatter;
        return this;
    }
}
