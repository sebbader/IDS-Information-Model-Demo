import de.fraunhofer.iais.eis.SelfDescriptionRequest;
import de.fraunhofer.iais.eis.SelfDescriptionRequestBuilder;
import de.fraunhofer.iais.eis.TokenBuilder;
import de.fraunhofer.iais.eis.TokenFormat;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.GregorianCalendar;

public class Messaging {

    @Test
    public void selfDescriptionRequest() throws MalformedURLException, DatatypeConfigurationException {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        XMLGregorianCalendar now = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

        SelfDescriptionRequest selfDescriptionRequest = new SelfDescriptionRequestBuilder()
                ._issuerConnector_(new URL("http://example.org#connector"))
                ._issued_(now)
                ._modelVersion_("1.0.2")
                .build();

        Assert.assertFalse(selfDescriptionRequest.toRdf().isEmpty());
    }

    @Test
    public void selfDescriptionRequestWithDapsToken() throws MalformedURLException, DatatypeConfigurationException {
        System.setProperty("doValidateToken", "false");

        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        XMLGregorianCalendar now = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

        SelfDescriptionRequest selfDescriptionRequest = new SelfDescriptionRequestBuilder()
                ._issuerConnector_(new URL("http://example.org#connector"))
                ._issued_(now)
                ._modelVersion_("1.0.2")
                ._securityToken_(new TokenBuilder()
                        ._tokenFormat_(TokenFormat.JWT)
                        ._tokenValue_("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImRlZmF1bHQifQ.eyJ...")
                        .build())
                .build();

        Assert.assertFalse(selfDescriptionRequest.toRdf().isEmpty());
    }

}
