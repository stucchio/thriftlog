#!/usr/bin/python

import sys
import re



if __name__=="__main__":
    filename = sys.argv[1]
    gencode = open(filename).read()

    gencode = gencode.replace("package com.scalyr.thrift.gen;", "package com.scalyr.thrift.gen;\n\nimport com.scalyr.thrift.*;") #Import scalyr thrift deps

    #Replace skip with skip or set message id
    gencode = gencode.replace("org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type)", "struct.setMessageIdFromFieldOrSkip(iprot, schemeField)")

    #Add write message id code to write method of args
    write_method_regex = re.compile("public void write\\(org.apache.thrift.protocol.TProtocol oprot, ([A-Za-z_]+) struct\\)\\s*throws\\s*org.apache.thrift.TException\\s*{\\n"\
                                    "\\s*struct.validate\\(\\);(\\n\\s*)+"\
                                    "oprot.writeStructBegin\\(STRUCT_DESC\\);", re.MULTILINE)
    gencode = re.sub(write_method_regex, "public void write(org.apache.thrift.protocol.TProtocol oprot, \\1 struct) throws org.apache.thrift.TException {\n"\
                     "      struct.validate();\n\n"\
                     "      oprot.writeStructBegin(STRUCT_DESC);\\n"\
                     "      struct.writeMessageId(oprot);\\n", gencode)

    #Delete empty constructors
    empty_constructor_regex = re.compile("public ([a-zA-Z_0-9]+)\\(\\)\s*{\\n\\s*}", re.MULTILINE)
    gencode = re.sub(empty_constructor_regex, "public \\1() {\n      super();\n    }\n", gencode)

    # Replace TBase with ScalyrTaggedTBase
    tbase_regexp = re.compile("public static class ([A-Za-z_0-9]+) implements org.apache.thrift.TBase<([A-Za-z_0-9]+), ([A-Za-z_0-9]+)._Fields>, java.io.Serializable, Cloneable")
    gencode = re.sub(tbase_regexp, "public static class \\1 extends ScalyrTaggedTBase<\\2,\\3._Fields> implements java.io.Serializable, Cloneable", gencode)
    gencode = re.sub(re.compile("org.apache.thrift.TBase([^a-zA-Z])"), "ScalyrTaggedTBase\\1", gencode);


    gencode = gencode.replace("org.apache.thrift.ProcessFunction", "com.scalyr.thrift.ScalyrProcessFunction")
    gencode = gencode.replace("org.apache.thrift.TBaseProcessor", "com.scalyr.thrift.ScalyrTBaseProcessor")
    gencode = re.sub(re.compile("org.apache.thrift.TServiceClient([^a-zA-Z])"), "ScalyrTServiceClient\\1", gencode)

    open(filename, 'w').write(gencode)
