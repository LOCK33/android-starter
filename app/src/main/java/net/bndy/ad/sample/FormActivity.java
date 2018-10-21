package net.bndy.ad.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import net.bndy.ad.R;
import net.bndy.ad.framework.BaseActivity;

import org.xutils.view.annotation.Event;
import org.xutils.x;

public class FormActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        x.view().inject(this);
    }

    @Event(R.id.form_submit1)
    private void onSubmit(View view) {
        if (checkRequired(R.id.form_input1, R.string.required)) {
            info("You have typed '" + ((EditText) findViewById(R.id.form_input1)).getText().toString() + "'.");
        }
    }
}
