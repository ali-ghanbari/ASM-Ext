#ASM-Ext: An Extension to ASM

##Overview
In this section we are going to review ASM-Ext. We are calling this project ASM-Ext for it is an extension
to ASM and is based on ASM. ASM-Ext allows high level data and control flow analysis. This tool proves to be
useful in research project when one wishes to quickly write a prototype static analysis to test his/her
algorithms.

For those who are not familiar with ASM, ASM is a general, low-level transformation and analysis framework
for Java bytecode. Java bytecodes are used not only for Java but a number of other programming langauges,
notably Scala, also compiles to Java bytecodes. Since ASM provides its users with only primitive facilities,
it can be used to analyze or transform any Java bytecode file regardless of the source language according to
which the file is produced. We refer the readers to the official website of ASM (asm.objectweb.org) so as to
get more information about this useful analysis/transformation framework.

As we stated above, ASM features primitive facilities that enable its users analyze or transform general JVM
bytecode files. These primitive tools are, though fast, hard to use for research purposes where we need to
quickly implement and test different static analysis algorithms. There are many "high-level" analysis frameworks
out there but ASM-Ext is the first analysis framework written on top of ASM which will be our main tool
during my PhD studies when I want to implement some algorithm. When compared to Soot framework, ASM-Ext runs
faster, but this is not a virtue as Soot does a rather complicated transformation before presenting the object
program to the programmer, and there are a handful of rival analyses that also run fast. So even though, to the
best of our knowledge it features the most accurate control flow construction algortihm, we are not going to call
this programming work a research contribution; this is just a warm up exercise of mine before joining Software
Evolution and Testing (SET) lab at UT Dallas.

As said above, ASM-Ext is a high-level analysis framework, by this I mean uisng ASM-Ext, in order to do control
flow analysis and construct call-graph for an object progrm, all you need to do is to specify the name of
its main class, and the path on which the class files reside. Similarly, in order to do a data-flow analysis
all you need to do is to specify an instanceof Monotone Framework. Thanks to the facilities provided by
Java 8, such a specification will be super easy. Pleade note that extending ASM does not mean that we have
restricted flexibility 

In this video, first I am goig to review packages of the ASM-Ext, then through a number of examples, I shall
show how does call graph analysis and control flow graph construction of ASM-Ext works. Next we will do a
classic data-flow analysis using ASM-Ext. Finally, I shall describe the difficulties that one may face if
they want to implement a control flow graph construction program for an object-oriented programming language
like Java.

Here in Eclipse, I want to review the purpose of each package making up ASM-Ext. This package ("asmext")
as the name suggests, is the package that contains the main class of the program. In this package there
are classes to model Java classes, class members such as fields and methods. The package also contains
classes for modeling different kinds of data types available in Java. The main class of ASM-Ext, receives
the name of the main class of the object program through its first command line argument. Other parameters
such as class path are provided through system properties. The main class, loads all classes that are directly
or indirectly referenced by the main class of the object program, construsts class heirarchy of the program,
and constructs its call-graph. It is worth mentioning that the call graph construction algorithm is based on
simple CHA, an indeed constructs a gross approximation of the real call-graph of the object program. In later
versions of ASM-Ext, once I built a points-to analysis for it, I shall refine the call-graph by consulting
points-to information which can in turn refine points-to analysis by providing it with a more accurate
call-graph. While ASM-Ext loads application and library classes, it does a very lightweighted abstract
interpretation on the method bodies to infer the type of parameters fed to certian kind of bytecode instructions.

