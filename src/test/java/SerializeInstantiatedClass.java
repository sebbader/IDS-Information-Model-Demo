import com.fasterxml.jackson.core.JsonProcessingException;
import de.fraunhofer.iais.eis.*;
import de.fraunhofer.iais.eis.ids.jsonld.Serializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static de.fraunhofer.iais.eis.util.Util.asList;

public class SerializeInstantiatedClass {

    private Serializer serializer = new Serializer();
    private BaseConnector baseConnector;

    @Before
    public void setUp() throws MalformedURLException {
        baseConnector = new BaseConnectorBuilder()
            ._maintainer_(new URL("http://www.iais.fraunhofer.de/"))
            ._curator_(new URL("http://www.iais.fraunhofer.de/"))
            ._catalog_(new CatalogBuilder().build())
            ._outboundModelVersion_("1.0.2")
            ._inboundModelVersions_(asList("1.0.2"))
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
