TERMPROJ START   3A0
.THIS IS A COMMENT LINE 
LBL1     BYTE    C'ABCDEF'
LBL2     RESB    4                 .Tagroba
LBL2     RESW    1
TOP      LDA     ZERO
         LDX     #INDEX
TOP      LDA     ZERO
         LDX     #INDEX
         ADDR    r1,r2
         +ADD    m
         FIX
         END     3A0