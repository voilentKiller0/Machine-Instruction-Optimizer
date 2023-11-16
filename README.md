# Machine Instruction Optimizer

## Project Overview
The Machine Instruction Optimizer is a Java-based project designed to analyze machine-level instructions, identify data dependencies, and optimize the instruction sequence using various strategies. The project offers four optimization strategies:

1. Unscheduled Strategies
2. Scheduled Strategies
3. Loop Unrolling and Scheduled Strategies
4. Loop Unrolling and Unscheduled Strategies
   
The optimized machine-level code is then generated and saved to separate text files of every strategy.


## Table of Content
1. [Getting Started](#Getting_Started)
    * [Prerequisites](#Prerequisites)
    * [Installation](#Installation)
2. [Usage](#Usage)
    * [Input Format](#input)
    * [Instruction set available](#instrAvail)
    * [Running the Program](#run)
    * [Output](#Output)
3. [Optimization Strategies Description](#Optimization_Strategies)
    * [Unscheduled Strategies](#Unscheduled)
    * [Scheduled Strategies](#Scheduled)
    * [Loop Unrolling and Scheduled Strategies](#Loop_Scheduled)
    * [Loop Unrolling and Unscheduled Strategies](#Loop_Unscheduled)


## Getting Started <a name="Getting_Started"></a>

### Prerequisites <a name="Prerequisites"></a>

* Java Development Kit (JDK) installed on your machine.
* Text editor or Integrated Development Environment (IDE) for Java.

### Installation <a name="Installation"></a>

Clone the repository to your local machine:

```
  git clone https://github.com/your-username/machine-instruction-optimizer.git
```

## Usage <a name="Usage"></a>

### Input Format <a name="input"></a>

The program accepts machine-level instructions in a specific format. Ensure your input file follows the format outlined in the Testcase.txt document.

 ![image](https://user-images.githubusercontent.com/55941465/163449846-6890b412-b857-4aec-849d-68a9a1d4f10a.png)

### Instruction set available <a name="instrAvail"></a>

There are two types of Instruction Available in this program

1. Assume a simplified MIPS processor with the following floating point instructions:

```
1. add.d FRdest, FRsrc1, FRsrc2      ;Floating Point Addition Double
2. add.s FRdest, FRsrc1, FRsrc2      ;Floating Point Addition Single
3. l.d   FRdest, address             ;Load Floating Point Double
4. l.s   FRdest, address             ;Load Floating Point Single
5. s.d   FRsrc, address              ;Store Floating Point Double
6. s.s   FRsrc, address              ;Store Floating Point Single
```

2. Assume the following integer instructions are available: 

```
1. add.uIRdest, IRsrc1, IRsrc2        ;Integer Addition (32-bits)
2. add.iIRdest, IRsrc1, Immediate     ;Integer Addition with Immediate (16-bits)
3. sub.uIRdest, IRsrc1, IRsrc2        ;Integer Subtraction(32-bits)
4. sub.iIRdest, IRsrc1, Immediate     ;Integer Subtractionwith Immediate (16-bits)
5. bne          IRsrc1, IRsrc2,Loop   ;branch to Loop if IRsrc1!=IRsrc2
6. beqzIRsrc1,  Loop                  ;branch to Loop if IRsrc1=0
7. lwIRdest,    address               ;Load Integer Word
8. swIRsrc,     address               ;StoreInteger Word
9. mv           IRdest, IRsrc         ;Integer data movement
10. or          IRdest, IRsrc1, IRsrc2;Integer OR operation(32-bits)
```

### Running the Program <a name="run"></a>

1. Navigate to the project directory:

```
  cd machine-instruction-optimizer
```

2. Compile the Java files:

```
  javac src/Main.java
```

3. Run the program with the input file and chosen optimization strategy:
```
  java Main
```


### Output <a name="Output"></a>

  It gives the data dependency between instruction in output terminal and also it gives four text file of different scheduling 
  
  ![image](https://user-images.githubusercontent.com/55941465/163450506-fd5d9908-ac8d-47c0-b1d4-aef22209adcd.png)
  ![image](https://user-images.githubusercontent.com/55941465/163450603-9ebb7344-9f28-4c3a-8d2c-51b91a746200.png)
  
  Created Files 
  
  ![image](https://user-images.githubusercontent.com/55941465/163452067-55f6efab-8b88-4db8-aa15-eaa5f1a57312.png)
  

## Optimization Strategies Description <a name="Optimization_Strategies"></a>

Certainly! Below are brief descriptions for each optimization strategy mentioned in this project:

### 1. Unscheduled Strategies <a name="Unscheduled"></a>

**Description:**
Unscheduled strategies aim to optimize machine-level instructions by rearranging them without adhering to a fixed schedule. This approach focuses on minimizing pipeline stalls and improving instruction throughput by reordering instructions based on data dependencies.

**Implementation:**
The algorithm scans the instruction sequence, identifies dependencies, and rearranges instructions to reduce idle time in the pipeline, thus enhancing the overall execution speed.

### 2. Scheduled Strategies <a name="Scheduled"></a>

**Description:**
Scheduled strategies involve organizing machine-level instructions according to a predetermined schedule, taking into account the pipeline stages and execution units of the target architecture. This strategy aims to improve instruction-level parallelism and maximize resource utilization.

**Implementation:**
Instructions are scheduled based on their dependencies and the available execution resources. The scheduler ensures that instructions are placed in slots that minimize data hazards and make optimal use of the pipeline.

### 3. Loop Unrolling and Scheduled Strategies <a name="Loop_Scheduled"></a>

**Description:**
This strategy combines loop unrolling and scheduled optimization techniques. Loop unrolling involves replicating loop bodies to reduce branch overhead, and scheduled optimization is applied to the unrolled loop to enhance instruction-level parallelism.

**Implementation:**
The loop is unrolled to expose more instruction-level parallelism, and then scheduled optimization is applied to the unrolled code. This synergistic approach aims to improve both loop-level and instruction-level performance.

### 4. Loop Unrolling and Unscheduled Strategies <a name="Loop_Unscheduled"></a>

**Description:**
Similar to the previous strategy, this approach combines loop unrolling with unscheduled optimization. Loop unrolling reduces loop overhead, and unscheduled optimization focuses on reordering instructions without adhering to a fixed schedule.

**Implementation:**
The loop is unrolled to minimize loop-related delays, and then unscheduled optimization is applied to the unrolled code. This strategy aims to strike a balance between loop-level and instruction-level optimizations.

These strategies offer different trade-offs and can be chosen based on the characteristics of the target architecture and the nature of the input machine-level code.



 **Made By :
      1. Chandreshwar Vishwakarma**


