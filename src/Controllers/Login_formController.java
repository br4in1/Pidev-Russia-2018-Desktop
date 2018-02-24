/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Entities.Admin;
import Entities.Moderator;
import Entities.SimpleUser;
import java.util.Map;
import com.cloudinary.*;
import Entities.User;
import Services.Browser;
import Services.UserCrud;
import Utils.TokenGenerator;
import com.cloudinary.utils.ObjectUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import static jdk.nashorn.internal.objects.NativeJava.to;

/**
 * FXML Controller class
 *
 * @author br4in
 */
public class Login_formController implements Initializable {

	Cloudinary cloudinary;
	@FXML
	private Pane loginform;
	@FXML
	private JFXTextField username;
	@FXML
	private JFXTextField username_login;
	@FXML
	private JFXTextField email;
	@FXML
	private JFXTextField lastname;
	@FXML
	private JFXTextField firstname;
	@FXML
	private JFXPasswordField password;
	@FXML
	private JFXPasswordField password_login;
	@FXML
	private JFXPasswordField passwordConfirm;
	@FXML
	private Pane signupform;
	@FXML
	private JFXRadioButton firstradio;
	@FXML
	private JFXRadioButton secondradio;
	@FXML
	private Pane photopick;
	@FXML
	private JFXComboBox<String> nationality;
	@FXML
	private ImageView countryavatar;
	@FXML
	private ImageView uploadphoto;
	@FXML
	private JFXDatePicker birthdate;
	@FXML
	private JFXButton signupbtn;
	@FXML
	private JFXButton uploadbtn;
	@FXML
	private StackPane welcomeSP;
	private static String current_username = null;
	@FXML
	private JFXButton loginbtn;
	@FXML
	private JFXButton backbtn;
	@FXML
	private JFXButton forgotpassword;
	@FXML
	private Pane forgotform;
	@FXML
	private JFXButton sendcodeforgot;
	@FXML
	private JFXPasswordField pass1forgot;
	@FXML
	private JFXTextField mailforgot;
	@FXML
	private JFXTextField codeforgot;
	@FXML
	private JFXPasswordField pass2forgot;
	@FXML
	private JFXButton changepassword;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		cloudinary = new Cloudinary("cloudinary://212894137142756:7Coi2BsCet7rXqPmDAuBi08ONfQ@dbs7hg9cy");
		username.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					Boolean exist = UserCrud.findUserByUsername(username.getText());
					if (exist) {
						username.setUnFocusColor(Color.rgb(255, 0, 0, 1));
					} else {
						username.setUnFocusColor(Color.rgb(0, 0, 0, 1));
					}
				}
			}
		});

		email.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					Boolean exist = UserCrud.findUserByEmail(email.getText());
					if (exist) {
						email.setUnFocusColor(Color.rgb(255, 0, 0, 1));
					} else {
						email.setUnFocusColor(Color.rgb(0, 0, 0, 1));
					}
				}
			}
		});

		password.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					if (password.getText().length() < 7) {
						JFXDialogLayout content = new JFXDialogLayout();
						content.setHeading(new Text(""));
						content.setBody(new Text("Le mot de passe doit contenir au moins 7 caractères"));
						JFXDialog check_username = new JFXDialog(welcomeSP, content, JFXDialog.DialogTransition.CENTER);
						check_username.show();
					}
				}
			}
		});

		passwordConfirm.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					if (!passwordConfirm.getText().equals(password.getText())) {
						passwordConfirm.setUnFocusColor(Color.rgb(255, 0, 0, 1));
					} else {
						passwordConfirm.setUnFocusColor(Color.rgb(0, 0, 0, 1));
					}
				}
			}
		});
	}

	@FXML
	public void showSignUpForm() {
		loginform.setVisible(false);
		signupform.setVisible(true);
		ObservableList<String> cities = FXCollections.observableArrayList();
		String[] locales1 = Locale.getISOCountries();
		for (String countrylist : locales1) {
			Locale obj = new Locale("", countrylist);
			String[] city = {obj.getDisplayCountry()};
			for (int x = 0; x < city.length; x++) {
				cities.add(obj.getDisplayCountry());
			}
		}
		nationality.setItems(cities);
		nationality.setPromptText("Nationalité");
		Locale.setDefault(Locale.FRANCE);
	}

	@FXML
	public void hideSignUpForm() {
		loginform.setVisible(true);
		signupform.setVisible(false);
	}

	public Boolean CheckUsername() {
		Boolean exist = UserCrud.findUserByUsername(username.getText());
		if (exist) {
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Oops !"));
			content.setBody(new Text("Ce nom d'utilisateur existe déjà"));
			JFXDialog check_username = new JFXDialog(welcomeSP, content, JFXDialog.DialogTransition.CENTER);
			check_username.show();
			return exist;
		}
		return false;
	}

	public Boolean CheckEmail() {
		Boolean exist = UserCrud.findUserByUsername(email.getText());
		if (exist) {
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Oops !"));
			content.setBody(new Text("Cette adresse email existe déjà"));
			JFXDialog check_username = new JFXDialog(welcomeSP, content, JFXDialog.DialogTransition.CENTER);
			check_username.show();
			return exist;
		}
		return false;
	}

	public Boolean CheckPassword() {
		if (password.getText().length() < 7 || !passwordConfirm.getText().equals(password.getText())) {
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Oops !"));
			content.setBody(new Text("Les deux mots de passes doivent être exactes et contenant 7 caractères ou plus."));
			JFXDialog check_username = new JFXDialog(welcomeSP, content, JFXDialog.DialogTransition.CENTER);
			check_username.show();
			return false;
		}
		return true;
	}

	@FXML
	public void SignUpUserStepOne() {
		Boolean B1 = CheckUsername();
		Boolean B2 = CheckEmail();
		Boolean B3 = CheckPassword();
		if (!B1 && !B2 && B3) {
			Date today = Date.valueOf(LocalDate.now());
			SimpleUser u = new SimpleUser(Date.valueOf(birthdate.getValue()), today, nationality.getSelectionModel().getSelectedItem(), false, 0, null, username.getText(), email.getText(), false, null, password.getText(), null, "ROLE_USER", firstname.getText(), lastname.getText());
			UserCrud.AddUserToDataBaseStepOne(u);
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text("Tout est prêt ... ou presque !"));
			content.setBody(new Text("Consultez votre boite email pour activer votre compte."));
			JFXDialog check_username = new JFXDialog(welcomeSP, content, JFXDialog.DialogTransition.CENTER);
			check_username.show();
			check_username.setOnDialogClosed(new EventHandler<JFXDialogEvent>() {
				@Override
				public void handle(JFXDialogEvent event) {
					signupform.setVisible(false);
					loginform.setVisible(true);
				}

			});
		}
	}

	@FXML
	public void loginUser() throws IOException {
		User u = UserCrud.AuthenticateUser(username_login.getText(), password_login.getText());
		if (u == null) {
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text(""));
			content.setBody(new Text("Veuillez vérifier les informations saisies."));
			JFXDialog check_data = new JFXDialog(welcomeSP, content, JFXDialog.DialogTransition.CENTER);
			check_data.show();
		} else if (u != null) {
			if (u.getRoles().equals("ROLE_ADMIN")) {
				Admin.current_user = (Admin) (u);
				Admin.current_user = UserCrud.GetMyData_Admin(Admin.current_user);
				ShowDashboard();
			} else if (u.getRoles().equals("ROLE_MODERATOR")) {
				if (u.getEnabled()) {
					Moderator.current_user = (Moderator) (u);
					Moderator.current_user = UserCrud.GetMyData_Moderator(Moderator.current_user);
					ShowDashboard();
				} else {
					JFXDialogLayout content = new JFXDialogLayout();
					content.setHeading(new Text("Veuillez contacter un administrateur."));
					content.setBody(new Text("Votre compte est désactivé."));
					JFXDialog activateAccount = new JFXDialog(welcomeSP, content, JFXDialog.DialogTransition.CENTER);
					activateAccount.show();
				}
			} else {
				if (u.getEnabled()) {
					SimpleUser.current_user = (SimpleUser) (u);
					SimpleUser.current_user = UserCrud.GetMyData_SimpleUser(SimpleUser.current_user);
					ShowFrontEnd();
				} else {
					JFXDialogLayout content = new JFXDialogLayout();
					content.setHeading(new Text("Veuillez saisir le code que vous avez reçu sur votre boite."));
					JFXTextField confirmation_token = new JFXTextField();
					content.setBody(confirmation_token);
					JFXDialog activateAccount = new JFXDialog(welcomeSP, content, JFXDialog.DialogTransition.CENTER);
					activateAccount.show();
					confirmation_token.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							if (confirmation_token.getText().equals(UserCrud.getConfirmationToken(u.getUsername()))) {
								UserCrud.enableSimpleUser(u.getUsername());
								loginform.setVisible(false);
								activateAccount.setVisible(false);
								photopick.setVisible(true);
								String filename = ((SimpleUser) (u)).getNationality();
								filename = filename.replace(" ", "_").toLowerCase();
								countryavatar.setImage(new Image("assets/flags/" + filename + ".png"));
								current_username = u.getUsername();
								SimpleUser.current_user = UserCrud.getSimpleUserByUsername(current_username);
							}
						}
					});
				}
			}
		}
	}

	@FXML
	public void FirstIsSelected() {
		firstradio.setSelected(true);
		secondradio.setSelected(false);
	}

	@FXML
	public void SecondIsSelected() {
		firstradio.setSelected(false);
		secondradio.setSelected(true);
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choisir une photo");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
		File f = fileChooser.showOpenDialog(null);
		if (f != null) {
			Image img = new Image(f.toURI().toString());
			uploadphoto.setImage(img);
		}
	}

	public static void SendConfirmationToken(String dest, String confirmationToken) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.prot", "465");
		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("moez.haddad@esprit.tn", "moezmoezhaddad71126429118");
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("moez.haddad@esprit.tn"));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(dest));
			message.setSubject("Account Activation");
			message.setText("Thank you for signing up on our platform. Please use this code to enable your account. " + confirmationToken);

			//send the message  
			Transport.send(message);

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void UploadPhoto() throws Exception {
		if (!firstradio.isSelected() && !secondradio.isSelected()) {
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text(""));
			content.setBody(new Text("Vous devez choisir une des options."));
			JFXDialog check_data = new JFXDialog(welcomeSP, content, JFXDialog.DialogTransition.CENTER);
			check_data.show();
		} else {
			File toUpload = new File("toUpload.png");
			if (firstradio.isSelected()) {
				BufferedImage bf = SwingFXUtils.fromFXImage(countryavatar.getImage(), null);
				ImageIO.write(bf, "png", toUpload);
			} else if (secondradio.isSelected() && uploadphoto.getImage().impl_getUrl() != "assets/update.png") {
				BufferedImage bf = SwingFXUtils.fromFXImage(uploadphoto.getImage(), null);
				ImageIO.write(bf, "png", toUpload);
			}
			System.out.println(toUpload.getPath());
			Map uploadResult = cloudinary.uploader().upload(toUpload, ObjectUtils.emptyMap());
			toUpload.delete();
			UserCrud.UpdateUserPhoto((String) uploadResult.get("url"), current_username);
			SimpleUser.current_user = UserCrud.getSimpleUserByUsername(current_username);
			SimpleUser.current_user.setProfilepicture((String) uploadResult.get("url"));
			try {
				ShowFrontEnd();
			} catch (IOException ex) {
				Logger.getLogger(Login_formController.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	@FXML
	private void AuthWithFacebook(ActionEvent event) throws IOException {
		Browser facebookBrowser = new Browser("2100597990159243", "819dc6dd91612fad4c43981e167ba986");
		Scene fbloginwindow;
		fbloginwindow = new Scene(facebookBrowser, 900, 600, Color.web("#666970"));
		Stage st = new Stage();
		st.setScene(fbloginwindow);
		st.show();
		facebookBrowser.showLogin(st, this);
	}

	public void ShowFrontEnd() throws IOException {
		((Stage) (loginform.getScene().getWindow())).close();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/FrontEnd.fxml"));
		Parent root = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}

	public void ShowDashboard() throws IOException {
		((Stage) (loginform.getScene().getWindow())).close();
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/Dashboard.fxml"));
		Parent root = (Parent) fxmlLoader.load();
		Stage stage = new Stage();
		stage.setScene(new Scene(root));
		stage.show();
	}

	@FXML
	private void loginUserByKeyboard(KeyEvent event) throws IOException {
		if (event.getCode() == KeyCode.ENTER) {
			loginUser();
		}
	}

	@FXML
	private void showForgotForm(ActionEvent event) {
		loginform.setVisible(false);
		forgotform.setVisible(true);
	}

	@FXML
	private void sendCodeForgot(ActionEvent event) {
		if(mailforgot.getText() == null || UserCrud.findUserByEmail(mailforgot.getText())){
			JFXDialogLayout content = new JFXDialogLayout();
			content.setHeading(new Text(""));
			content.setBody(new Text("L'adresse email est invalide."));
			JFXDialog check_data = new JFXDialog(welcomeSP, content, JFXDialog.DialogTransition.CENTER);
			check_data.show();
		}
		else
		{
			String confirmationToken = TokenGenerator.generateToken2();
			SendConfirmationToken(mailforgot.getText(), confirmationToken);
			
		}
	}

	@FXML
	private void ChangePassword(ActionEvent event) {
	}
}
