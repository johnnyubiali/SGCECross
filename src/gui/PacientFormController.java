package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.CovidResults;
import model.entities.Pacient;
import model.exceptions.ValidationException;
import model.services.PacientService;

public class PacientFormController implements Initializable{

	private Pacient entity;

	private PacientService service;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtRegistSigs;

	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtAge;
	
	@FXML
	private ComboBox<CovidResults> comboBoxCovidResults;
	
	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorRegistSigs;
	
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

	public void setPacient(Pacient entity) {
		this.entity = entity;
	}
	
	public void setServices(PacientService service) { // dependecy inject
		this.service = service;
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
		} 
		catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} 
		catch (DbException e) {
			Alerts.showAlert("Erro ao salvar o objeto!", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners)
			listener.onDataChanged();
	}

	private Pacient getFormData() {
		Pacient obj = new Pacient();

		ValidationException exception = new ValidationException("Erro na valida��o dos dados!");

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if (txtRegistSigs.getText() == null || txtRegistSigs.getText().trim().equals("")) { // exception to empty fields
			exception.addError("registSigs", "* Campo obrigat�rio!");
		}
		obj.setRegistSigs(Utils.tryParseToInt(txtRegistSigs.getText()));
		
		if (txtName.getText() == null || txtName.getText().trim().equals("")) { // exception to empty fields
			exception.addError("name", "* Campo obrigat�rio!");
		}
		obj.setName(txtName.getText());
		
		if (txtAge.getText() == null || txtAge.getText().trim().equals("")) { // exception to empty fields
			exception.addError("age", "* Campo obrigat�rio!");
		}
		obj.setAge(Utils.tryParseToInt(txtAge.getText()));
		
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
		Constraints.setTextFieldMaxLength(txtName, 100);
		Constraints.setTextFieldInteger(txtAge);
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtRegistSigs.setText(String.valueOf(entity.getRegistSigs()));
		txtName.setText(entity.getName());
		txtAge.setText(String.valueOf(entity.getAge()));
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		labelErrorRegistSigs.setText(fields.contains("registSigs") ? errors.get("registSigs") : "");
		labelErrorName.setText(fields.contains("name") ? errors.get("name") : "");
		labelErrorAge.setText(fields.contains("age") ? errors.get("age") : "");
	}
}
