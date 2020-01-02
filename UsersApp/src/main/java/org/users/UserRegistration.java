package org.users;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.users.utils.InstanceValidator;
import org.users.validators.PolicyPasswordValidator;

public class UserRegistration implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(UserRegistration.class.getName());

    private final String id;
    private Form<Void> registrationForm;
    private TextField<String> username;
    private PasswordTextField password;
    private PasswordTextField confPassword;
    private final Optional<IModel<User>> compModel;
    private Button send;

    public UserRegistration(final Optional<IModel<User>> compModel, final String id) {
        this.compModel = compModel;
        this.id = id;
        init();
        initModel();
        setValidatorsInTheForm();
    }

    private void init() {
        LOG.info("[ENTERING void init()]");

        registrationForm = new Form<Void>(getDefaultId());
        username = new TextField<>("username");
        password = new PasswordTextField("password");
        confPassword = new PasswordTextField("confPassword");
        send = new Button("register");

        LOG.info("[RETURNING void init()]");
    }
    
    private String getDefaultId() {
        LOG.info("[ENTERING String getDefaultId()]");

        if (Objects.isNull(id)) 
            return "user-registration";
        
        LOG.info("[RETURNING FROM String getDefaultId()]");
        return id;
    }

    private void initModel() {
        LOG.info("[ENTERING void initModel()]");

        if (compModel.isPresent() && compModel.get() instanceof CompoundPropertyModel)
            registrationForm.setDefaultModel(compModel.get());
        else
            registrationForm.setDefaultModel(new CompoundPropertyModel<>(new User()));
        
        LOG.info(String.format("[ENDING void initModel(): %s]", registrationForm.getModel()));
    }

    public void setValidatorsInTheForm() {
        LOG.info("[ENTERING void setValidatrosInTheForm()]");

        if (Objects.nonNull(registrationForm)) {
            registrationForm.add(new EqualPasswordInputValidator(password, confPassword));
            final StringValidator strValidator = StringValidator.minimumLength(8);
            final PolicyPasswordValidator policyPassValidator = new PolicyPasswordValidator();
            password.add(strValidator);
            password.add(policyPassValidator);
            confPassword.add(strValidator);
            confPassword.add(policyPassValidator);
        }

        LOG.info("[ENDING void setValidatorsInTheForm()]");
    }

    public  UserRegistration addExtraValidators(final Object... validators) {
        LOG.info("[ENTERING void addExtraValidators(IValidator<String>... validators)]");
        
        if (Objects.nonNull(validators) &&  validators instanceof IValidator[]) {
            @SuppressWarnings("unchecked")
            final IValidator<String>[] validatorsArray = (IValidator<String>[]) validators;
            Arrays.asList(validatorsArray).forEach(username::add);
        }
            
        LOG.info(String.format("[ENDING FROM addExtraValidators(IValidator<String>... validators): %s]", validators));
        return this;
    }

    public Form<Void> getRegistrationForm() {
        LOG.info(String.format("[RETURNING FROM Form<Void> getRegistrationForm(): %s]", registrationForm));
        Arrays.asList(username, password, confPassword, send).forEach(registrationForm::add);
        return registrationForm;
    }

    public Optional<? extends MarkupContainer> setCompositionIntoAContainer(final Optional<? extends MarkupContainer> container) {
        LOG.info("[ENTERING Optional<? extends MarkupContainer> setCompositionIntoAContainer(final Optional<? extends MarkupContainer> container)]");
        
        if (!InstanceValidator.nullOrNonPresent(container)) {
            container.get().add(registrationForm);
            Arrays.asList(username, password, confPassword).forEach(registrationForm::add);
            return container;
        }

        LOG.info(String.format("[RETURNING FROM Optional<? extends MarkupContainer> setCompositionIntoAContainer(final Optional<? extends MarkupContainer> container):" 
         + "\n %s]", container));
        return container;
    }

    @Override
    public String toString() {
        return "UserRegistration [compModel=" + compModel + ", confPassword=" + confPassword + ", password=" + password
                + ", registrationForm=" + registrationForm + ", username=" + username  + ", id=" + id + "]";
    }
}