package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Destination;
import model.services.DestinationService;

public class DestinationListController implements Initializable, DataChangeListener {

	private DestinationService service;

	@FXML
	private TableView<Destination> tableViewDestination;

	@FXML
	private TableColumn<Destination, Integer> tableColumnId;

	@FXML
	private TableColumn<Destination, String> tableColumnDestiny;

	@FXML
	private TableColumn<Destination, Destination> tableColumnEDIT;

	@FXML
	private TableColumn<Destination, Destination> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Destination> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Destination obj = new Destination();
		createDialogForm(obj, "/gui/DestinationForm.fxml", parentStage);
	}

	public void setDestinationService(DestinationService service) { // dependecy inject
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnDestiny.setCellValueFactory(new PropertyValueFactory<>("destiny"));

		Stage stage = (Stage) Main.getMainScene().getWindow(); // return config of this scene
		tableViewDestination.prefHeightProperty().bind(stage.heightProperty()); // table follow this window height config

	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null!"); // return error if service is null
		}
		List<Destination> list = service.findAll(); // return list of DestinationService
		obsList = FXCollections.observableArrayList(list); // instance obsList with data and reference to list
		tableViewDestination.setItems(obsList); // load itens in tableView
		initEditButtons(); // add new button edit type in all lines that nedded
		initRemoveButtons();

	}

	private void createDialogForm(Destination obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			DestinationFormController controller = loader.getController();
			controller.setDestination(obj);
			controller.setDestinationService(new DestinationService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Editar Resultado de Covid-19");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", "Error Loading View", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();

	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Destination, Destination>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Destination obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DestinationForm.fxml", Utils.currentStage(event)));
			}

		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Destination, Destination>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(Destination obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Destination obj) {
		
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza disso?");
		
		if(result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();
			}
			catch (DbIntegrityException e) {
				Alerts.showAlert("Erro ao excluir o objeto!", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
}