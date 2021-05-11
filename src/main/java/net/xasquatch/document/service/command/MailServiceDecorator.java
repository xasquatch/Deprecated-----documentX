package net.xasquatch.document.service.command;

public abstract class MailServiceDecorator {

    protected String email, title, contents;


    protected abstract void setEmail(String email);

    protected abstract void setTitle(String title);

    protected abstract void setContents(String contents);

    protected abstract void send();

    public void sendAuthMail(String email, String title, String contents) {
        setEmail(email);
        setTitle(title);
        setContents(contents);
        send();

    }
}
