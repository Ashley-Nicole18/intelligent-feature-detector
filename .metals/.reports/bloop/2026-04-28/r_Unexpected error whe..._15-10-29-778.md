error id: 2nwwVxNYJAgpwXCvr1qBkQ==
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
	at bloop.logging.BspServerLogger.noDiagnostic(BspServerLogger.scala:285)
	at bloop.reporter.BspProjectReporter.$anonfun$reportRemainingProblems$1(BspProjectReporter.scala:236)
	at bloop.reporter.BspProjectReporter.$anonfun$reportRemainingProblems$1$adapted(BspProjectReporter.scala:232)
	at scala.collection.immutable.Map$Map1.foreach(Map.scala:193)
	at bloop.reporter.BspProjectReporter.reportRemainingProblems(BspProjectReporter.scala:232)
	at bloop.reporter.BspProjectReporter.processEndPreviousCycle(BspProjectReporter.scala:158)
	at bloop.reporter.BspProjectReporter.processEndCompilation(BspProjectReporter.scala:404)
	at bloop.reporter.ObservedReporter.processEndCompilation(ObservedReporter.scala:77)
	at bloop.Compiler$.$anonfun$compile$6(Compiler.scala:775)
	at bloop.task.Task.$anonfun$runAsync$7(Task.scala:265)
	at monix.eval.Task$Map.apply(Task.scala:4604)
	at monix.eval.Task$Map.apply(Task.scala:4600)
	at monix.eval.internal.TaskRunLoop$.startFull(TaskRunLoop.scala:170)
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