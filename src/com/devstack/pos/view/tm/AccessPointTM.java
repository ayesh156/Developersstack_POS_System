package com.devstack.pos.view.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccessPointTM {
    private long Id;
    private String pointName;
    private String operation;
    private Button deleteButton;
    private Button updateButton;
}
