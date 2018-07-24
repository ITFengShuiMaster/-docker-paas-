package cn.edu.jit.tianyu_paas.im.message;

public class AuthenticationMessage extends MinaMessage {
    private String username;
    private String paasword;

    public AuthenticationMessage() {
        super.setMessageType(MinaMessageType.AUTHENTICATION);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPaasword() {
        return paasword;
    }

    public void setPaasword(String paasword) {
        this.paasword = paasword;
    }
}
