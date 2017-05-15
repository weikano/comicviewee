package com.wkswind.comicviewer.utils;

/**
 * Created by Administrator on 2017/5/15.
 */

public class UIEvent {
    private static final int IDLE = 0x0;
    private static final int LOADING = 0x1;
    private static final int COMPLETE = 0x2;
    private static final int FAIL = 0x3;

    private Throwable exception;
    private Object response;
    private int status;

    private UIEvent(int status) {
        this.status = status;
    }

    public Throwable getException() {
        return exception;
    }

    private void setException(Throwable exception) {
        this.exception = exception;
    }

    public Object getResponse() {
        return response;
    }

    private void setResponse(Object response) {
        this.response = response;
    }

    public static UIEvent ofIdle() {
        return new UIEvent(IDLE);
    }

    public static UIEvent ofLoading(){
        return new UIEvent(LOADING);
    }

    public static UIEvent ofError(Throwable throwable) {
        UIEvent event = new UIEvent(FAIL);
        event.setException(throwable);
        return event;
    }

    public static UIEvent ofResponse(Object response) {
        UIEvent event = new UIEvent(COMPLETE);
        event.setResponse(response);
        return event;
    }

    public boolean loading() {
        return status == LOADING;
    }

    public boolean error() {
        return status == FAIL && exception != null;
    }

    public boolean complete() {
        return status == COMPLETE && response != null;
    }
}
