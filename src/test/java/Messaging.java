import de.fraunhofer.iais.eis.DescriptionRequestMessage;
import de.fraunhofer.iais.eis.DescriptionRequestMessageBuilder;
import de.fraunhofer.iais.eis.DynamicAttributeTokenBuilder;
import de.fraunhofer.iais.eis.TokenFormat;
import de.fraunhofer.iais.eis.ids.jsonld.Serializer;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.GregorianCalendar;

public class Messaging {

    private String inbound = "3.1.0";

    @Test
    public void selfDescriptionRequest() throws IOException, DatatypeConfigurationException {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        XMLGregorianCalendar now = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

        DescriptionRequestMessage selfDescriptionRequest = new DescriptionRequestMessageBuilder()
                ._issuerConnector_(URI.create("http://example.org#connector"))
                ._issued_(now)
                ._modelVersion_(inbound)
                ._securityToken_(new DynamicAttributeTokenBuilder()
                        ._tokenFormat_(TokenFormat.JWT)
                        ._tokenValue_("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImRlZmF1bHQifQ.eyJ...")
                        .build())
                ._senderAgent_(URI.create("http://example.org#senderAgent"))
                .build();

        Serializer serializer = new Serializer();
        Assert.assertFalse(serializer.serialize(selfDescriptionRequest).isEmpty());
    }

    @Test
    public void selfDescriptionRequestWithDapsToken() throws DatatypeConfigurationException, IOException {
        System.setProperty("doValidateToken", "false");

        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        XMLGregorianCalendar now = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

        DescriptionRequestMessage selfDescriptionRequest = new DescriptionRequestMessageBuilder()
                ._issuerConnector_(URI.create("http://example.org#connector"))
                ._issued_(now)
                ._modelVersion_(inbound)
                ._securityToken_(new DynamicAttributeTokenBuilder()
                        ._tokenFormat_(TokenFormat.JWT)
                        ._tokenValue_("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImRlZmF1bHQifQ.eyJ...")
                        .build())
                ._senderAgent_(URI.create("http://example.org#senderAgent"))
                .build();

        Serializer serializer = new Serializer();
        Assert.assertFalse(serializer.serialize(selfDescriptionRequest).isEmpty());
    }

}
