package com.example.todomono.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TodoForm {

    @NotNull
    @NotEmpty(message = "Field required")
    @Size(min = 2, max = 50, message = "min")
    private String label;

    private long num;

    public TodoForm(@NotNull @NotEmpty(message = "Field required") String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

}
