Type Serialization
==================

It is often required to be able to send a Java generic type over a
serialization protocol. While many serializers support sending
classes by name, far fewer support the full generic type structure.
This library addresses that need.

It may be obtained from Maven central as
* org.anarres.typeserializer:typeserializer-core
* org.anarres.typeserializer:typeserializer-simplexml
* org.anarres.typeserializer:typeserializer-kryo

If you want to contribute a plugin for another serialization engine,
please do so. If you need one but are not sure how to write it,
please ask.

API Documentation
-----------------

The [JavaDoc is available](http://shevek.github.io/typeserializer/docs/javadoc/).

typeserializer-core
-------------------

This module contains the two main routines:
* `String TypeSerializer.serialize(Type)`
* `Type TypeSerializer.deserialize(String)`

It also contains a generic `TypeVisitor` which may be used to walk a
generic Type.

typeserializer-kryo
-------------------

This provides an additional registerable Serializer for Kryo:

	Kryo kryo = new Kryo();	// As usual.
	kryo.addDefaultSerializer(Type.class, new KryoTypeSerializer());

typeserializer-simplexml
------------------------

This provides an additional registerable Serializer for SimpleXML:

	(example using TypeConverter and TypeTransform while avoiding
	using a custom RegistryMatcher... which is what I do...)

Building
--------

This is a standard gradle build: Run

    ./gradlew build

For more details, see the [gradle-stdproject-plugin cheatsheet](//github.com/shevek/gradle-stdproject-plugin/CHEATSHEET.md).

