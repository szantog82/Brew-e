package com.szantog.brew_e;

public class LoginResult {

    private int success;
    private String session_id;

    public LoginResult(int success, String session_id) {
        this.success = success;
        this.session_id = session_id;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
}
