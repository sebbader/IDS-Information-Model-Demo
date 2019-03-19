import de.fraunhofer.iais.eis.BaseConnector;
import de.fraunhofer.iais.eis.BaseConnectorBuilder;
import de.fraunhofer.iais.eis.CatalogBuilder;
import de.fraunhofer.iais.eis.util.ConstraintViolationException;
import org.junit.Test;
import org.springframework.util.Assert;

import java.net.MalformedURLException;
import java.net.URL;

import static de.fraunhofer.iais.eis.util.Util.asList;

public class InstantiateInfomodelClass {

    @Test(expected = ConstraintViolationException.class)
    public void createInvalidConnectorDescription() {
        new BaseConnectorBuilder().build();
    }

    @Test
    public void createConnectorDescription() throws MalformedURLException {
        BaseConnector instantiatedConnectorDescription = new BaseConnectorBuilder()
                ._maintainer_(new URL("http://www.iais.fraunhofer.de/"))
                ._curator_(new URL("http://www.iais.fraunhofer.de/"))
                ._catalog_(new CatalogBuilder().build())
                ._outboundModelVersion_("1.0.2")
                ._inboundModelVersions_(asList("1.0.2"))
                .build();

        Assert.notNull(instantiatedConnectorDescription);
    }

}
