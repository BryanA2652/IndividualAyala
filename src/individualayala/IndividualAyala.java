/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
package individualayala;

import individualayala.modelo.driverss;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javax.swing.JOptionPane;

/**
 *
 * @author ROCIO
 */
public class IndividualAyala extends Application {
    
      private ComboBox<Integer> yearComboBox;
    private TableView<driverss> driverTable;
    private ObservableList<driverss> driverData;
    
    private String driver = "com.mysql.jdbc.Driver";
    private String cadenaconeccion = "jdbc:mysql://localhost:3306/formula01";
    private String usuario = "root";
    private String contraseña = "";
    
   
   public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        
         yearComboBox = new ComboBox<>();
        yearComboBox.getItems().addAll(1939, 1969, 1973, 1980);
        yearComboBox.setValue(1939);

        driverTable = new TableView<>();
        driverData = FXCollections.observableArrayList();

        TableColumn<driverss, Integer> idColumn = new TableColumn<>("Driver ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("driverId"));

        TableColumn<driverss, String> refColumn = new TableColumn<>("Driver Ref");
        refColumn.setCellValueFactory(new PropertyValueFactory<>("driverRef"));

        TableColumn<driverss, Integer> numberColumn = new TableColumn<>("Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn<driverss, String> codeColumn = new TableColumn<>("Code");
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));

        TableColumn<driverss, String> forenameColumn = new TableColumn<>("Forename");
        forenameColumn.setCellValueFactory(new PropertyValueFactory<>("forename"));

        TableColumn<driverss, String> surnameColumn = new TableColumn<>("Surname");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        TableColumn<driverss, Date> dobColumn = new TableColumn<>("DOB");
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));

        TableColumn<driverss, String> nationalityColumn = new TableColumn<>("Nationality");
        nationalityColumn.setCellValueFactory(new PropertyValueFactory<>("nationality"));

        TableColumn<driverss, String> urlColumn = new TableColumn<>("URL");
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));

        driverTable.getColumns().addAll(idColumn, refColumn, numberColumn, codeColumn, forenameColumn, surnameColumn, dobColumn, nationalityColumn, urlColumn);
        driverTable.setItems(driverData);

        yearComboBox.setOnAction(event -> IndividualAyala(yearComboBox.getValue()));

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(yearComboBox, driverTable);
       
        
        Scene scene = new Scene(vbox, 800, 600);
        
        primaryStage.setTitle("Tabla conductores");
        primaryStage.setScene(scene);
        primaryStage.show();
        
         // Inicializar con el año 2016
        IndividualAyala(1939);
    }
    
    private void IndividualAyala(int year) {
        driverData.clear();
        List<driverss> drivers = getDriversByYear(year);
        driverData.addAll(drivers);
    }
    
    private List<driverss> getDriversByYear(int year) {
        List<driverss> drivers = new ArrayList<>();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            con = DriverManager.getConnection(cadenaconeccion, usuario, contraseña);
            String sql = "SELECT * FROM driverss WHERE YEAR(dob) = ?";
            statement = con.prepareStatement(sql);
            statement.setInt(1, year);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                driverss driver = new driverss(
                        resultSet.getInt("driverId"),
                        resultSet.getString("driverRef"),
                        resultSet.getInt("number"),
                        resultSet.getString("code"),
                        resultSet.getString("forename"),
                        resultSet.getString("surname"),
                        resultSet.getDate("dob"),
                        resultSet.getString("nationality"),
                        resultSet.getString("url")
                );
                drivers.add(driver);
            }
            JOptionPane.showMessageDialog(null, "Presentacion de datos");
        JOptionPane.showMessageDialog(null, "Se establecio conexion con la BD");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se establecio conexion"+e.getMessage());
        } 

        return drivers;
    }

    
}
