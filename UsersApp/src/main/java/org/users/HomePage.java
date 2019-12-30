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
import org.apache.wicket.markup.html.panel.FeedbackPanel;
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
		components.add(button("send"));
		add(usernameForm("users",  components));
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

	private Button button(String id) {
		LOG.info("[Button button(String id)]");
		
		return new Button(id);
	}

	private Form<Void> usernameForm(final String id, List<Component> componentsToAdd) {
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

	private void runPatternValiations(final TextField<String> keyword, final DropDownChoice<String> selectedType) {
		LOG.info("[ENTERING void runPatternValiations(final String keyword, final String selectedType)]");

		if (ZIPCODE.equals(selectedType.getConvertedInput()) && !Pattern.matches("[0-9]{5}", keyword.getConvertedInput()))
			keyword.error(new ValidationError().setMessage("INVALID ZIP_CODE"));
		if (PHONE.equals(selectedType.getConvertedInput()) && !Pattern.matches("[0-9]{10}", keyword.getConvertedInput()))
			keyword.error(new ValidationError().setMessage("INVALID PHONE NUMBER"));
		
		LOG.info("[ENDING void runPatternValiations(final TextField<String> keyword, final DropDownChoice<String> selectedType)]");
	}

	

}
