package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.UserBo;
import com.devstack.pos.bo.custom.UserRoleBo;
import com.devstack.pos.dto.UserDto;
import com.devstack.pos.dto.UserRoleDto;
import com.devstack.pos.view.tm.SystemUserTM;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NewSystemUserFormController {
    public AnchorPane newSystemUserContext;
    public JFXComboBox<String> cmbUserRole;
    public JFXTextField txtUsername;
    public JFXTextField txtDisplayName;
    public TableView<SystemUserTM> tblUsers;
    public TableColumn<SystemUserTM,Long> colId;
    public TableColumn<SystemUserTM,String> colUserRole;
    public TableColumn<SystemUserTM,String> colStatus;
    public TableColumn<SystemUserTM,String> colDisplayName;
    public TableColumn<SystemUserTM,String> colEmail;
    public TableColumn<SystemUserTM, Button> colDelete;
    public TableColumn<SystemUserTM,Button> colModify;
    public TextField txtSearchText;
    public JFXButton btnNewData;
    public JFXButton btnNewSystemUser;

    private UserBo userBo = BoFactory.getBo(BoFactory.BoType.USER);

    private UserRoleBo userRoleBo = BoFactory.getBo(BoFactory.BoType.USER_ROLE);
    private ObservableList<String> observableList = FXCollections.observableArrayList();
    private ObservableList<SystemUserTM> systemUserTMS = FXCollections.observableArrayList();
    private  List<UserRoleDto> userRoleDtos = new ArrayList<>();

    private String searchText = "";

    private Long selectedUserId;

    public void initialize(){

        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUserRole.setCellValueFactory(new PropertyValueFactory<>("userRole"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colDisplayName.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));
        colModify.setCellValueFactory(new PropertyValueFactory<>("modify"));
        
        loadAllUserRoles();
        loadAllSystemUser();

        txtSearchText.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue;
            loadAllSystemUser();
        });

    }

    private void loadAllSystemUser() {
        systemUserTMS.clear();
        for (UserDto userDto: userBo.loadAllUsers(searchText)
             ) {

            Button deleteButton = new Button("Delete");
            Button updateButton = new Button("Update");

            SystemUserTM tm = new SystemUserTM(
                    userDto.getPropertyId(),
                    userDto.getUserRoleDto().getRoleName(),
                    userDto.isActiveState()?"Active":"Disabled",
                    userDto.getDisplayName(),
                    userDto.getUsername(),
                    deleteButton,
                    updateButton
            );
            systemUserTMS.add(tm);

            updateButton.setOnAction(e->{
                Alert alert = new Alert(Alert.AlertType.WARNING, "Do you need to update this data?",
                        ButtonType.APPLY, ButtonType.CLOSE);
                Optional<ButtonType> buttonType = alert.showAndWait();
                if(buttonType.get()==ButtonType.APPLY){
                    cmbUserRole.setValue(tm.getUserRole());
                    txtUsername.setText(tm.getEmail());
                    txtDisplayName.setText(tm.getDisplayName());
                    selectedUserId = tm.getUserId();
                    btnNewData.setText("Update Data");
                    btnNewData.setStyle("-fx-background-color: #16a085");
                }
            });

            deleteButton.setOnAction(e->{
                Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> buttonType = alert.showAndWait();
                if(buttonType.get()==ButtonType.YES){
                    if(userBo.dropUser(tm.getUserId())){
                        loadAllSystemUser();
                    }
                }
            });

        }
        tblUsers.setItems(systemUserTMS);
    }

    private void loadAllUserRoles() {
        userRoleDtos = userRoleBo.loadAllUserRoles();
        for (UserRoleDto dto : userRoleDtos
             ) {
            observableList.add(dto.getRoleName());

        }
        cmbUserRole.setItems(observableList);
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("UserManagementForm");
    }

    private void setUi(String location) throws IOException {
        Stage stage = (Stage) newSystemUserContext.getScene().getWindow();
        stage.setScene(
                new Scene(
                        FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))
                )
        );
        stage.centerOnScreen();
    }

    public void createSystemUser(ActionEvent actionEvent) {

        if(btnNewData.getText().equalsIgnoreCase("Submit Data")){
            String userRole = cmbUserRole.getValue();
            Optional<UserRoleDto> selectedUserRoleDto = userRoleDtos.stream().filter(e -> e.getRoleName().equals(userRole)).findFirst();
            String displayName = txtDisplayName.getText();
            String userName = txtUsername.getText();

            userBo.createNewSystemUser(
                    selectedUserRoleDto.get().getPropertyId(),displayName,userName
            );
            loadAllSystemUser();
            clearAll();
        }else {
            // update
            if(selectedUserId!=null){
                String userRole = cmbUserRole.getValue();
                Optional<UserRoleDto> selectedUserRoleDto = userRoleDtos.stream().filter(e -> e.getRoleName().equals(userRole)).findFirst();
                String displayName = txtDisplayName.getText();
                String userName = txtUsername.getText();

                userBo.updateSystemUser(
                        selectedUserRoleDto.get().getPropertyId(),displayName,userName,selectedUserId
                );
                selectedUserId=null;
                loadAllSystemUser();
                clearAll();
                newSystemUser(null);
            } else {
                clearAll();
                btnNewData.setText("Submit Data");
                btnNewData.setStyle("-fx-background-color: #3498db");
            }
        }


    }

    private void clearAll(){
        cmbUserRole.setValue(null);
        txtUsername.clear();
        txtDisplayName.clear();
    }

    public void newSystemUser(ActionEvent actionEvent) {
        clearAll();
        btnNewData.setText("Submit Data");
        btnNewData.setStyle("-fx-background-color: #3498db");
    }
}
