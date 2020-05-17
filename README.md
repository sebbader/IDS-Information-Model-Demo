# Using the IDS Information Model Programmatically

This project demonstrates how to work with the Industrial Data Space (IDS) Information Model programmatically, using Java.
It consists of a [set of classes ](src/test/java) containing unit tests that show how to deal with aspects of instantiating
Infomodel classes, serializing, deserializing and validating them.  

## Background

The IDS Information Model is a formalization of the IDS' concepts as an [RDF](https://www.w3.org/RDF/) -based ontology. It can
be considered as a kind of data model, describing how these concepts (e.g., architectural components such as connectors, brokers) 
are characterized and how they relate to each other. Therefore, in order to correctly describe a specific connector (i.e., an "instance"),
the descriptions need to conform to the Information Model ontology.

Creating instances can be done by creating an RDF document, using properties and classes of the ontology in a syntactically and
logically consistent way. This requires learning the concept and ideas of RDF and [Linked Data](http://linkeddata.org/), which imposes
quite some effort on developers trying to get started with creating services for the IDS.

As an effort to quickly get development teams on board to contribute and access IDS services such as custom connectors, we
provide a mapping of the [Information Model Ontology](https://github.com/IndustrialDataSpace/InformationModel) to the Java programming
language in the form of a library. It supports instantiation of ontology classes as Java beans and to automatically convert
(serialize) them to an RDF representation. Using an additionally provided Java validation library makes sure that each instantiated
object conforms to the Information Model ontology.         

## Accessing and Integrating the Libraries

[Fraunhofer IAIS](https://www.iais.fraunhofer.de/) ([EIS](https://www.iais.fraunhofer.de/en/institute/departments/enterprise-information-systems.html) 
department) runs a Maven artifactory that serves these utility libraries for using the Information Model conveniently with the
Java programming language. It can be

* accessed directly [here](https://maven.iais.fraunhofer.de/artifactory/eis-ids-public/),
* or included in Maven-based project's ```pom.xml``` file like this:
```xml
<repositories>
        <repository>
            <id>eis-public-repo</id>
            <name>maven-public</name>
            <url>http://maven.iais.fraunhofer.de/artifactory/eis-ids-public</url>
        </repository>
        <repository>
            <id>eis-snapshot-repo</id>
            <name>maven-snapshot</name>
            <url>http://maven.iais.fraunhofer.de/artifactory/eis-ids-snapshot</url>
        </repository>
</repositories>
```
Note that the `eis-snapshot-repo` is containing development releases and requires a further username and password.

The Information Model Java library can then be included in your ```dependencies``` section in this way: 
```xml
<dependency>
    <groupId>de.fraunhofer.iais.eis.ids.infomodel</groupId>
    <artifactId>java</artifactId>
    <version>3.1.0</version>
</dependency>
``` 

It is also highly recommended to include the following dependency:
```xml
<dependency>
    <groupId>de.fraunhofer.iais.eis.ids.infomodel</groupId>
    <artifactId>validation-serialization-provider</artifactId>
    <version>3.1.1</version>
</dependency>
```
Its job is to provide methods to validate Information Model objects and to serialize them when they should be transferred
over a network connection. These topics are described in more detail below.

## Basic Functionality

Each OWL class in the ontology is represented by an identically named interface in the Java library. In addition, implementations
of these interfaces are provided as classes with ```Impl``` prefix. So, for instance, the class ```Catalog```, which is defined as
```ids:Catalog a owl:Class``` in the Information Model ontology, is represented as ```public interface Catalog``` in the file
```Catalog.class``` of the library and accompanied by a ```public class CatalogImpl implements Catalog``` that is defined
in the file ```CatalogImpl.class```.

### Object Instantiation

The easiest way to instantiate an Information Model class is to use the accompanying builder class. For instance, in order to
create a self-description document for a Connector, you can use the class ```BaseConnectorBuilder``` as documented in the 
method ```createConnectorDescription()``` in the file [InstantiateInfomodelClass.java](src/test/java/InstantiateInfomodelClass.java).

### Object Serialization and Deserialization 

The current recommedation is to express Information Model instances in the RDF Format when publishing, transferring or storing them.
A method to correctly serialize the instantiated Information Model objects is provided by the ```validation-serialization-provider```
dependency (see Section "Accessing and Integrating the Libraries").

The preferred serialization format is [JSON-LD](https://json-ld.org/). It is valid JSON with some additional supportive
attributes such as ```@context``` and ```@type```. However, if you're only working with the Java library, you don't need to
get into the details of this format. The library calls do the serialization (i.e., object -> JSON-LD) and deserialization 
(i.e., JSON-LD -> object) for you and are described below. 

#### Serialization

The main way is to directly invoke the ```Serializer``` class, as shown in method ```serializeToJsonLD_bySerializerCall()```
in file [DeserializeInstantiatedClass.java](src/test/java/DeserializeInstantiatedClass.java). 

There is also another way to serialize an object (see Section "Object Instantiation"). The object's
```toRdf()``` method as described in the method ```serializeToJsonLD_fromObject``` in file 
[SerializeInstantiatedClass.java](src/test/java/SerializeInstantiatedClass.java). 

#### Deserialization

The method ```deserialize()``` in the file [DeserializeInstantiatedClass.java](src/test/java/DeserializeInstantiatedClass.java)
shows how the ```Serializer``` class can be used to transform a JSON-LD representation of an Information Model instance
into its (Java) object representation.

## Validation

When using the builder classes to instantiate Information Model objects __and__ having the ```validation-serialization-provider```
included, a validation is performed when the ```.build()``` method is invoked. This is demonstrated by the method
```createInvalidConnectorDescription()``` in the file [InstantiateInfomodelClass.java](src/test/java/InstantiateInfomodelClass.java).
The example shows that calling ```.build()``` throws an exception because, for instance, mandatory properties such as
```maintainer``` are not set.

### Implementing Custom Validators

Out of the box, the validation logic provided by ```validation-serialization-provider``` uses only the ```NotNull``` and
```NotEmpty``` validation constraints from the [Java EE Validation API](https://docs.oracle.com/javaee/7/api/javax/validation/package-summary.html) (JSR-303).
In future versions of the Information Model Java library, we plan to support additional validation constraints. However,
validity of Information Model objects may depend on domain- or implementation-specific requirements so that this validation
mechanism is open for extension.

Custom Information Model validation logic can be integrated into the validation workflow so that it is called on
invocation of the ```.build()``` method that is provided by each object builder class. Technically, this implementation
can be done by using Java's [Service provider interface](https://docs.oracle.com/javase/tutorial/ext/basics/spi.html) (SPI). 
According to the SPI specification, projects need to specify the interfaces they implement. An example can be found
in the [resources](src/main/resources) directory of this project, defining in the [services](src/main/resources) directory 
a mapping of interfaces and their concrete implementation.

In the example, the file [de.fraunhofer.iais.eis.spi.BeanValidator](src/main/resources/META-INF/services/de.fraunhofer.iais.eis.spi.BeanValidator)
states that the interface ```de.fraunhofer.iais.eis.spi.BeanValidator``` is implemented by the class 
```de.fraunhofer.iais.eis.validate.CustomBeanValidator```.

In the file [Validation.java](src/test/java/Validation.java), we provide two different examples that show how the default 
```NotNull``` and ```NotEmpty``` object validations can be extended with further functionality. The method

* ```violateCustomURLValidation()``` shows that a validation exception is thrown because of undereferencable URLs, and
* ```violateSecurityTokenValidation()``` illustrates how validation can be bound to a specific object (i.e., a ```Token``` instance).

Based on the SPI-related declarations in the ```resources``` directory described above, the builders' ```build()``` methods, that 
are called in the two mentioned methods, invoke the ```validate()``` method declared in the file [CustomBeanValidator.java](src/main/java/de/fraunhofer/iais/eis/validate/CustomBeanValidator.java),
which in turn delegates to the URL and Token validation logic.

## Usage Examples

### Supporting the IDS Messaging Communication Paradigm

Perhaps the easiest example of an IDS message that each connector should understand is the message of type ```ids:DescriptionRequestMessage```. It does not
require any payload part and can be created using the information model library as shown in the method ```selfDescriptionRequest()``` in the file
[Messaging.java](src/test/java/Messaging.java). In order to send this message to connector that supports the synchronous HTTP API, the serialized
instance of the ```DescriptionRequestMessage``` class needs to be sent as HTTP POST to the ```/infrastructure``` method of the receiving connector. The
post body itself should be of content-type ```multipart/form-data``` or ```multipart/mixed```, e.g.,

```
multipart/form-data; boundary=--------------------------949567736778671771657427
```

with this exemplary raw content:
  
```
----------------------------949567736778671771657427
Content-Disposition: form-data; name="header"

{
  "@context" : "https://w3id.org/idsa/contexts/context.jsonld",
  "@type" : "ids:DescriptionRequestMessage",
  "modelVersion" : "3.1.0",
  "issued" : "2020-05-21T13:32:33.073+02:00",
  "issuerConnector" : "http://example.org#connector",
  "@id" : "https://w3id.org/idsa/autogen/selfDescriptionRequest/b0731661-7df1-43e5-bb75-50f0709f31c9"
}
----------------------------949567736778671771657427--
```
 
However, in this minimal example, the receiving connector cannot verify the authenticity of the requesting connector, i.e., it cannot
determine if the sender is (still) a valid participant on the IDS ecosystem and if facts that it claims about itself in its self-description
are actually trustworthy. 
In order to provide this kind of security, each request needs to be accompanied by security token (aka 'DAPS token'). A connector that is in possession of 
this token may present it at each outgoing message exchange so that the receiver can analyze it and decide whether to accept the message or
deny it. Note that it is up to the receiving connector to handle and interpret the security token. The receiver may as well ignore the security token in
case it does not perform any security-relevant data processing.
The header part of the above example with included security token looks like this:  

```
----------------------------949567736778671771657427
Content-Disposition: form-data; name="header"

{
  "@context" : "https://w3id.org/idsa/contexts/context.jsonld",
  "@type" : "ids:DescriptionRequestMessage",
  "modelVersion" : "3.1.0",
  "issued" : "2020-05-21T13:32:33.073+02:00",
  "issuerConnector" : "http://example.org#connector",
  "securityToken" : {
      "@type" : "ids:Token",
      "tokenValue" : "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6ImRlZmF1bHQifQ.eyJ...",
      "tokenFormat" : {
        "@id" : "https://w3id.org/idsa/code/tokenformat/JWT"
      },
      "@id" : "https://w3id.org/idsa/autogen/token/58bf58a2-5a1b-44cb-85eb-9b4eecf5bf58"
    },
  "@id" : "https://w3id.org/idsa/autogen/selfDescriptionRequest/b0731661-7df1-43e5-bb75-50f0709f31c9"
}
----------------------------949567736778671771657427--
```

The receiving connector's response to the message above looks like this: 

```
--mPLw1UTMYjqqYqh1Bb_ttWBKcdSPfB9FBgz3
Content-Disposition: form-data; name="header"
Content-Type: application/json
Content-Length: 409

{
  "@type" : "ids:DescriptionResponseMessage",
  "issued" : "2020-05-21T13:11:14.596Z",
  "issuerConnector" : "https://broker.ids.isst.fraunhofer.de/",
  "correlationMessage" : "https://w3id.org/idsa/autogen/selfDescriptionRequest/b0731661-7df1-43e5-bb75-50f0709f31c9",
  "modelVersion" : "3.1.0",
  "@id" : "https://w3id.org/idsa/autogen/selfDescriptionResponse/851e3218-2bb7-45f9-8795-7f99c1f19680"
}
--mPLw1UTMYjqqYqh1Bb_ttWBKcdSPfB9FBgz3
Content-Disposition: form-data; name="payload"
Content-Type: application/ld+json
Content-Length: 965

{
  "@context" : "https://w3id.org/idsa/contexts/context.jsonld",
  "@type" : "ids:Broker",
  "outboundModelVersion" : "3.1.0",
  "description" : [ {
    "@value" : "A Broker with a graph persistence layer",
    "@language" : "en"
  } ],
  "inboundModelVersion" : [ "3.1.0" ],
  "title" : [ {
    "@value" : "EIS Broker",
    "@language" : "en"
  } ],
  "maintainer" : "https://www.iais.fraunhofer.de",
  "curator" : "https://www.iais.fraunhofer.de",
  "catalog" : {
    "@type" : "ids:Catalog",
    "@id" : "https://w3id.org/idsa/autogen/catalog/a50c93a3-388b-4bbd-959e-a446b7ec2946"
  },
  "securityProfile" : {
    "@type" : "ids:SecurityProfile",
    "basedOn" : {
      "@type" : "ids:SecurityProfile",
      "@id" : "https://w3id.org/idsa/core/Level0SecurityProfile"
    },
    "@id" : "https://w3id.org/idsa/autogen/securityProfile/cca6c0e6-ad34-4d66-8137-ab94e3fad424"
  },
  "@id" : "https://broker.ids.isst.fraunhofer.de/"
}
--mPLw1UTMYjqqYqh1Bb_ttWBKcdSPfB9FBgz3--
```
 
<!--
### The Information Model JSON-LD Serialization Format

As described above, [JSON-LD](https://json-ld.org/) extends JSON by several attributes. One of them is ```@context``` which basically maps human readable names to IRIs. So basically, a ```@context``` shared between different parties can serve as a vocabulary for the exchanged JSON data. You can find more detailed information in the [specification](https://json-ld.org/spec/latest/json-ld/) ('The Context').

[Fraunhofer IAIS](https://www.iais.fraunhofer.de/) ([EIS](https://www.iais.fraunhofer.de/en/institute/departments/enterprise-information-systems.html) department) provides a ```@context```-file for the IndustrialDataSpace ontology class names at https://w3id.org/idsa/contexts/context.jsonld.

It can be included into hand-written JSON-LD as follows:
```json
{
  ...
  "@context" : "https://w3id.org/idsa/contexts/context.jsonld",
  ...
}

TODO: the "@id" field, serialization of literals (language tags), typed literals, enums, whats the used timestamp format

```
-->

## References

* [IDS Information Model](https://github.com/IndustrialDataSpace/InformationModel)
* [Information Model Library Maven Repository](https://maven.iais.fraunhofer.de/artifactory/eis-ids-public/)

## Contact

For bug reports, issues or general help please write an email to [our bugtracking list](mailto:contact@ids.fraunhofer.de).

## Contributors

* Christian Mader (Fraunhofer IAIS)
* Benedikt Imbusch (Fraunhofer IAIS)
* Sebastian Bader (Fraunhofer IAIS)