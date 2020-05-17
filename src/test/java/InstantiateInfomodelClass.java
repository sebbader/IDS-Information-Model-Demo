import de.fraunhofer.iais.eis.BaseConnector;
import de.fraunhofer.iais.eis.BaseConnectorBuilder;
import de.fraunhofer.iais.eis.CatalogBuilder;
import de.fraunhofer.iais.eis.SecurityProfile;
import de.fraunhofer.iais.eis.util.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static de.fraunhofer.iais.eis.util.Util.asList;

public class InstantiateInfomodelClass {

    private String inbound = "3.1.0";


    @Test(expected = ConstraintViolationException.class)
    public void createInvalidConnectorDescription() {
        new BaseConnectorBuilder().build();
    }

    @Test
    public void createConnectorDescription() {
        BaseConnector instantiatedConnectorDescription = new BaseConnectorBuilder()
                ._maintainer_(URI.create("http://www.iais.fraunhofer.de/"))
                ._curator_(URI.create("http://www.iais.fraunhofer.de/"))
                ._catalog_(new CatalogBuilder().build())
                ._outboundModelVersion_(inbound)
                ._inboundModelVersion_(asList(inbound))
                ._securityProfile_(SecurityProfile.BASE_CONNECTOR_SECURITY_PROFILE)
                .build();

        Assert.assertNotNull(instantiatedConnectorDescription);
    }

}
