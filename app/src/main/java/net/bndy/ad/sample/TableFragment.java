package net.bndy.ad.sample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.bndy.ad.R;
import net.bndy.ad.framework.BaseFragment;
import net.bndy.ad.framework.ui.TableView;
import net.bndy.ad.framework.ui.table.AdvanceTableAdapter;
import net.bndy.ad.framework.ui.table.AdvanceTableColumnDefinition;
import net.bndy.ad.model.AppUser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TableFragment extends BaseFragment {

    private boolean selectedAll;
    private Context mContext;
    private TableView tableView;
    private List<String> selectedData = new ArrayList<>();
    private final List<AppUser> data = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);

        this.mContext = this.getContext();
        this.tableView = layout.findViewById(R.id.tableView);

        for (int i = 1; i < 1000; i++) {
            AppUser u = new AppUser();
            u.setName("Name" + i);
            u.setEmail("name" + i + "@bndy.net");
            data.add(u);
        }

        final AdvanceTableAdapter<AppUser> adapter = new AdvanceTableAdapter<>(this.mContext, data,
                new AdvanceTableColumnDefinition().setWidth(40).setHeaderFormatter(new AdvanceTableAdapter.CellFormatter<String>() {
                    @Override
                    public View format(String o) {
                        LinearLayout container = new LinearLayout(mContext);
                        container.setOrientation(LinearLayout.HORIZONTAL);
                        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        container.setLayoutParams(layoutParams);
                        CheckBox cb = new CheckBox(mContext);
                        cb.setChecked(selectedAll);
                        cb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean checked = ((CheckBox) v).isChecked();
                                if (checked) {
                                    for (AppUser au : data) {
                                        if (!selectedData.contains(au.getName())) {
                                            selectedData.add(au.getName());
                                        }
                                    }
                                } else {
                                    selectedData.clear();
                                }
                                selectedAll = checked;
                                tableView.getDataAdapter().notifyDataSetChanged();
                            }
                        });
                        container.addView(cb);

                        TextView tvText = new TextView(mContext);
                        container.addView(tvText);

                        return container;
                    }
                }).setCellFormatter(new AdvanceTableAdapter.CellFormatter<AppUser>() {
                    @Override
                    public View format(final AppUser o) {
                        CheckBox cb = new CheckBox(mContext);
                        cb.setChecked(selectedData.contains(o.getName()));
                        cb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CheckBox vv = (CheckBox) v;
                                if (vv.isChecked()) {
                                    if (!selectedData.contains(o.getName())) {
                                        selectedData.add(o.getName());
                                    }
                                } else {
                                    if (selectedData.contains(o.getName())) {
                                        selectedData.remove(o.getName());
                                    }
                                }
                                Toast.makeText(v.getContext(), "You click " + o.getName() + ", total " + selectedData.size(), Toast.LENGTH_LONG);
                            }
                        });
                        return cb;
                    }
                }),
                new AdvanceTableColumnDefinition<AppUser>().setSortable(true).setWidth(80).setSortComparator(new Comparator<AppUser>() {
                    @Override
                    public int compare(AppUser o1, AppUser o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                }).setHeader("Name").setCellFormatter(new AdvanceTableAdapter.CellFormatter<AppUser>() {
                    @Override
                    public View format(AppUser o) {
                        TextView tv = new TextView(mContext);
                        tv.setPadding(20, 0, 0, 0);
                        tv.setText(o.getName());
                        return tv;
                    }
                }),
                new AdvanceTableColumnDefinition<AppUser>().setHeader("Email").setWidth(140).setCellTextFormatter(new AdvanceTableAdapter.CellTextFormatter<AppUser>() {
                    @Override
                    public String[] format(AppUser o) {
                        return new String[]{o.getEmail()};
                    }
                }),
                new AdvanceTableColumnDefinition<AppUser>().setHeader("Name & Email").setCellTextFormatter(new AdvanceTableAdapter.CellTextFormatter<AppUser>() {
                    @Override
                    public String[] format(AppUser o) {
                        return new String[]{o.getName(), o.getEmail()};
                    }
                })

        );

        adapter.setRowClickListener(new AdvanceTableAdapter.RowClickListener<AppUser>() {
            @Override
            public void onRowClick(int rowIndex, AppUser clickedData) {
                Toast.makeText(mContext, clickedData.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setRowLongClickListener(new AdvanceTableAdapter.RowLongClickListener<AppUser>() {
            @Override
            public boolean onRowLongClick(int rowIndex, AppUser clickedData) {
                Toast.makeText(mContext, "Long click:" + clickedData.getName(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        tableView.setAdapter(adapter);

        return layout;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_table;
    }
}
