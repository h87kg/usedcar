package com.jason.usedcar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jason.usedcar.fragment.BaseDialogFragment;
import com.jason.usedcar.interfaces.IJobListener;
import com.jason.usedcar.model.param.LoginParam;
import com.jason.usedcar.util.HttpUtil;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Required;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends ActionBarActivity implements OnClickListener, ValidationListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Required(order = 1)
    private EditText editAccount;

    @Password(order = 2)
    private EditText editPassword;

    private boolean mResult = false;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editAccount = (EditText) findViewById(R.id.login_account);
        editPassword = (EditText) findViewById(R.id.login_password);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_login:
                validator.validate();
                break;
            case R.id.login_register:
                register();
                break;
            case R.id.login_reset_password:
                resetPassword();
                break;
        }
    }

    private void login() {
        final String account = String.valueOf(editAccount.getText());
        final String password = String.valueOf(editPassword.getText());
        Log.d(TAG, " HttpUtil.LOGIN_URI:" + HttpUtil.LOGIN_URI);
        final LoginParam param = new LoginParam();
        param.setPhoneOrEmail(account);
        param.setPassword(password);
        StringRequest request = new StringRequest(Method.POST, HttpUtil.LOGIN_URI,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "response: " + response);
                    mResult = true;
                    //                    Gson gson = new Gson();
                    //                    Result result = gson.fromJson(response, Result.class);
                    if (mIJobListener != null) {
                        mIJobListener.executionDone();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mResult = true;
                    Log.d(TAG, error.toString());
                    if (mIJobListener != null) {
                        mIJobListener.executionDone();
                    }
                }
            }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return object2Map(param);
            }

            private Map<String, String> object2Map(Object obj) {
                return object2Map(obj, false);
            }

            private Map<String, String> object2Map(Object obj, boolean nullable) {
                Map<String, String> result = new HashMap<String, String>();
                if (obj == null) {
                    return result;
                }
                java.lang.reflect.Method[] methods = obj.getClass().getMethods();
                if (methods == null || methods.length == 0) {
                    return result;
                }
                try {
                    for (java.lang.reflect.Method method : methods) {
                        String methodName = method.getName();
                        if (methodName != null && methodName.length() > 3
                            && methodName.startsWith("get") && !methodName.equals("getClass")) {
                            Object value = method.invoke(obj, null);
                            if (!nullable && value == null) {
                                continue;
                            }
                            char[] fieldNameArray = methodName.substring(3).toCharArray();
                            fieldNameArray[0] = Character.toLowerCase(fieldNameArray[0]);
                            result.put(String.valueOf(fieldNameArray),
                                (value == null) ? "" : String.valueOf(value));
                        }
                    }
                } catch (InvocationTargetException e) {
                    result.clear();
                } catch (IllegalAccessException e) {
                    result.clear();
                }
                return result;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
    private void register() {
        startActivity(new Intent(this, RegisterActivity.class));
    }
    private void resetPassword() {
        startActivity(new Intent(this, ResetPasswordActivity.class));
    }

    // bellow code is for test:
    public boolean getResult() {
        return mResult;
    }

    private IJobListener mIJobListener;

    public void setListener(IJobListener listener) {
        mIJobListener = listener;
    }

    @Override
    public void onValidationSucceeded() {
        login();
    }

    @Override
    public void onValidationFailed(View view, Rule<?> rule) {
        switch (view.getId()) {
            case R.id.login_account:
                BaseDialogFragment.newInstance(getString(R.string.error_input_account)).show(getSupportFragmentManager());
                break;
            case R.id.login_password:
                BaseDialogFragment.newInstance(getString(R.string.error_input_password)).show(getSupportFragmentManager());
                break;
        }
    }
}
