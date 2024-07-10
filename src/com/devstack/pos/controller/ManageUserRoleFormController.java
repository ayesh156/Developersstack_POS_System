package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.UserRoleBo;
import com.devstack.pos.dto.UserRoleDto;
import com.devstack.pos.util.KeyGenerator;
import com.devstack.pos.view.tm.UserRoleTM;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ManageUserRoleFormController {

    public AnchorPane manageUserRolesContext;
    public JFXTextField txtDescription;
    public JFXTextField txtUserRoleName;
    public TableView<UserRoleTM> tblUserRole;
    public TableColumn colId;
    public TableColumn colUserRole;
    public TableColumn colDel;
    public TableColumn colModify;
    public TableColumn colDescription;

    private UserRoleBo userRoleBo = BoFactory.getBo(BoFactory.BoType.USER_ROLE);

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUserRole.setCellValueFactory(new PropertyValueFactory<>("roleName"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDel.setCellValueFactory(new PropertyValueFactory<>("deleteButton"));
        colModify.setCellValueFactory(new PropertyValueFactory<>("updateButton"));
        loadAllUserRoles();
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("UserRolesAndAuthoritiesForm");
    }

    private void setUi(String location) throws IOException {
        Stage stage = (Stage) manageUserRolesContext.getScene().getWindow();
        stage.setScene(
                new Scene(
                        FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))
                )
        );
        stage.centerOnScreen();
    }

    public void btnSubmitDataOnAction(ActionEvent actionEvent) {
        userRoleBo.saveUserRole(new UserRoleDto(
                KeyGenerator.generateId(), txtUserRoleName.getText().trim().toUpperCase(), txtDescription.getText()
        ));
        loadAllUserRoles();
    }

    private void loadAllUserRoles() {
        ObservableList<UserRoleTM> tms = FXCollections.observableArrayList();
        List<UserRoleDto> userRoleDtos = userRoleBo.loadAllUserRoles();
        for (UserRoleDto dto:userRoleDtos
             ) {
            Button deleteButton = new Button("Delete");
            Button updateButton = new Button("Update");
            UserRoleTM tm = new UserRoleTM(
                    dto.getPropertyId(), dto.getRoleName(), dto.getRoleDescription(), deleteButton, updateButton
            );
    tms.add(tm);
        }
        tblUserRole.setItems(tms);
    }
}
