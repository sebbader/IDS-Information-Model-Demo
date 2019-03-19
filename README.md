# Using the IDS Information Model Programmatically

This project demonstrates how to work with the Industrial Data Space (IDS) Information Model programmatically, using Java.
It consists of a [set of classes ](src/test/java) containing unit tests that show how to deal with aspects of instantiating
Infomodel classes, serializing, deserializing and validating them.  

## Background

The IDS Information Model is a formalization of the IDS' concepts as an [RDF](https://www.w3.org/RDF/)-based ontology. It can
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
       <id>eis-snapshot-repo</id>
       <name>maven-snapshots</name>
       <url>http://maven.iais.fraunhofer.de/artifactory/eis-ids-public</url>
   </repository>
</repositories>
```

The Information Model Java library can then be included in your ```dependencies``` section in this way: 
```xml
<dependency>
    <groupId>de.fraunhofer.iais.eis.ids.infomodel</groupId>
    <artifactId>java</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>
``` 

It is also highly recommended to include the following dependency:
```xml
<dependency>
    <groupId>de.fraunhofer.iais.eis.ids.infomodel</groupId>
    <artifactId>validation-serialization-provider</artifactId>
    <version>1.0.2-SNAPSHOT</version>
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

There are two ways to serialize an object (see Section "Object Instantiation"). The easiest one is to call the object's
```toRdf()``` method as described in the method ```serializeToJsonLD_fromObject``` in file 
[SerializeInstantiatedClass.java](src/test/java/SerializeInstantiatedClass.java). 

The second (alternative) way is to directly invoke the ```Serializer``` class, as shown in method ```serializeToJsonLD_bySerializerCall()```
in file [DeserializeInstantiatedClass.java](src/test/java/DeserializeInstantiatedClass.java). 

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

todo: spi documentation 

[Validation.java](src/test/java/Validation.java). 

<!--
## For those that don't like Java...

todo: describe how the project is platform-independent

### The Information Model JSON-LD Serialization Format

todo: describe how objects are serialized and deserialized to/from JSON-LD
-->

## References

* [IDS Information Model](https://github.com/IndustrialDataSpace/InformationModel)
* [Information Model Library Maven Repository](https://maven.iais.fraunhofer.de/artifactory/eis-ids-public/)

## Contributors

* Christian Mader (Fraunhofer IAIS)
* Benedikt Imbusch (Fraunhofer IAIS)