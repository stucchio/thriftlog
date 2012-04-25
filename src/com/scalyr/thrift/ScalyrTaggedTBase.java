package com.scalyr.thrift;

import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TType;
import org.apache.thrift.TException;
import org.apache.thrift.TBase;
import org.apache.thrift.TFieldIdEnum;

import java.util.UUID;

public abstract class ScalyrTaggedTBase<T extends TBase<?,?>, F extends TFieldIdEnum> implements TBase<T,F> {
    public static int SCALYR_MESSAGE_IDENTIFIER_FIELD_ID = -29437;
    private static final org.apache.thrift.protocol.TField MESSAGE_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("messageid", org.apache.thrift.protocol.TType.STRING, (short)SCALYR_MESSAGE_IDENTIFIER_FIELD_ID);

    private String messageId = null;

    public ScalyrTaggedTBase() {
	messageId = UUID.randomUUID().toString();
    }

    public String getMessageId(){
	return messageId;
    }

    public void setMessageId(String id) {
	messageId = id;
    }

    public void writeMessageId(TProtocol oprot) throws TException {
	if (getMessageId() != null) {
	    oprot.writeFieldBegin(MESSAGE_ID_FIELD_DESC);
	    oprot.writeString(getMessageId());
	    oprot.writeFieldEnd();
	}
    }

    public void setMessageIdFromFieldOrSkip(TProtocol iprot, TField schemaField) throws TException {
	// This function reads the next field from iprot. If the field is a Scalyr message id, we assign it.
	// Otherwise we skip it.
	if (schemaField.id == SCALYR_MESSAGE_IDENTIFIER_FIELD_ID && schemaField.type == TType.STRING) {
	    setMessageId(iprot.readString());
	} else {
	    org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemaField.type);
	}
    }
}