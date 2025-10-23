package frontend;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

public class MainController {
    @FXML
    private void onHello() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hello");
        alert.setHeaderText(null);
        alert.setContentText("Hello from JavaFX!");
        alert.showAndWait();
    }
}
