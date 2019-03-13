package de.fraunhofer.iais.eis.validate;

import de.fraunhofer.iais.eis.spi.BeanValidator;
import de.fraunhofer.iais.eis.util.ConstraintViolationException;

public class CustomBeanValidator implements BeanValidator {

    public <T> void validate(T t) throws ConstraintViolationException {
        System.out.println(t.getClass().getName());

        // todo: dereference URL
        // todo: validate securitytoken
    }

}
