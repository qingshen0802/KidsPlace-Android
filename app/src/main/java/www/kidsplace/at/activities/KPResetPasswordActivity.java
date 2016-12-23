package www.kidsplace.at.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import www.kidsplace.at.R;

public class KPResetPasswordActivity extends KPBaseActivity {

    @BindView(R.id.activity_reset_password_et_password)
    EditText etPassword;
    @BindView(R.id.activity_reset_password_et_confirm)
    EditText etConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
    }

    private void resetPasswordSuccess() {
        Intent returnIntent = new Intent();
        String email = getIntent().getExtras().getString("email");
        String password = etPassword.getText().toString();
        returnIntent.putExtra("email", email);
        returnIntent.putExtra("password", password);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private boolean checkPassowrd() {
        String password = etPassword.getText().toString();
        String confirm = etConfirm.getText().toString();

        if (password.isEmpty() || !password.equals(confirm)) {
            return false;
        }

        return true;
    }

    @OnClick(R.id.activity_reset_password_bt_back)
    public void onBack() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @OnClick(R.id.activity_reset_password_bt_check)
    public void onCheck() {
        if (checkPassowrd())
            resetPasswordSuccess();
    }

}
