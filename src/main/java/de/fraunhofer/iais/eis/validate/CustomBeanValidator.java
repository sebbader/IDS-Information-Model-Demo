package de.fraunhofer.iais.eis.validate;

import de.fraunhofer.iais.eis.Token;
import de.fraunhofer.iais.eis.spi.BeanValidator;
import de.fraunhofer.iais.eis.util.ConstraintViolationException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class CustomBeanValidator implements BeanValidator {

    public <T> void validate(T t) throws ConstraintViolationException {
        Collection<String> exceptionMessages = new ArrayList<>();

        Arrays.asList(t.getClass().getDeclaredFields()).forEach(field -> {

            try {
                if (field.getType().isAssignableFrom(URL.class) && !field.getName().equals("id")) {
                    field.setAccessible(true);

                        URL value = (URL) field.get(t);
                        if (value != null) {
                            boolean success = checkUrlResolvable(value);
                            if (!success) exceptionMessages.add("URL '" +value+ "' is unresolvable.");
                        }
                }

                if (field.getType().isAssignableFrom(Token.class)) {
                    field.setAccessible(true);
                    Token token = (Token) field.get(t);
                    if (token != null) {
                        boolean success = validateToken(token);
                        if (!success) exceptionMessages.add("Token '" +field.getName()+ "' could not be validated.");
                    }

                }
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        });

        if (!exceptionMessages.isEmpty()) {
            throw new ConstraintViolationException(exceptionMessages);
        }
    }

    private boolean checkUrlResolvable(URL url) {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet get = new HttpGet(url.toURI());
            return !String.valueOf(httpclient.execute(get).getStatusLine().getStatusCode()).startsWith("4");
        }
        catch (URISyntaxException | IOException e) {
            // intentionally ignore
        }

        return false;
    }

    private boolean validateToken(Token token) {
        // add the validation code here!
        return false;
    }
}
