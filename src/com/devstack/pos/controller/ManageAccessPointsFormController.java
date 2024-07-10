package com.devstack.pos.controller;

import com.devstack.pos.bo.BoFactory;
import com.devstack.pos.bo.custom.AccessPointBo;
import com.devstack.pos.dto.AccessPointCrudDto;
import com.devstack.pos.dto.AccessPointDto;
import com.devstack.pos.util.KeyGenerator;
import com.devstack.pos.view.tm.AccessPointTM;
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

public class ManageAccessPointsFormController {
    public AnchorPane manageAccessPointsContext;
    public JFXTextField txtAccessPoint;
    public TableView tblAccessPoints;
    public TableColumn colId;
    public TableColumn colAccessPoint;
    public TableColumn colOperation;
    public TableColumn colDelete;
    public TableColumn colModify;
    public TextField txtSearchText;

    private AccessPointBo accessPointBo = BoFactory.getBo(BoFactory.BoType.ACCESS_POINT);

    private String searchText = "";

    public void initialize(){

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAccessPoint.setCellValueFactory(new PropertyValueFactory<>("pointName"));
        colOperation.setCellValueFactory(new PropertyValueFactory<>("operation"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("deleteButton"));
        colModify.setCellValueFactory(new PropertyValueFactory<>("updateButton"));

        loadAllData();

        txtSearchText.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText=newValue;
            loadAllData();
        });
    }

    private void loadAllData() {
        List<AccessPointDto> accessPointDtos = accessPointBo.loadAllAccessPoint(searchText);
        ObservableList<AccessPointTM> tms = FXCollections.observableArrayList();
        for (AccessPointDto dto:accessPointDtos
             ) {
            Button delete = new Button("Delete");
            Button update = new Button("Update");

            ArrayList<String> array = new ArrayList<>();

            for (AccessPointCrudDto cd: dto.getCruds()
                 ) {
                array.add(cd.getCrud().name());
            }

            AccessPointTM accessPointTM = new AccessPointTM(
                    dto.getPropertyId(),
                    dto.getPointName(),
                    array.toString(),
                    delete,
                    update
            );
            tms.add(accessPointTM);
        }
        tblAccessPoints.setItems(tms);
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("UserRolesAndAuthoritiesForm");
    }
    private void setUi(String location) throws IOException {
        Stage stage = (Stage) manageAccessPointsContext.getScene().getWindow();
        stage.setScene(
                new Scene(
                        FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))
                )
        );
        stage.centerOnScreen();
    }

    public void btnSubmitOnAction(ActionEvent actionEvent) {
        if(accessPointBo.createAccessPoint(
                new AccessPointDto(KeyGenerator.generateId(),
                        txtAccessPoint.getText(),null)
        )){
            loadAllData();
            new Alert(Alert.AlertType.CONFIRMATION, "Saved!", ButtonType.CLOSE).show();
        }
    }
}
