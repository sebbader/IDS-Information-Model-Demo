import de.fraunhofer.iais.eis.*;
import de.fraunhofer.iais.eis.util.ConstraintViolationException;
import org.junit.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.net.URI;
import java.util.Date;
import java.util.GregorianCalendar;

import static de.fraunhofer.iais.eis.util.Util.asList;

public class Validation {

    private String inbound = "3.1.0";

    @Test(expected = ConstraintViolationException.class)
    public void violateCustomURLValidation()  {
        new BaseConnectorBuilder()
                ._maintainer_(URI.create("http://www.iais.fraunhofer.de/"))
                ._curator_(URI.create("http://www.iais.fraunhofer.de/unknownTarget"))
                ._catalog_(new CatalogBuilder().build())
                ._outboundModelVersion_(inbound)
                ._inboundModelVersion_(asList(inbound))
                .build();
    }

    @Test(expected = ConstraintViolationException.class)
    public void violateSecurityTokenValidation() throws DatatypeConfigurationException {
        System.setProperty("doValidateToken", "true");

        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        XMLGregorianCalendar now = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

        new ConnectorAvailableMessageBuilder()
                ._issuerConnector_(URI.create("http://www.iais.fraunhofer.de"))
                ._issued_(now)
                ._modelVersion_(inbound)
                ._securityToken_(new DynamicAttributeTokenBuilder()
                        ._tokenValue_("akjfsajfaskfjdsaf")
                        ._tokenFormat_(TokenFormat.JWT)
                        .build())
                .build();
    }
}
