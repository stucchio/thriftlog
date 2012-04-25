package com.scalyr.thrift.test;
/*
  To run this:
  $ ant compile-tests
  $ java -cp build/classes/:build/scalyr_thrift.jar:/usr/local/lib/libthrift-0.8.0.jar:/usr/local/lib/slf4j-api-1.5.8.jar:/usr/local/lib/slf4j-log4j12-1.5.8.jar:/usr/local/lib/log4j-1.2.14.jar com.scalyr.thrift.test.TestServer
 */
import com.scalyr.thrift.gen.ScalyrDemo;

import org.apache.thrift.protocol.*;
import org.apache.thrift.server.*;
import org.apache.thrift.transport.*;
import org.apache.thrift.TException;

import java.util.UUID;

class TestServer  {

    public TestServer() { }

    public static class ScalyrDemoServer implements ScalyrDemo.Iface {
	public String scalyrcall(String testString, byte testByte, int testInt, double testDouble) throws org.apache.thrift.TException {
	    System.out.println("Scalyr server received a client call.");
	    return UUID.randomUUID().toString();
	}
    }

    public static final int PORT = 7384;

    public static void main(String[] args) throws Exception {
	System.out.println("Starting");
	System.out.println("Starting server thread");
	(new Thread(new ThriftServerRunnable())).start();
	Thread.currentThread().sleep(250);
	(new Thread(new ThriftClientRunnable())).start();
    }

    private static class ThriftServerRunnable implements Runnable {
	public ThriftServerRunnable() throws TException {
	    System.out.println("Starting server on port " + PORT);
	    TServerSocket serverTransport = new TServerSocket(PORT);
	    ScalyrDemo.Processor processor = new ScalyrDemo.Processor(new ScalyrDemoServer());
	    server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
	    System.out.println("Server Started");
	}

	private final TServer server;

	public void run() {
	    server.serve();
	}
    }

    private static class ThriftClientRunnable implements Runnable {
	public ThriftClientRunnable() throws TException {
	    TTransport transport = new TSocket("localhost", PORT);
	    TProtocol protocol = new TBinaryProtocol(transport);
	    client = new ScalyrDemo.Client(protocol);
	    transport.open();
	}
	private final ScalyrDemo.Client client;

	public void run() {
	    System.out.println("Starting client thread");
	    for (int i=0;i<1;i++) {
		try {
		    String result = client.scalyrcall("foo", (byte)i, i, (double)i);
		} catch (TException e) {
		    System.out.println("Client received TException");
		    System.out.println(e);
		}
	    }
	    System.out.println("Client thread finished.");
	}
    }
}