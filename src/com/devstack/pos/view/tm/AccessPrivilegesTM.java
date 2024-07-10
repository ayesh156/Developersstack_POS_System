package com.devstack.pos.view.tm;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccessPrivilegesTM {
    private long id;
    private String accessPointName;
    private CheckBox operation;
}
