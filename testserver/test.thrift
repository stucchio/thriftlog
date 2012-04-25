namespace java com.scalyr.thrift.gen

service ScalyrDemo {
  string scalyrcall(1:string arg1, 2:byte arg2, 3:i32 arg3, 4:double arg4),
}