package views;

import controllers.BillPDFController;
import controllers.PaymentController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import models.Payment;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CashierMainView implements Initializable, CashierView {
    @FXML private ComboBox cb_table;
    @FXML private Label lb_peopleAmt, lb_totalPrice, lb_eachPrice, lb_package;
    @FXML private Button btn_delete, btn_dot, btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_pay, btn_clear;
    @FXML private TextField tf_receive, tf_change;
    private double receive;
    private List<Payment> payments = new ArrayList<>();
    private PaymentController controller;

    public void initialize(URL location, ResourceBundle resources) {
        onClickNumber(btn_0, "0");
        onClickNumber(btn_1, "1");
        onClickNumber(btn_2, "2");
        onClickNumber(btn_3, "3");
        onClickNumber(btn_4, "4");
        onClickNumber(btn_5, "5");
        onClickNumber(btn_6, "6");
        onClickNumber(btn_7, "7");
        onClickNumber(btn_8, "8");
        onClickNumber(btn_9, "9");
        onClickNumber(btn_dot, ".");
        onClickDelete();
        onActionTextField();
        onClickOtherButton();

        initComboBoxTable();

    }

    public void setAvailable(List<Payment> payments) {
        this.payments = payments;
//        initComboBoxTable();
        refresh();
    }

    private void refresh(){
        cb_table.getItems().clear();
        cb_table.getItems().addAll(payments);
    }

    public void onPayComplete(Payment payment) {
        // TODO handle pay complete event
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                BillPDFController.createBill(payment, receive);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Pay complete");
                alert.showAndWait();

            }
        });

    }

    public void onPayFailure(Payment payment) {
        // TODO handle pay failure event
    }

    public void setController(PaymentController controller) {
        this.controller = controller;
    }

    public void initComboBoxTable() {
        cb_table.getSelectionModel().selectFirst();
        cb_table.setConverter(new StringConverter() {
            @Override
            public String toString(Object object) {
                Payment payment = (Payment) object;
                return payment.getTable() + "";
            }

            @Override
            public Object fromString(String string) {
                return null;
            }
        });

        cb_table.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Payment payment = (Payment) cb_table.getValue();
                lb_package.setText(payment.getaPackage().getName());
                lb_peopleAmt.setText(payment.getAmt() + "");
                lb_totalPrice.setText((payment.getAmt() * payment.getaPackage().getPrice()) + " Baht");
                lb_eachPrice.setText(payment.getaPackage().getPrice() + " Baht");

            }
        });
    }

    public void onClickNumber(Button btn, String num){
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String price = tf_receive.getText() + num;
                tf_receive.setText(price);

            }
        });
    }

    public void onClickDelete(){
        btn_delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String price = tf_receive.getText().substring(0, tf_receive.getText().length()-1);
                tf_receive.setText(price);
            }
        });
    }

    public void onActionTextField(){
        tf_receive.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    Payment payment = (Payment) cb_table.getValue();
                    double totalChange = Double.parseDouble(tf_receive.getText()) - payment.getaPackage().getPrice()*payment.getAmt();
                    tf_change.setText(totalChange + "");
                }catch (NumberFormatException e){
                    System.out.println("TextField empty when click pay");
                }

            }
        });
    }

    public void onClickOtherButton(){
        btn_pay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(Double.parseDouble(tf_change.getText()) < 0 || tf_receive.getText().equals("")){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Look, You have not entered the  text field yet.");
                    alert.setContentText("Please enter a number in the field .");
                    alert.showAndWait();

                }else{
                    Payment payment = (Payment) cb_table.getValue();
                    controller.pay(payment);
                    receive = Double.parseDouble(tf_receive.getText());
                    clear();
                    payments.remove(payment);
                    System.out.println("payments = " + payments);

                }
            }
        });

        btn_clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clear();
            }
        });
    }

    public void clear(){
        lb_eachPrice.setText("");
        lb_totalPrice.setText("");
        lb_peopleAmt.setText("");
        lb_package.setText("");
        tf_receive.setText("");
        tf_change.setText("");
    }

}
