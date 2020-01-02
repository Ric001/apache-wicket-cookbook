package org.users;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.ValidationError;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(HomePage.class.getName());
	private static final String ZIPCODE = "ZIPCODE";
	private static final String PHONE = "PHONE";
	private static final List<String> TYPES = Arrays.asList(ZIPCODE, PHONE);

	public HomePage(final PageParameters parameters) {
		super(parameters);

		add(new FeedbackPanel("feedback"));
		final List<Component> components = new LinkedList<>();
		components.add(getDropDownChoice("types"));
		components.add(getKeywords("keyword"));
		components.add(submitEvent("send"));
		add(queries("users",  components));
		add(getRegisterForm("user-registration"));
		getRedirectionTab().forEach(this::add);
	}	

	private DropDownChoice<String> getDropDownChoice(String id) {
		LOG.info("[RETURNING FROM DropDownChoice<String> getDropDownChoice(String id)]");
		return (DropDownChoice<String>) new DropDownChoice<String>(id, new Model<String>(ZIPCODE), TYPES).setRequired(true);
	}

	private TextField<String> getKeywords(final String id) {
		LOG.info("[RETURING FROM ]TextField<String> getKeywords(final String id)");
		return (TextField<String>) new TextField<>(id, new Model<String>()).setRequired(true);
	}

	private AjaxFallbackLink<Void> ajaxFallBackButton(final String id) {
		LOG.info("[RETURNING AjaxFallbackLink<Void> ajaxFallbackButton(String id)]");
		return new AjaxFallbackLink<Void>(id) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(Optional<AjaxRequestTarget> target) {
				if (target.isPresent())
					target.get().add(new Label("Performing some validations"));
			}
		};
	}

	private Button submitEvent(String id) {
		LOG.info("[Button subitEvent(String id)]");
		return new Button(id);
	}

	private Form<Void> queries(final String id, List<Component> componentsToAdd) {
		LOG.info("[ENTERING Form<Void> rnameForm(final String id, List<FormComponent<?>> componentsToAdd)]");
		if (Objects.isNull(id) || id.isEmpty())
			return null;
		if (Objects.isNull(componentsToAdd) || componentsToAdd.isEmpty())
			return null;

		final Form<Void> userForm = new Form<Void>(id) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {	
				info("Form successfully submitted");			
			}
			
			@Override
			public void onValidate() {
				if (hasError()) return;
				@SuppressWarnings("unchecked")
				final TextField<String> keywordField = (TextField<String>)get("keyword");
				@SuppressWarnings("unchecked")
				final DropDownChoice<String> types = (DropDownChoice<String>)get("types");
				runPatternValiations(keywordField, types);
			}
		};
		componentsToAdd.forEach(userForm::add);
		LOG.info(String.format("[RETURNING FROM Form<Void> rnameForm(final String id, List<FormComponent<?>> componentsToAdd) %s]", userForm));
		return userForm;
	}

	private Form<Void> getRegisterForm(final String id) {
		LOG.info("[ENTERING Form<Void> getRegisterForm(final String id, final List<Component> componentsToAdd)]");
		
		final CompoundPropertyModel<User> compoundPropertyModel = new CompoundPropertyModel<>(new User());
		final UserRegistration userRegistration = new UserRegistration(Optional.of(compoundPropertyModel), id);
		final Form<Void> registrationForm = userRegistration.getRegistrationForm();

		LOG.info(String.format("[RETURNING Form<Void> getRegistrationForm(final String id, final List<Component> componentsToAdd)]: %s", registrationForm));
		return registrationForm;
	}

	//Create a method to Form<Void> getRegisterForm(final String id, final List<Component> componentsToAdd), to control the validators with the component References

	private void runPatternValiations(final TextField<String> keyword, final DropDownChoice<String> selectedType) {
		LOG.info("[ENTERING void runPatternValiations(final String keyword, final String selectedType)]");

		if (ZIPCODE.equals(selectedType.getConvertedInput()) && !Pattern.matches("[0-9]{5}", keyword.getConvertedInput()))
			keyword.error(new ValidationError().setMessage("INVALID ZIP_CODE"));
		if (PHONE.equals(selectedType.getConvertedInput()) && !Pattern.matches("[0-9]{10}", keyword.getConvertedInput()))
			keyword.error(new ValidationError().setMessage("INVALID PHONE NUMBER"));
		
		LOG.info("[ENDING void runPatternValiations(final TextField<String> keyword, final DropDownChoice<String> selectedType)]");
	}


	private List<Link<Void>> getRedirectionTab() {

		final List<Link<Void>> links = new LinkedList<>();
		links.add(getLink("signupPageLink"));
		return links;
	}

	private Link<Void> getLink(String id) {
		return new Link<Void>(id) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(SignupPage.class);
			}
		};
	}
}
