package com.example.lab1.Modal;

public class chat {
    private String render, message;
    public chat() {
    }
    public chat(String render, String message) {
        this.render = render;
        this.message = message;
    }
    public String getRender() {
        return render;
    }

    public void setRender(String render) {
        this.render = render;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
