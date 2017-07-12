# ASM-Ext: An Extension to ASM

## Introduction
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
best of our knowledge it features the most accurate control-flow construction algortihm, we are not going to call
this programming work a research contribution; this is just a warm up exercise of mine before joining Software
Evolution and Testing (SET) lab at UT Dallas.

As said above, ASM-Ext is a high-level analysis framework, by this we mean uisng ASM-Ext, in order to do control
flow analysis and construct call-graph for an object progrm, all you need to do is to specify the name of
its main class, and the path on which the class files reside. Similarly, in order to do a data-flow analysis
all you need to do is to specify an instanceof Monotone Framework. Thanks to the facilities provided by
Java 8, such a specification will be super easy. Pleade note that the functionality of ASM-Ext is highly
extensible, e.g. one can write his/her desired fixed-point algorithm.

### Organization

In this report, first we are goig to review packages of the ASM-Ext, then through a number of examples, we
will show how does call-graph analysis and control-flow graph construction of ASM-Ext works. Next we will do a
classic data-flow analysis using ASM-Ext. Finally, we will describe the difficulties that one may face if
they want to implement a control flow graph construction program for an object-oriented programming language
like Java.

## Packages

In this section, we are going to review the purpose of each package making up ASM-Ext. The package "asmext,"
as the name suggests, is the package that contains the main class of the program. In this package there
are classes to model Java classes, class members such as fields and methods. The package also contains
classes for modeling different kinds of data types available in Java. The main class of ASM-Ext, receives
the name of the main class of the object program through its first command line argument. Other parameters
such as class path are provided through properties. The main class loads all classes that are directly or
indirectly referenced by the main class of the object program, construsts class heirarchy of the program,
and constructs its call graph. It is worth mentioning that the call-graph construction algorithm is based on
simple CHA, an indeed constructs a gross approximation of the real call-graph of the object program. While
ASM-Ext loads application and library classes, it does a very lightweighted abstract interpretation on the
method bodies to infer the type of parameters fed to certian kind of bytecode instructions. This type
information proves very useful in constructing accurate control-flow graphs.

Inside "asmext," the package "asmext.instruction" contains abstractions for Java bytecode instructions. We
have grouped Java bytecode instructions based on their functionality, e.g. all load instructions are modeled
by the interface "Load." From our previous experience of using Soot framework, we know that using such
abstractions makes an analysis framework safe (in that programming under such a framework becomes less error
prone) and more user-friendly. Among the instructions the class "NeutralInst" is used in control-flow graph
construction to create "dummy" head nodes. Such dummy nodes do not correspond to any bytecode instruction
inside the body of a method, rather they are added to control-flow graphs to make single-headed,
easy-to-deal-with control-flow graphs. This package contains over 80 classes, and the root of all them is
the interface `Inst`, which specifies the most general interface that is applicable in the case of any Java
bytecode instruction, thereby enabling us treat them in a uniform manner.

When one needs to convert Java bytecodes to their corresponding abstract representation (e.g. during
construction of control-flow graphs), instantiating the correct class based on the opcode of a bytecode
instruction is a tedious and error prone task. The class `InstFactory` inside the package
`asmext.instruction.factory` abstracts this process by providing a "builder" that by recieving an
specification of the instruction to be created, instantiates the `Inst` class that correctly models the
specified bytecode instruction.

The package `controlflow` contains three sub-packages `controlflow.callgraph`, `controlflow.cfg`, and
`controlflow.analysis'. We describe these packages in the following paragraphs.

The interface `CallGraph` in the package `controlflow.callgraph` defines the general interface of any
call-graph. As it is expected, any call-graph must, at least, have two method to query the methods that
each method may call, and the methods that may be called at each call site. These functionality is provided
by the two overloads of `mayCall` method.

Inside the package `controlflow.cfg`, the class `CFG` models control-flow graphs whose nodes are `BasicBlocks`
and whose edges represent flow of control from source basic block to destination basic block. The class has
methods to query successors or predecessors of each basic block. It also has methods to retrieve heads
(which is always a single basic block in our case) and tails of the control-flow graph. By definition head
is any node that does not have any predecessor and tail is any node that lacks successor. The class also
offers a method `dot` to print out the graph in the form that can be fed to GraphViz program so as to 
produce a graphical representation of the graph. Any basic block is either an `InstSeq` (which represents
sequence of instructions) or a `TryCatchBlock` (which represents a specification of a try-catch-block inside
a method body). Although one may desire to insert specifications of try-catch blocks inside control-flow
graphs they contruct, our control-flow graph construction algorithm does not include `TryCatchBlock`s inside
the graphs it builds.

The package `controlflow.analysis' contains two important classes `CHA` and `CFGBuilder` that are responsible
for control flow analysis. The class `CHA` offers a method `doCFA` that builds a call graph of the object
program based on class hierarchy information that is gathered during class loading. The class `CFGBuilder`
receives a method, and construct a control-flow graph for the method. The control-flow graph has not only
normal flows of control, but also contains so-called exceptional control flows. As we shall describe in the
next sections, building a control-flow graph in the presence of exception can be tricky. The algorithm
implemented in `CFGBuilder` is very accurate in that, in comparison with the control-flow graphs produced
by Soot, it contains less spurious edges. Removing such false edges, that are necessary in order to have a
sound analysis, deteriorates the precision of any analysis based on the graphs.

The package `dataflow`, as the name suggests, contains classes necessary for data-flow analysis. The class
`MonotoneFramework` represents instances of Monotone Framework. Each instance of `MonotoneFramework` is a
sextuple (`cfg`, `dep`, `initializer`, `lte`, `join`, `transferFunction`) where

* `cfg` is a reference to the control-flow graph of the method to be analyzed.
* `dep` is a two place function that given the `cfg` and a node should return its successors or predecessors
(depending on direction of the analysis which might be "forward" or "backward").
* `initializer` is a function that given an extremal node (head or tail depending on direction of the
analysis) should return the initial value for the analysis.
* `lte` is a two-place predicate that should represent less-than-or-equal relation on the lattice based on
which we are conducting our analysis.
* `join` is a binary function that (depending on the nature of the analysis that might be "may" or "must")
instructs how to join the data coming from different branchs of the `cfg`.
* `transferFunction` represents the transfer function, it does the (abstract) interpretation by transforming
input data to output data based on the kind of instruction being met.

The class `MonotoneFramework` is parameterized with the type of lattice on which the analysis is based. The
contains the method `doAnalysis` that given a fixed-point computation strategy finds the maximal fixed-point
solution (aka MFP). Please note that MFP is a misnomer and is present in the static analysis literature due
to historical reasons; actually a solution to an analysis might be minimal fixed-point. The fixed-point 
algorithm shipped with ASM-Ext is general enough to handle both cases.

Fixed-point computation strategy is modeled by the abstract class `FixedPointAlgorithm` which is parameterized
with the type of lattice on which the analysis is based. Any fixed-point computation algorithm in order to be
compatible with our framework, needs to override the abstract method `perform`. Given an instance of
`MonotoneFramework`, the method should return an MFP solution. ASM-Ext is shipped with the class `Worklist`
which contains a general implementation of Worklist algorithm, a popular fixed-point algorithm with decent
efficiency.

The interface `SemiLattice` specifies a general interface of a semi-lattice (aka join semi-lattice; a partially
ordered set with pair of join and meet operations that yield least upper bound and greatest lower bound, resp.,
for each pair of elements). The interface `FiniteSet` is used to represent finite semi-lattices. The class
`FiniteHashSet` is an implementation of `FiniteSet` based on Java's `HashSet`, and `BitVectorSet` is an
implementation of the interface based on bit vectors.

Finally, the package `util` contains a number of useful classes on which our implementation depends. The class
`BiDiGraph_Int` represent a directed graph. The implementation of the class is both fast (as it is purely
based on integer manipulation) and space-efficient (as it chooses to use `byte`s or `int`s depending on the
size of graph). The class `BitVector`, as the name suggests, implements a bit vector. Bit vectors are useful
in many analyzes, and it is used (for some other purpose) in several places in our implementation of ASM-Ext.
The parameterized class `ImmutablePair` represents pairs. Lastly, the class `Utilities` offers two methods
for printing collections.

## Future Work

In later versions of ASM-Ext, once we build a points-to analysis for it, we shall refine the call-graph by
consulting points-to information which can in turn refine points-to analysis by providing it with a more
accurate call graph. 

