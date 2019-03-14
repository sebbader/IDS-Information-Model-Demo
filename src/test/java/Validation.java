import de.fraunhofer.iais.eis.*;
import de.fraunhofer.iais.eis.util.ConstraintViolationException;
import org.junit.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.GregorianCalendar;

import static de.fraunhofer.iais.eis.util.Util.asList;

public class Validation {

    @Test(expected = ConstraintViolationException.class)
    public void violateCustomURLValidation() throws MalformedURLException {
        new BaseConnectorBuilder()
                ._maintainer_(new URL("http://www.iais.fraunhofer.de/"))
                ._curator_(new URL("http://www.iais.fraunhofer.de/unknownTarget"))
                ._catalog_(new CatalogBuilder().build())
                ._outboundModelVersion_("1.0.2")
                ._inboundModelVersions_(asList("1.0.2"))
                .build();
    }

    @Test(expected = ConstraintViolationException.class)
    public void violateSecurityTokenValidation() throws MalformedURLException, DatatypeConfigurationException {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        XMLGregorianCalendar now = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);

        new ConnectorAvailableMessageBuilder()
                ._issuerConnector_(new URL("http://www.iais.fraunhofer.de"))
                ._issued_(now)
                ._modelVersion_("1.0.2")
                ._securityToken_(new TokenBuilder()
                        ._tokenValue_("akjfsajfaskfjdsaf")
                        ._tokenFormat_(TokenFormat.JWT)
                        .build())
                .build();
    }
}
