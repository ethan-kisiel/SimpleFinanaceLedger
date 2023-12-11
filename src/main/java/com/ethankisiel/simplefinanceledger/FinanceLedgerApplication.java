package com.ethankisiel.simplefinanceledger;

import com.ethankisiel.simplefinanceledger.Controllers.LedgerEditorController;
import com.ethankisiel.simplefinanceledger.Managers.EntityManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Set;

public class FinanceLedgerApplication extends Application
{

    // private EntityManager entityManager = new EntityManager();

    @Override
    public void start(Stage stage) throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(FinanceLedgerApplication.class.getResource("LedgerEditorPreview.fxml"));
        // ((LedgerEditorController) fxmlLoader.getController()).setEntityManager(entityManager);

        Scene scene = new Scene(fxmlLoader.load(), 1920, 1080);

        stage.setTitle("Simple Finance Ledger");

        try
        {
            Image stageIcon = new Image(Constants.ICON_PATH);
            stage.getIcons().add(stageIcon);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}


