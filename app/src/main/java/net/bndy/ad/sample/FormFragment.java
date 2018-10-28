package net.bndy.ad.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import net.bndy.ad.R;
import net.bndy.ad.framework.BaseFragment;

public class FormFragment extends BaseFragment {

    private Activity mActivity;
    private Button mButtonSubmit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);
        mActivity = this.getActivity();
        mButtonSubmit = layout.findViewById(R.id.form_submit1);
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (utils.checkRequired(mActivity, R.id.form_input1, R.string.required)) {
                    utils.info("You have typed '" + ((EditText) getActivity().findViewById(R.id.form_input1)).getText().toString() + "'.");
                }
            }
        });
        return layout;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_form;
    }
}
