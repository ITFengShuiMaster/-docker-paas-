package cn.edu.jit.tianyu_paas.im.message;

public class AuthenticationMessage extends MinaMessage {
    private String username;
    private String password;

    public AuthenticationMessage() {
        super.setMessageType(MinaMessageType.AUTHENTICATION);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
