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

public class UserRoleTM {
    private long id;
    private String roleName;
    private String description;
    private Button deleteButton;
    private Button updateButton;
}
