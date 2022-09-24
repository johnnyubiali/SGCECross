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
import model.entities.Resource;
import model.services.ResourceService;

public class ResourceListController implements Initializable, DataChangeListener {

	private ResourceService service;

	@FXML
	private TableView<Resource> tableViewResource;

	@FXML
	private TableColumn<Resource, Integer> tableColumnId;

	@FXML
	private TableColumn<Resource, String> tableColumnSpecialty;

	@FXML
	private TableColumn<Resource, Resource> tableColumnEDIT;

	@FXML
	private TableColumn<Resource, Resource> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Resource> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Resource obj = new Resource();
		createDialogForm(obj, "/gui/ResourceForm.fxml", parentStage);
	}

	public void setResourceService(ResourceService service) { // dependecy inject
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnSpecialty.setCellValueFactory(new PropertyValueFactory<>("specialty"));

		Stage stage = (Stage) Main.getMainScene().getWindow(); // return config of this scene
		tableViewResource.prefHeightProperty().bind(stage.heightProperty()); // table follow this window height config

	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null!"); // return error if service is null
		}
		List<Resource> list = service.findAll(); // return list of ResourceService
		obsList = FXCollections.observableArrayList(list); // instance obsList with data and reference to list
		tableViewResource.setItems(obsList); // load itens in tableView
		initEditButtons(); // add new button edit type in all lines that nedded
		initRemoveButtons();

	}

	private void createDialogForm(Resource obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			ResourceFormController controller = loader.getController();
			controller.setResource(obj);
			controller.setResourceService(new ResourceService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Editar Especialidade");
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Resource, Resource>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Resource obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/ResourceForm.fxml", Utils.currentStage(event)));
			}

		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Resource, Resource>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(Resource obj, boolean empty) {
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

	private void removeEntity(Resource obj) {
		
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