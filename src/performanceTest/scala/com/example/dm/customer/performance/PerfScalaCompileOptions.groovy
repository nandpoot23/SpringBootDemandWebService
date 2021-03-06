package com.example.dm.customer.performance;

public class PerfScalaCompileOptions {
    def List<String> scala_jvmargs = ["-server", "-XX:+UseThreadPriorities", "-XX:ThreadPriorityPolicy=42", "-Xms512M",
                                      "-Xmx512M", "-Xmn100M", "-XX:+HeapDumpOnOutOfMemoryError", "-XX:+AggressiveOpts",
                                      "-XX:+OptimizeStringConcat", "-XX:+UseFastAccessorMethods", "-XX:+UseParNewGC",
                                      "-XX:+UseConcMarkSweepGC", "-XX:+CMSParallelRemarkEnabled",
                                      "-Djava.net.preferIPv4Stack=true", "-Djava.net.preferIPv6Addresses=false"]
}