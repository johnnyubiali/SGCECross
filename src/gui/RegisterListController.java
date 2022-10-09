package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
import model.entities.Register;
import model.services.AnswerService;
import model.services.BedTypeService;
import model.services.CovidResultsService;
import model.services.DestinationService;
import model.services.EmployeeService;
import model.services.PacientService;
import model.services.RegisterService;
import model.services.ResourceService;
import model.services.VentilationTypeService;

public class RegisterListController implements Initializable, DataChangeListener {

	private RegisterService service;

	@FXML
	private TableView<Register> tableViewRegister;

	@FXML
	private TableColumn<Register, Integer> tableColumnId;

	@FXML
	private TableColumn<Register, Date> tableColumnDateInsert;
	
	@FXML
	private TableColumn<Register, Date> tableColumnTimeInsert;
	
	@FXML
	private TableColumn<Register, Date> tableColumnDateFinal;
	
	@FXML
	private TableColumn<Register, Date> tableColumnTimeFinal;
	
	@FXML
	private TableColumn<Register, String> tableColumnEmployee;
	
	@FXML
	private TableColumn<Register, String> tableColumnDestination;
	
	@FXML
	private TableColumn<Register, String> tableColumnPacient;
	
	@FXML
	private TableColumn<Register, Integer> tableColumnRegistSigs;
	
	@FXML
	private TableColumn<Register, Integer> tableColumnAge;
	
	@FXML
	private TableColumn<Register, Integer> tableColumnAnswer;
	
	@FXML
	private TableColumn<Register, String> tableColumnCovidResults;
	
	@FXML
	private TableColumn<Register, String> tableColumnResource;
	
	@FXML
	private TableColumn<Register, String> tableColumnBedType;
	
	@FXML
	private TableColumn<Register, String> tableColumnVentilationType;
	
	@FXML
	private TableColumn<Register, String> tableColumnObs;

	@FXML
	private TableColumn<Register, Register> tableColumnEDIT;

	@FXML
	private TableColumn<Register, Register> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Register> obsList;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Register obj = new Register();
		createDialogForm(obj, "/gui/RegisterForm.fxml", parentStage);
	}

	public void setRegisterService(RegisterService service) { // dependecy inject
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnDateInsert.setCellValueFactory(new PropertyValueFactory<>("dateInsert"));
		Utils.formatTableColumnDate(tableColumnDateInsert, "dd/MM/yyyy");
		tableColumnTimeInsert.setCellValueFactory(new PropertyValueFactory<>("timeInsert"));
		Utils.formatTableColumnDate(tableColumnTimeInsert, "HH:ss");
		tableColumnDateFinal.setCellValueFactory(new PropertyValueFactory<>("dateFinal"));
		Utils.formatTableColumnDate(tableColumnDateFinal, "dd/MM/yyyy");
		tableColumnTimeFinal.setCellValueFactory(new PropertyValueFactory<>("timeFinal"));
		Utils.formatTableColumnDate(tableColumnTimeFinal, "HH:ss");
		tableColumnEmployee.setCellValueFactory(new PropertyValueFactory<>("employee"));
		tableColumnDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));
		tableColumnRegistSigs.setCellValueFactory(new PropertyValueFactory<>("registSigs"));
		tableColumnPacient.setCellValueFactory(new PropertyValueFactory<>("pacient"));
		tableColumnAge.setCellValueFactory(new PropertyValueFactory<>("age"));
		tableColumnAnswer.setCellValueFactory(new PropertyValueFactory<>("answer"));
		tableColumnCovidResults.setCellValueFactory(new PropertyValueFactory<>("covidResults"));
		tableColumnResource.setCellValueFactory(new PropertyValueFactory<>("resource"));
		tableColumnBedType.setCellValueFactory(new PropertyValueFactory<>("bedType"));
		tableColumnVentilationType.setCellValueFactory(new PropertyValueFactory<>("ventilationType"));
		tableColumnObs.setCellValueFactory(new PropertyValueFactory<>("obs"));

		Stage stage = (Stage) Main.getMainScene().getWindow(); // return config of this scene
		tableViewRegister.prefHeightProperty().bind(stage.heightProperty()); // table follow this window height config

	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null!"); // return error if service is null
		}
		List<Register> list = service.findAll(); // return list of RegisterService
		obsList = FXCollections.observableArrayList(list); // instance obsList with data and reference to list
		tableViewRegister.setItems(obsList); // load itens in tableView
		initEditButtons(); // add new button edit type in all lines that nedded
		initRemoveButtons();

	}

	private void createDialogForm(Register obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			RegisterFormController controller = loader.getController();
			controller.setRegister(obj);
			controller.setServices(new RegisterService(), new PacientService(), new AnswerService(), new ResourceService(), new EmployeeService(), new DestinationService(), 
								   new CovidResultsService(), new BedTypeService(), new VentilationTypeService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Editar Registro");
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
		tableColumnEDIT.setCellFactory(param -> new TableCell<Register, Register>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Register obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/RegisterForm.fxml", Utils.currentStage(event)));
			}

		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Register, Register>() {
			private final Button button = new Button("Excluir");

			@Override
			protected void updateItem(Register obj, boolean empty) {
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

	private void removeEntity(Register obj) {
		
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
