package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Answer;
import model.entities.BedType;
import model.entities.CovidResults;
import model.entities.Destination;
import model.entities.Employee;
import model.entities.Pacient;
import model.entities.Register;
import model.entities.Resource;
import model.entities.VentilationType;
import model.exceptions.ValidationException;
import model.services.AnswerService;
import model.services.BedTypeService;
import model.services.CovidResultsService;
import model.services.DestinationService;
import model.services.EmployeeService;
import model.services.PacientService;
import model.services.RegisterService;
import model.services.ResourceService;
import model.services.VentilationTypeService;

public class RegisterFormController implements Initializable {

	DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.systemDefault());
	
	private Register entity;

	private RegisterService service;

	private PacientService pacientService;
	
	private AnswerService answerService;

	private ResourceService resourceService;

	private EmployeeService employeeService;

	private DestinationService destinationService;

	private CovidResultsService covidResultsService;

	private BedTypeService bedTypeService;

	private VentilationTypeService ventilationTypeService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private DatePicker dpDateInsert;

	@FXML
	private LocalDateTime timeInsert;

	@FXML
	private DatePicker dpDateFinal;

	@FXML
	private LocalDateTime timeFinal;

	@FXML
	private TextField txtRegistSigs;
	
	@FXML
	private TextField txtPacient;

	@FXML
	private TextField txtAge;
	
	@FXML
	private ComboBox<Answer> comboBoxAnswer;

	@FXML
	private ComboBox<Employee> comboBoxEmployee;

	@FXML
	private ComboBox<CovidResults> comboBoxCovidResults;

	@FXML
	private ComboBox<Destination> comboBoxDestination;

	@FXML
	private ComboBox<Resource> comboBoxResource;

	@FXML
	private ComboBox<BedType> comboBoxBedType;

	@FXML
	private ComboBox<VentilationType> comboBoxVentilationType;

	@FXML
	private TextField txtObs;

	@FXML
	private Label labelErrorRegistSigs;
	
	@FXML
	private Label labelErrorPacient;

	@FXML
	private Label labelErrorAge;

	@FXML
	private Button btSave;

	@FXML
	private Button btDelete;

	@FXML
	private Button btEdit;

	@FXML
	private Button btCancel;

	private ObservableList<Employee> obsListEmployee;
	
	private ObservableList<Answer> obsListAnswer;

	private ObservableList<CovidResults> obsListCovidResults;

	private ObservableList<Destination> obsListDestination;

	private ObservableList<Resource> obsListResource;

	private ObservableList<BedType> obsListBedType;

	private ObservableList<VentilationType> obsListVentilationType;

	public void setRegister(Register entity) {
		this.entity = entity;
	}

	public void setServices(RegisterService service, PacientService pacientService, AnswerService answerService, ResourceService resourceService,
			EmployeeService employeeService, DestinationService destinationService,
			CovidResultsService covidResultsService, BedTypeService bedTypeService,
			VentilationTypeService ventilationTypeService) { // dependecy inject
		this.service = service;
		this.pacientService = pacientService;
		this.answerService = answerService;
		this.resourceService = resourceService;
		this.employeeService = employeeService;
		this.destinationService = destinationService;
		this.covidResultsService = covidResultsService;
		this.bedTypeService = bedTypeService;
		this.ventilationTypeService = ventilationTypeService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Erro ao salvar o objeto!", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners)
			listener.onDataChanged();
	}

	private Register getFormData() {
		Register obj = new Register();
		Pacient pac = new Pacient();

		ValidationException exception = new ValidationException("Erro na validação dos dados!");

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if (txtRegistSigs.getText() == null || txtRegistSigs.getText().trim().equals("")) { // exception to empty fields
			exception.addError("registSigs", "* Campo obrigatório!");
		}
		pac.setRegistSigs(Utils.tryParseToInt(txtRegistSigs.getText()));

		if (txtPacient.getText() == null || txtPacient.getText().trim().equals("")) { // exception to empty fields
			exception.addError("pacient", "* Campo obrigatório!");
		}
		pac.setName(txtPacient.getText());

		if (txtAge.getText() == null || txtAge.getText().trim().equals("")) { // exception to empty fields
			exception.addError("age", "* Campo obrigatório! Inserir valor 0 (zero), se desconhecido!");
		}
		pac.setAge(Utils.tryParseToInt(txtAge.getText()));
		
		Instant instantDateInsert = Instant.from(dpDateInsert.getValue().atStartOfDay(ZoneId.systemDefault()));
		obj.setDateInsert(Date.from(instantDateInsert));
		
		Instant instantTimeInsert = Instant.from(timeInsert.atZone(ZoneId.systemDefault()));
		obj.setTimeInsert(Date.from(instantTimeInsert));
		
		Instant instantDateFinal = Instant.from(dpDateFinal.getValue().atStartOfDay(ZoneId.systemDefault()));
		obj.setDateFinal(Date.from(instantDateFinal));
		
		Instant instantTimeFinal = Instant.from(timeFinal.atZone(ZoneId.systemDefault()));
		obj.setTimeFinal(Date.from(instantTimeFinal));

		obj.setObs(txtObs.getText());
		
		obj.setAnswer(comboBoxAnswer.getValue());
		obj.setEmployee(comboBoxEmployee.getValue());
		obj.setResource(comboBoxResource.getValue());
		obj.setDestination(comboBoxDestination.getValue());
		obj.setCovidResults(comboBoxCovidResults.getValue());
		obj.setBedType(comboBoxBedType.getValue());
		obj.setVentilationType(comboBoxVentilationType.getValue());
		

		if (exception.getErrors().size() > 0) { // if exist errors throw exception
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldInteger(txtRegistSigs);
		Constraints.setTextFieldMaxLength(txtPacient, 100);
		Constraints.setTextFieldInteger(txtAge);
		Constraints.setTextFieldMaxLength(txtObs, 300);
		Utils.formatDatePicker(dpDateInsert, "dd/MM/yyyy");
		timeInsert.format(timeFormat);
		Utils.formatDatePicker(dpDateFinal, "dd/MM/yyyy");
		timeFinal.format(timeFormat);
		
		
		initializeComboBoxAnswer();
		initializeComboBoxCovidResults();
		initializeComboBoxDestination();
		initializeComboBoxResource();
		initializeComboBoxEmployee();
		initializeComboBoxBedType();
		initializeComboBoxVentilationType();
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtRegistSigs.setText(String.valueOf(entity.getPacient().getRegistSigs()));
		txtPacient.setText(entity.getPacient().getName());
		txtAge.setText(String.valueOf(entity.getPacient().getAge()));
		if(entity.getDateInsert() != null) {
			dpDateInsert.setValue(LocalDate.ofInstant(entity.getDateInsert().toInstant(), ZoneId.systemDefault()));
		}
		if(entity.getDateFinal() != null) {
			dpDateFinal.setValue(LocalDate.ofInstant(entity.getDateFinal().toInstant(), ZoneId.systemDefault()));
		}
		//Employee ComboBox
		if(entity.getEmployee() == null) {
			comboBoxEmployee.getSelectionModel().selectFirst();
		}
		else {
			comboBoxEmployee.setValue(entity.getEmployee());
		}
		//CovidResults ComboBox
		if(entity.getCovidResults() == null) {
			comboBoxCovidResults.getSelectionModel().selectFirst();
		}
		else {
			comboBoxCovidResults.setValue(entity.getCovidResults());
		}
		//Destination ComboBox
		if(entity.getDestination() == null) {
			comboBoxDestination.getSelectionModel().selectFirst();
		}
		else {
			comboBoxDestination.setValue(entity.getDestination());
		}
		//Resource ComboBox
		if(entity.getResource() == null) {
			comboBoxResource.getSelectionModel().selectFirst();
		}
		else {
			comboBoxResource.setValue(entity.getResource());
		}
		//BedType ComboBox
		if(entity.getBedType() == null) {
			comboBoxBedType.getSelectionModel().selectFirst();
		}
		else {
			comboBoxBedType.setValue(entity.getBedType());
		}
		//VentilationType ComboBox
		if(entity.getVentilationType() == null) {
			comboBoxVentilationType.getSelectionModel().selectFirst();
		}
		else {
			comboBoxVentilationType.setValue(entity.getVentilationType());
		}
		//Answer ComboBox
		if(entity.getAnswer() == null) {
			comboBoxAnswer.getSelectionModel().selectFirst();
		}
		else {
			comboBoxResource.setValue(entity.getResource());
		}
	}

	public void loadAssociatedObjects() {
		if (employeeService == null) {
			throw new IllegalStateException("EmployeeService was null");
		}
		List<Employee> employeeList = employeeService.findAll();
		obsListEmployee = FXCollections.observableArrayList(employeeList);
		comboBoxEmployee.setItems(obsListEmployee);
		
		if (answerService == null) {
			throw new IllegalStateException("AnswerService was null");
		}
		List<Answer> answerList = answerService.findAll();
		obsListAnswer = FXCollections.observableArrayList(answerList);
		comboBoxAnswer.setItems(obsListAnswer);
		
		if (destinationService == null) {
			throw new IllegalStateException("DestinationService was null");
		}
		List<Destination> destinationList = destinationService.findAll();
		obsListDestination = FXCollections.observableArrayList(destinationList);
		comboBoxDestination.setItems(obsListDestination);
		
		if (covidResultsService == null) {
			throw new IllegalStateException("CovidResultsService was null");
		}
		List<CovidResults> covidResultsList = covidResultsService.findAll();
		obsListCovidResults = FXCollections.observableArrayList(covidResultsList);
		comboBoxCovidResults.setItems(obsListCovidResults);
		
		if (resourceService == null) {
			throw new IllegalStateException("ResourceService was null");
		}
		List<Resource> resourceList = resourceService.findAll();
		obsListResource = FXCollections.observableArrayList(resourceList);
		comboBoxResource.setItems(obsListResource);
		
		if (bedTypeService == null) {
			throw new IllegalStateException("BedTypeService was null");
		}
		List<BedType> bedTypeList = bedTypeService.findAll();
		obsListBedType = FXCollections.observableArrayList(bedTypeList);
		comboBoxBedType.setItems(obsListBedType);

		if (ventilationTypeService == null) {
			throw new IllegalStateException("VentilationTypeService was null");
		}
		List<VentilationType> ventilationTypeList = ventilationTypeService.findAll();
		obsListVentilationType = FXCollections.observableArrayList(ventilationTypeList);
		comboBoxVentilationType.setItems(obsListVentilationType);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		labelErrorRegistSigs.setText(fields.contains("registSigs") ? errors.get("registSigs") : "");
		labelErrorPacient.setText(fields.contains("name") ? errors.get("name") : "");
		labelErrorAge.setText(fields.contains("age") ? errors.get("age") : "");
	}
	
	private void initializeComboBoxAnswer() {
		Callback<ListView<Answer>, ListCell<Answer>> factory = lv -> new ListCell<Answer>() {
			@Override
			protected void updateItem(Answer item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getDescription());
			}
		};
		
		comboBoxAnswer.setCellFactory(factory);
		comboBoxAnswer.setButtonCell(factory.call(null));
	}
	
	private void initializeComboBoxDestination() {
		Callback<ListView<Destination>, ListCell<Destination>> factory = lv -> new ListCell<Destination>() {
			@Override
			protected void updateItem(Destination item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getDestiny());
			}
		};
		
		comboBoxDestination.setCellFactory(factory);
		comboBoxDestination.setButtonCell(factory.call(null));
	}
	
	private void initializeComboBoxCovidResults() {
		Callback<ListView<CovidResults>, ListCell<CovidResults>> factory = lv -> new ListCell<CovidResults>() {
			@Override
			protected void updateItem(CovidResults item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getResults());
			}
		};
		
		comboBoxCovidResults.setCellFactory(factory);
		comboBoxCovidResults.setButtonCell(factory.call(null));
	}
	
	private void initializeComboBoxResource() {
		Callback<ListView<Resource>, ListCell<Resource>> factory = lv -> new ListCell<Resource>() {
			@Override
			protected void updateItem(Resource item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getSpecialty());
			}
		};
		
		comboBoxResource.setCellFactory(factory);
		comboBoxResource.setButtonCell(factory.call(null));
	}
	
	private void initializeComboBoxEmployee() {
		Callback<ListView<Employee>, ListCell<Employee>> factory = lv -> new ListCell<Employee>() {
			@Override
			protected void updateItem(Employee item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		
		comboBoxEmployee.setCellFactory(factory);
		comboBoxEmployee.setButtonCell(factory.call(null));
	}
	
	private void initializeComboBoxBedType() {
		Callback<ListView<BedType>, ListCell<BedType>> factory = lv -> new ListCell<BedType>() {
			@Override
			protected void updateItem(BedType item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getDescription());
			}
		};
		
		comboBoxBedType.setCellFactory(factory);
		comboBoxBedType.setButtonCell(factory.call(null));
	}
	
	private void initializeComboBoxVentilationType() {
		Callback<ListView<VentilationType>, ListCell<VentilationType>> factory = lv -> new ListCell<VentilationType>() {
			@Override
			protected void updateItem(VentilationType item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getDescription());
			}
		};
		
		comboBoxVentilationType.setCellFactory(factory);
		comboBoxVentilationType.setButtonCell(factory.call(null));
	}
}
