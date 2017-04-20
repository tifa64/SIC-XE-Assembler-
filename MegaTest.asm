COPY     START   0
FIRST    STL     RETADR            17202D
         LDB     #LENGTH           69202D
         BASE    LENGTH
CLOOP    +JSUB   RDREC             4B101036
         LDA     LENGTH            032026
         COMP    #0                290000
         JEQ     ENDFIL            332007
         +JSUB   WRREC             4B10105D
         J       CLOOP             3F2FEC
ENDFIL   LDA     EOF               032010
         STA     BUFFER            0F2016
         LDA     #3                010003
         STA     LENGTH            0F200D
         +JSUB   WRREC             4B10105D
         J       @RETADR           3E2003
EOF      BYTE    C’EOF’            454F46
RETADR   RESW    1
LENGTH   RESW    1
BUFFER   RESB    4096
.
. READ RECORD INTO BUFFER
.
RDREC    CLEAR   X                 B410
         CLEAR   A                 B400
         CLEAR   S                 B440
         +LDT    #4096             75101000
RLOOP    TD      INPUT             E32019
         JEQ     RLOOP             332FFA
         RD      INPUT             DB2013
         COMPR   A,S               A004
         JEQ     EXIT              332008
         STCH    BUFFER,X          57C003
         TIXR    T                 B850
         JLT     RLOOP             3B2FEA
EXIT     STX     LENGTH            134000
         RSUB                      4F0000
INPUT    BYTE    X'FI'             F1
.
. SUBROUTINE TO WRITE RECORD FROM BUFFER
.
WRREC    CLEAR   X                 B410
         LDT     LENGTH            774000
WLOOP    TD      OUTPUT            E32011
         JEQ     WLOOP             332FFA
         LDCH    BUFFER,X          53C003
         WD      OUTPUT            DF2008
         TIXR    T                 B850
         JLT     WLOOP             3B2FEF
         RSUB                      4F0000
OUTPUT   BYTE    X'05'             05
         END     FIRST