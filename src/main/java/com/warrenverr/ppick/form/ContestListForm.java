package com.warrenverr.ppick.form;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContestListForm {

    private String title;
    private String img;
    private String EndDate;

    public ContestListForm(String title, String img, String endDate) {
        this.title = title;
        this.img = img;
        EndDate = endDate;
    }
}
