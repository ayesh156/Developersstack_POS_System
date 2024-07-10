package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.AccessPointBo;
import com.devstack.pos.dto.AccessPointCrudDto;
import com.devstack.pos.dto.AccessPointDto;
import com.devstack.pos.view.tm.AccessPrivilegesTM;
import com.devstack.pos.view.tm.SystemUserTM;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
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
import java.util.List;
import java.util.Optional;

public class ManagePrivilegesFormController {
    public AnchorPane privilegesContext;
    public JFXComboBox cmbAccessPoint;
    public JFXCheckBox rBtnCreate;
    public JFXCheckBox rBtnRead;
    public JFXCheckBox rBtnUpdate;
    public JFXCheckBox rBtnDelete;
    public TableColumn colAccessPoint;
    public TableColumn colId;
    public TableView tblAccessPrivileges;
    public TableColumn colOperation;

    private AccessPointBo accessPointBo = BoFactory.getBo(BoFactory.BoType.ACCESS_POINT);
    List<AccessPointDto> accessPointDtos = accessPointBo.loadAll();
    public void initialize(){
         colId.setCellValueFactory(new PropertyValueFactory<>("id"));
         colAccessPoint.setCellValueFactory(new PropertyValueFactory<>("accessPointName"));
         colOperation.setCellValueFactory(new PropertyValueFactory<>("operation"));
        loadAccessPoints();
        loadTable();
    }

    private void loadAccessPoints() {

        ObservableList<String> obList = FXCollections.observableArrayList();
        for (AccessPointDto dto: accessPointDtos
             ) {
            obList.add(dto.getPointName());
        }
        cmbAccessPoint.setItems(obList);

    }

    private void loadTable() {
        ObservableList<AccessPrivilegesTM> obList = FXCollections.observableArrayList();

        for (AccessPointDto dto : accessPointDtos) {
            for (AccessPointCrudDto cd : dto.getCruds()) {
                CheckBox checkBox = new CheckBox(cd.getCrud().name());
                checkBox.setSelected(true);

                AccessPrivilegesTM tm = new AccessPrivilegesTM(
                        cd.getPropertyId(),
                        dto.getPointName(),
                        checkBox
                );
                obList.add(tm);

                checkBox.setOnAction(e -> {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.isPresent() && buttonType.get() == ButtonType.YES) {
                        if (accessPointBo.dropPrivilage(tm.getId())) {
                            obList.remove(tm); // Remove the item from the ObservableList
                            tblAccessPrivileges.refresh(); // Refresh the TableView
                        }
                    }
                });
            }
        }

        tblAccessPrivileges.setItems(obList);
        tblAccessPrivileges.refresh(); // Ensure that the TableView is refreshed after setting the items
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("UserRolesAndAuthoritiesForm");
    }
    private void setUi(String location) throws IOException {
        Stage stage = (Stage) privilegesContext.getScene().getWindow();
        stage.setScene(
                new Scene(
                        FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))
                )
        );
        stage.centerOnScreen();
    }

    public void submitData(ActionEvent actionEvent) {

        Optional<AccessPointDto> first = accessPointDtos.stream().filter(e -> e.getPointName().equals(cmbAccessPoint.getValue())).findFirst();
        if (first.isPresent()) {
            accessPointBo.setPrivileges(
                    first.get().getPropertyId(),
                    rBtnCreate.isSelected(),
                    rBtnRead.isSelected(),
                    rBtnUpdate.isSelected(),
                    rBtnDelete.isSelected()
            );

            // Reload the access point data
            accessPointDtos = accessPointBo.loadAll();

            // Reload the access points combo box
            loadAccessPoints();

            // Reload and add newly added data to the table
            loadTable();

            clearAll();
        }

    }

    private void clearAll(){
        cmbAccessPoint.setValue(null);
        rBtnCreate.setSelected(false);
        rBtnRead.setSelected(false);
        rBtnUpdate.setSelected(false);
        rBtnDelete.setSelected(false);
    }
}
