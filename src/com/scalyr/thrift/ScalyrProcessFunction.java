package com.scalyr.thrift;

import org.apache.thrift.*;
import org.apache.thrift.protocol.TMessage;
import org.apache.thrift.protocol.TMessageType;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolException;

public abstract class ScalyrProcessFunction<I,T extends ScalyrTaggedTBase> {
    private final String methodName;

    public ScalyrProcessFunction(String methodName) {
        this.methodName = methodName;
    }

    protected abstract ScalyrTaggedTBase getResult(I iface, T args) throws TException;

    protected abstract T getEmptyArgsInstance();

    public String getMethodName() {
        return methodName;
    }

  public final void process(int seqid, TProtocol iprot, TProtocol oprot, I iface) throws TException {
    T args = getEmptyArgsInstance();
    try {
      args.read(iprot);
    } catch (TProtocolException e) {
      iprot.readMessageEnd();
      TApplicationException x = new TApplicationException(TApplicationException.PROTOCOL_ERROR, e.getMessage());
      oprot.writeMessageBegin(new TMessage(getMethodName(), TMessageType.EXCEPTION, seqid));
      x.write(oprot);
      oprot.writeMessageEnd();
      oprot.getTransport().flush();
      return;
    }
    iprot.readMessageEnd();
    System.out.println("Server: received " + args.getMessageId());
    ScalyrTaggedTBase result = getResult(iface, args);
    result.setMessageId(args.getMessageId());
    System.out.println("Server: sent " + result.getMessageId());
    oprot.writeMessageBegin(new TMessage(getMethodName(), TMessageType.REPLY, seqid));
    result.write(oprot);
    oprot.writeMessageEnd();
    oprot.getTransport().flush();
  }
}