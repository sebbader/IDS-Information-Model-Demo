import com.fasterxml.jackson.core.JsonProcessingException;
import de.fraunhofer.iais.eis.BaseConnector;
import de.fraunhofer.iais.eis.BaseConnectorBuilder;
import de.fraunhofer.iais.eis.CatalogBuilder;
import de.fraunhofer.iais.eis.SecurityProfile;
import de.fraunhofer.iais.eis.ids.jsonld.Serializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

import static de.fraunhofer.iais.eis.util.Util.asList;

public class SerializeInstantiatedClass {

    private Serializer serializer = new Serializer();
    private BaseConnector baseConnector;
    private String inbound = "3.1.0";

    @Before
    public void setUp() {
        baseConnector = new BaseConnectorBuilder()
            ._maintainer_(URI.create("http://www.iais.fraunhofer.de/"))
            ._curator_(URI.create("http://www.iais.fraunhofer.de/"))
            ._catalog_(new CatalogBuilder().build())
            ._securityProfile_(SecurityProfile.BASE_CONNECTOR_SECURITY_PROFILE)
            ._outboundModelVersion_(inbound)
            ._inboundModelVersion_(asList(inbound))
            .build();
    }

    @Test
    public void serializeToJsonLD_fromObject() {
        String serializedJsonLD = baseConnector.toRdf();

        Assert.assertTrue(serializedJsonLD.contains("@id"));
        Assert.assertTrue(serializedJsonLD.contains("@context"));
    }

    @Test
    public void serializeToJsonLD_bySerializerCall() throws IOException {
        String serializedJsonLD = serializer.serialize(baseConnector);

        Assert.assertTrue(serializedJsonLD.contains("@id"));
        Assert.assertTrue(serializedJsonLD.contains("@context"));
    }

    @Test
    public void serializeToJson() throws JsonProcessingException {
        String serializedJson = serializer.serializePlainJson(baseConnector);

        Assert.assertFalse(serializedJson.contains("@context"));
        Assert.assertTrue(serializedJson.contains("@id"));
    }

}
