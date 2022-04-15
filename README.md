# High-Performance-Computer-Architecture

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

# Input
  It will take the file path as input.
  ![image](https://user-images.githubusercontent.com/55941465/163449846-6890b412-b857-4aec-849d-68a9a1d4f10a.png)

# Output
  It gives the data dependency between instruction in output terminal and also it gives four text file of different scheduling 
  
  ![image](https://user-images.githubusercontent.com/55941465/163450506-fd5d9908-ac8d-47c0-b1d4-aef22209adcd.png)
  ![image](https://user-images.githubusercontent.com/55941465/163450603-9ebb7344-9f28-4c3a-8d2c-51b91a746200.png)
  
  Created Files 
  
  ![image](https://user-images.githubusercontent.com/55941465/163452067-55f6efab-8b88-4db8-aa15-eaa5f1a57312.png)
  
  
 **Made By :
      1. Chandreshwar Vishwakarma**


