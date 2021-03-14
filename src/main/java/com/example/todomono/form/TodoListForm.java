package com.example.todomono.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TodoListForm {

    @NotNull
    @NotEmpty(message = "Field required")
    @Size(min = 2, max = 50, message = "min")
    private String title;

    private long num;

    public TodoListForm(@NotNull @NotEmpty(message = "Field required") String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

}
