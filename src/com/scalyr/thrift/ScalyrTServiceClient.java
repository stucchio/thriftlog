package com.scalyr.thrift;

import org.apache.thrift.*;
import org.apache.thrift.protocol.TMessage;
import org.apache.thrift.protocol.TMessageType;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.TApplicationException;

public abstract class ScalyrTServiceClient extends TServiceClient{

    public ScalyrTServiceClient(TProtocol prot) {
	super(prot);
    }

    public ScalyrTServiceClient(TProtocol iprot, TProtocol oprot) {
	super(iprot, oprot);
    }

    protected void sendBase(String methodName, ScalyrTaggedTBase args) throws TException {
	System.out.println("Client: calling " + args.getMessageId());
        oprot_.writeMessageBegin(new TMessage(methodName, TMessageType.CALL, ++seqid_));
        args.write(oprot_);
        oprot_.writeMessageEnd();
        oprot_.getTransport().flush();
    }

    protected void receiveBase(ScalyrTaggedTBase result, String methodName) throws TException {
        TMessage msg = iprot_.readMessageBegin();
        if (msg.type == TMessageType.EXCEPTION) {
            TApplicationException x = TApplicationException.read(iprot_);
            iprot_.readMessageEnd();
            throw x;
        }
        if (msg.seqid != seqid_) {
            throw new TApplicationException(TApplicationException.BAD_SEQUENCE_ID, methodName + " failed: out of sequence response");
        }
        result.read(iprot_);
        iprot_.readMessageEnd();
	System.out.println("Client: received " + result.getMessageId());
    }

}