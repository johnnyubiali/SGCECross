package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.DepartmentService;
import model.services.SellerService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuAttendanceRegister;
	@FXML
	private MenuItem menuAttendanceSearch;
	@FXML
	private MenuItem menuDestiny;
	@FXML
	private MenuItem menuSpeciality;
	@FXML
	private MenuItem menuEmployee;
	@FXML
	private MenuItem menuCovidResults;
	@FXML
	private MenuItem menuBedType;
	@FXML
	private MenuItem menuVentilationType;
	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemAttendanceRegister() {
			Stage parentStage = Utils.currentStage(event);
			Seller obj = new Seller();
			createDialogForm(obj, "/gui/AttendanceRegister.fxml", parentStage);
		});

	}

	@FXML
	public void onMnuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> { // lambda expression
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMnuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {
		});
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {

	}

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) { // keep app stable
																									// with synchronized
																									// mode
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVbox = loader.load();

			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent(); // get Contents of
																					// ScroolPane->content->Vbox

			Node mainMenu = mainVBox.getChildren().get(0); // get children of the Node
			mainVBox.getChildren().clear(); // Clean all Children objects
			mainVBox.getChildren().add(mainMenu); // add Main Menu
			mainVBox.getChildren().addAll(newVbox.getChildren()); // return all childrens of newVbox Menu

			T controller = loader.getController();
			initializingAction.accept(controller);

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error Loading View", e.getMessage(), AlertType.ERROR);
		}
	}

}
