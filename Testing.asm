TERMPROJ START   3A0
.THIS IS A COMMENT LINE 
LBL1     BYTE    C'ABCDEF'
LBL2     RESB    4                 .Tagroba
LBL2     RESW    1
TOP      lda     ZERO
         LdX     #INDEX
TOP      LDA     ZERO
Test     LDX     #INDEX
         ADDR    r1,r2
         hi
         +ADD    m
         FIX
         END     3A0