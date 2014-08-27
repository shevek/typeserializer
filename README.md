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

serializer-core
---------------

This module contains the two main routines:
* String TypeSerializer.serialize(Type)
* Type TypeSerializer.deserialize(String)

