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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Employee;
import model.exceptions.ValidationException;
import model.services.EmployeeService;

public class EmployeeFormController implements Initializable{

	private Employee entity;
	
	private EmployeeService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private TextField txtOffice;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Label labelErrorOffice;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setEmployee(Employee entity) {
		this.entity = entity;
	}
	
	public void setEmployeeService(EmployeeService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity was null!");
		}
		if(service == null) {
			throw new IllegalStateException("Service was null!");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch(ValidationException e){
			setErrorMessages(e.getErrors());
		}
		catch(DbException e) {
			Alerts.showAlert("Erro ao salvar o objeto!", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : dataChangeListeners)
		listener.onDataChanged();
	}

	private Employee getFormData() {
		Employee obj = new Employee();
		
		ValidationException exception = new ValidationException("Erro na validação dos dados!");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtName.getText() == null || txtName.getText().trim().equals("")) { // exception to empty fields
			exception.addError("name", "* Campo obrigatório!");
		}
		obj.setName(txtName.getText());
		
		if(txtOffice.getText() == null || txtOffice.getText().trim().equals("")) { // exception to empty fields
			exception.addError("office", "* Campo obrigatório!");
		}
		obj.setOffice(txtOffice.getText());
		
		if(exception.getErrors().size() > 0) { // if exist errors throw exception
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
		Constraints.setTextFieldMaxLength(txtName, 60);
		Constraints.setTextFieldMaxLength(txtOffice, 60);
	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtOffice.setText(entity.getOffice());
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		labelErrorName.setText(fields.contains("name") ? errors.get("name") : "");
		labelErrorOffice.setText(fields.contains("office") ? errors.get("office") : "");
	}
}
