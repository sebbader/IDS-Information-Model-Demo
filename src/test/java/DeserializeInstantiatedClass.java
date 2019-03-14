import de.fraunhofer.iais.eis.BaseConnector;
import de.fraunhofer.iais.eis.InfrastructureComponent;
import de.fraunhofer.iais.eis.ids.jsonld.Serializer;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class DeserializeInstantiatedClass {

    private Serializer serializer = new Serializer();

    @Test
    public void deserialize() throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(this.getClass().getResourceAsStream("BaseConnector.jsonld"), writer);
        String serializedConnectorDescription = writer.toString();

        InfrastructureComponent deserialized = serializer.deserialize(serializedConnectorDescription, InfrastructureComponent.class);

        Assert.assertNotNull(deserialized);
        Assert.assertTrue(deserialized instanceof BaseConnector);
    }

}
