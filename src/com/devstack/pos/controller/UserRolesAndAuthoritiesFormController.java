package com.devstack.pos.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class UserRolesAndAuthoritiesFormController {
    public AnchorPane userRolesAndAuthorityContext;

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("UserManagementForm");
    }

    private void setUi(String location) throws IOException {
        Stage stage = (Stage) userRolesAndAuthorityContext.getScene().getWindow();
        stage.setScene(
                new Scene(
                        FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))
                )
        );
        stage.centerOnScreen();
    }


    public void manageUserRolesOnAction(MouseEvent mouseEvent) throws IOException {
        setUi("ManageUserRoleForm");
    }

    public void manageAccessPointsOnAction(MouseEvent mouseEvent) throws IOException {
        setUi("ManageAccessPointsForm");

    }

    public void authoritiesOnAction(MouseEvent mouseEvent) throws IOException {
        setUi("ManageAuthoritiesForm");
    }

    public void privilegesOnAction(MouseEvent mouseEvent) throws IOException {
        setUi("ManagePrivilegesForm");
    }
}
