error id: +KCPL8UFQKiEbxUp9HHlcg==
### Bloop error:

Unexpected error when compiling quadtreeproject: java.nio.file.InvalidPathException: Illegal char <
> at index 92: not setting the location of system modules may lead to class files that cannot run on JDK 21
    --release 21 is recommended instead of -source 21 -target 21 because it sets the location of system modules automatically
<WORKSPACE>\src\main\java\module-info.java
	at java.base/sun.nio.fs.WindowsPathParser.normalize(WindowsPathParser.java:191)
	at java.base/sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:142)
	at java.base/sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:46)
	at java.base/sun.nio.fs.WindowsPath.parse(WindowsPath.java:92)
	at java.base/sun.nio.fs.WindowsFileSystem.getPath(WindowsFileSystem.java:203)
	at java.base/java.io.File.toPath(File.java:2099)
	at bloop.logging.BspServerLogger.diagnostic(BspServerLogger.scala:207)
	at bloop.reporter.BspProjectReporter.logFull(BspProjectReporter.scala:66)
	at bloop.reporter.Reporter.log(Reporter.scala:164)
	at bloop.reporter.ObservedReporter.log(ObservedReporter.scala:20)
	at sbt.internal.inc.javac.JavacLogger.$anonfun$flush$1(JavacProcessLogger.scala:58)
	at sbt.internal.inc.javac.JavacLogger.$anonfun$flush$1$adapted(JavacProcessLogger.scala:58)
	at scala.collection.immutable.List.foreach(List.scala:431)
	at sbt.internal.inc.javac.JavacLogger.flush(JavacProcessLogger.scala:58)
	at sbt.internal.inc.javac.BloopForkedJavaUtils$.$anonfun$launch$3(BloopForkedJavaUtils.scala:36)
	at sbt.internal.inc.javac.BloopForkedJavaUtils$.$anonfun$launch$3$adapted(BloopForkedJavaUtils.scala:28)
	at sbt.internal.inc.javac.ForkedJava$.$anonfun$withArgumentFile$1(ForkedJava.scala:77)
	at sbt.io.IO$.withTemporaryDirectory(IO.scala:496)
	at sbt.io.IO$.withTemporaryDirectory(IO.scala:506)
	at sbt.internal.inc.javac.ForkedJava$.withArgumentFile(ForkedJava.scala:74)
	at sbt.internal.inc.javac.BloopForkedJavaUtils$.launch(BloopForkedJavaUtils.scala:28)
	at bloop.CompilerCache$BloopForkedJavaCompiler.run(CompilerCache.scala:221)
	at sbt.internal.inc.javac.AnalyzingJavaCompiler.$anonfun$compile$12(AnalyzingJavaCompiler.scala:173)
	at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.java:23)
	at sbt.internal.inc.javac.AnalyzingJavaCompiler.timed(AnalyzingJavaCompiler.scala:263)
	at sbt.internal.inc.javac.AnalyzingJavaCompiler.compile(AnalyzingJavaCompiler.scala:161)
	at sbt.internal.inc.bloop.internal.BloopHighLevelCompiler.$anonfun$compile$11(BloopHighLevelCompiler.scala:197)
	at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.java:23)
	at sbt.internal.inc.bloop.internal.BloopHighLevelCompiler.$anonfun$compile$1(BloopHighLevelCompiler.scala:73)
	at bloop.tracing.NoopTracer$.trace(BraveTracer.scala:53)
	at sbt.internal.inc.bloop.internal.BloopHighLevelCompiler.timed$1(BloopHighLevelCompiler.scala:72)
	at sbt.internal.inc.bloop.internal.BloopHighLevelCompiler.$anonfun$compile$10(BloopHighLevelCompiler.scala:190)
	at scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.java:23)
	at monix.eval.internal.TaskRunLoop$.startFull(TaskRunLoop.scala:81)
	at monix.eval.internal.TaskRestartCallback.syncOnSuccess(TaskRestartCallback.scala:101)
	at monix.eval.internal.TaskRestartCallback.onSuccess(TaskRestartCallback.scala:74)
	at monix.eval.internal.TaskExecuteOn$AsyncRegister$$anon$1.run(TaskExecuteOn.scala:71)
	at java.base/java.util.concurrent.ForkJoinTask$RunnableExecuteAction.compute(ForkJoinTask.java:1750)
	at java.base/java.util.concurrent.ForkJoinTask$RunnableExecuteAction.compute(ForkJoinTask.java:1742)
	at java.base/java.util.concurrent.ForkJoinTask$InterruptibleTask.exec(ForkJoinTask.java:1659)
	at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:511)
	at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1450)
	at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:2019)
	at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:187)
#### Short summary: 

Unexpected error when compiling quadtreeproject: java.nio.file.InvalidPathException: Illegal char <
...