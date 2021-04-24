package com.example.todomono.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TodoForm {

    @NotNull
    @NotEmpty(message = "Field required")
    @Size(min = 2, max = 50, message = "min")
    private String label;

    private int num;

    private boolean done;

    public TodoForm(@NotNull @NotEmpty(message = "Field required") String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}
