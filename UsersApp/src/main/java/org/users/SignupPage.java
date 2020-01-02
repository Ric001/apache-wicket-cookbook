package org.users;

import java.util.Optional;
import java.util.logging.Logger;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.users.validators.UsernameValidator;


public class SignupPage extends WebPage { 

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(SignupPage.class.getName()); 
    
    public SignupPage(final PageParameters parameters) {
        super(parameters);
        add(new FeedbackPanel("feedback"));
        add(getSignupForm("signup"));
    }
    
    private Form<Void> getSignupForm(final String id) {
        LOG.info("[ENTERING Form<Void> getSignupForm(final String id)]");

        final CompoundPropertyModel<User> compModel = new CompoundPropertyModel<>(new User());
        final Optional<IModel<User>> opModel = Optional.of(compModel);
        final UserRegistration userRegistration = new UserRegistration(opModel, id);
        final Form<Void> signupForm = userRegistration
            .addExtraValidators(new UsernameValidator())
            .getRegistrationForm();
        
        LOG.info(String.format("[RETURNING FROM Form<Void> getSignupForm(final String id): %s]", signupForm));
        return signupForm;
    }

}