package com.mybnb.mybnb;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WelcomeController {
    @FXML
    public Button renterLoginBtn;
    @FXML
    public Button hostLoginBtn;
    @FXML
    public Button renterSignupBtn;

    @FXML
    private void onAdminBtnClick(){
        System.out.println("hello");
    }

    public void onRenterLoginBtnClick(ActionEvent actionEvent) {
    }

    public void onHostLoginBtnClick(ActionEvent actionEvent) {
    }

    public void onRenterSignupBtnClick(ActionEvent actionEvent) {
    }

    public void onHostSignupBtnClick(ActionEvent actionEvent) {
    }
}