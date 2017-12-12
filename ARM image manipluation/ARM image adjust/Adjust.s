	AREA	Adjust, CODE, READONLY
	IMPORT	main
	IMPORT	getPicAddr
	IMPORT	putPic
	IMPORT	getPicWidth
	IMPORT	getPicHeight
	IMPORT	getkey
	IMPORT	sendchar
	IMPORT 	fputs
	EXPORT	start
	PRESERVE8 {true}

start

	LDR	R0, =brightnessMes	; outputRegister = opening message
	BL 	fputs				; send opening message to console
	MOV R4,#0 				; boolean storeContrast = false;
	MOV R2,#0				; runningTotal = 0;
	MOV R3,#10				; multiplier = 10;
	MOV R6,#0				; sign = ''
read				; 
	BL	getkey		; read key from console
	CMP	R0, #0x0D  	; while (key != CR)
	BEQ	endRead		; {	
	BL	sendchar	;   send char to user
	CMP R0, #0x20	; 	if(key != space)
	BEQ nextNum		;	{
	CMP R0,#0x2D	; 		if(key == '-'){	
	BNE pos			;			sign = '-'
	MOV R6,R0		; 			//branch to start if while
	B read			; 		}
pos					;
	CMP R4,#1		;		if(!storeContrast){
	BEQ contrast	; 		//branch to get the contrast}
	MUL R10,R3,R10	;		int brightness = brightness * multiplier
	SUB R0,R0,#0x30	;		//convert next int from ASCII to decimal
	ADD R10,R10,R0	;		brightness += nextInt
	B read			; }
contrast			;
	MUL R11,R3,R11	;	int contrast = contrast * multiplier
	SUB R0,R0,#0x30 ;	//convert next int from ASCII to decimal
	ADD R11,R11,R0  ;	contrast += nextInt
	B	read		; }	

nextNum				;//branch to read the next number
	CMP R6,#0x2D	; if(sign == '-'){
	BNE posInt		;   Invert bits of x
	MVN R10,R10		; 	brightness = brightness + 1
	ADD R10,R10,#1	;   sign = ''
	MOV R6,#0		;  }
posInt				; 
	MOV R2,#0		;	runningTotal = 0
	ADD R4,R4,#1	;	storeContrast = true
	B read			; }
	
endRead				;//when getting input is finished
	CMP R6,#0x2D	; if(sign == '-'){
	BNE begin		; Invert bits of contrast
	MVN R11,R11		; contrast = contrast + 1
	ADD R11,R11,#1	; }

	;//User input code above only works with simulator otherwise we have to comment out the above code and manually load in the vales
	;MOV R10,#0			;brightness
	;MOV R11,#5			;contrast

begin	
	BL	getPicAddr		; load the start address of the image 
	MOV	R4, R0
	BL	getPicHeight	; load the height of the image (rows) 
	MOV	R5, R0
	BL	getPicWidth		; load the width of the image (columns)
	MOV	R6, R0
	

	LDR R7,= -1	 		;rowCount = -1
forRow					; for(int row = 0; row <= imageHeight; row++){
	ADD R7,R7,#1		;	rowCount++
	LDR R9,=0 			;	columnCount = 0
	CMP R7,R5			;	
	BEQ finish			;
forColumn 				;		for(int column = 0; column < imageWidth; column++)
	CMP R9,R6			;		{
	BEQ forRow			;			//passing local varaibles as parmeters	
	MOV R1,R7			;			row
	MOV R2,R9			;			column	
	MOV R3,R6			;			width
	STMFD sp!, {R4}		;			passing the image address as a parameter by pushing it onto a stack
	BL getPixel			;			currentPixel = getPixel(row,column,width,imageAddress)
	MOV R1,R0 			; 			passing parameters- currentPixel
	MOV R2,R10			;			brightness
	MOV R3,R11			;			contrast
	BL updatePixel		;			updatePixel(brightness,contrast,currentPixel);
	;//setting updated pixel
	STMFD sp!, {R4}		; 			push imageAddress onto stack to use as parameter
	MOV R1,R7			;			row
	MOV R2,R9			;			column
	MOV R3,R6			;			width
	BL setPixel			;			setPixel(currentPixel,row,column,width,imageAddress)
	ADD R9,R9,#1		;			columnCount++
	b forColumn			;}


;getRed subroutine
;Returns the red component of a given pixel
;Parameters :
;R3 : current Pixel
;Returns : R0 : Value of red component
getRed						
	STMFD sp!, {R4}			;backing up registers using the stack
	MOV R4,R3				;currentPixel
	AND R4,R4,#0x00FF0000	;isolating red bits
	MOV R0,R4,LSR#16		;moving red into least significant bytes
	LDMFD sp!, {R4}			;taking registers off stack
	BX lr					;branch to link address
	
	
;getGreen subroutine
;Returns the green component of a given pixel
;Parameters :
;R3 : currentPixel
;Returns : R0 : Value of green component
getGreen
	STMFD sp!, {R4}			;backing up registers using the stack
	MOV R4,R3				;currentPixel
	AND R4,R4,#0x0000FF00	;isolating green bits
	MOV R0,R4,LSR#8			;moving green into least significant bytes
	LDMFD sp!, {R4}			;taking registers off stack
	BX lr					;branch to link address
	
	
;getBlue subroutine
;Returns the blue component of a given pixel
;Parameters :
;R3: currentPixel
;Returns : R0 : Value of blue component
getBlue						
	STMFD sp!, {R4}			;backing up registers using the stack
	MOV R4,R3				;currentPixel
	AND R0,R4,#0x000000FF	;isolating blue bits
	LDMFD sp!, {R4}			;taking registers off stack
	BX lr					;branch to link address

;getPixel subroutine
;Returns a given pixel in an array
;Parameters:
;R1 : row of pixel
;R2 : column of pixel
;R3 : width of array
;R4	: imageAddress
;Return R0 : any given pixel
getPixel		
	LDMFD sp!, {R4}			;current pixel parameter pushed off stack
	STMFD sp!, {R4-R8}		;backing up registers using the stack
	MOV R6,R1				;rowOfPixel
	MOV R5,R4				;imageAddress
	MOV R7,R2				;columnOdPixel
	MOV R8,R3				;width
	MUL R6,R8,R6			;pixelPosition = row * arrayWidth
	ADD R6,R6,R7			;pixelPosition = pixelPosition + column
	LDR R0, [R5,R6,LSL#2]	;currentPixel = imageAdress[PixelPosition * 4]
	LDMFD sp!, {R4-R8}		;taking registers off stack
	BX lr					;branch to link address
		
;setRed subroutine
;Sets the red value of a given pixel
;Parameters
;R1 : redValue
;R3 : currentPixel
;Returns : R0 : current pixel with updated red value
setRed
	STMFD sp!, {R7,R8}		;backing up registers using the stack
	MOV R7,R1   			;load red value into local variable
	MOV R8,R3				;load pixel into local variable
	MOV R7,R7,LSL#16		;Moving red value into correct position
	AND R8,R8,#0xFF00FFFF 	;clear red bits
	ORR R0,R8,R7			;setting red bits
	LDMFD sp!, {R7,R8}		;taking registers off stack
	BX lr					;branch to link address
	
;setGreen subroutine
;Sets the green value of a given pixel
;Parameters
;R1 : greenValue
;R3 : currentPixel
;Returns : R0 : current pixel with updated green value
setGreen
	STMFD sp!, {R7,R8}		;backing up registers using the stack
	MOV R7,R1   			;load green value into local variable
	MOV R8,R3				;load pixel into local variable
	MOV R7,R7,LSL#8			;Moving green value into correct position
	AND R8,R8,#0xFFFF00FF 	;clear green bits
	ORR R0,R8,R7			;setting green bits
	LDMFD sp!, {R7,R8}		;taking registers off stack
	BX lr					;branch to link address
	
;setBlue subroutine
;Sets the blue value of a given pixel
;Parameters
;R1 : blueValue
;R3 : currentPixel
;Returns : R0 : current pixel with updated blue value	
setBlue
	STMFD sp!, {R7,R8}		;backing up registers using the stack
	MOV R7,R1   			;load blue value into local variable
	MOV R8,R3				;load pixel into loacal variable
	AND R8,R8,#0xFFFFFF00 	;clear blue bits
	ORR R0,R8,R7			;setting blue bits
	LDMFD sp!, {R7,R8}		;taking registers off stack
	BX lr					;branch to link address
	
;setPixel
;sets a particular pixel in memory to its new form
;Parameters:
;R0 : currentPixel
;R1 : row of pixel
;R2 : column of pixel
;R3 : width of array
;Returns : this subroutine doesnt return anything
setPixel
	LDMFD sp!, {R4}			;imageAddress parameter pushed off stack
	STMFD sp!, {R4-R9}      ;backing up registers using the stack
	MOV R5,R4				;address
	MOV R6,R1				;rowOfPixel
	MOV R7,R2				;columnOfPixel
	MOV R8,R3				;widthOfArray
	MOV R9,R0				;currentPixel
	MUL R8,R6,R8			;pixelPosition = rowOfPixel * arrayWidth
	ADD R8,R8,R7			;pixelPosition = pixelPosition + columnOfPixel
	STR R0,[R5,R8,LSL#2]	;memory.word[address] = currentPixel
	LDMFD sp!, {R4-R9}		;taking registers off stack
	BX lr					;branch to link address



;correctValue subRoutine
;corrects red, green or blue value if its above the maximum value of 255 or below 0
;Parameters:
;R1 : Red, Green or Blue value to be corrected
;Return R1 : corrected value
correctValue				
	STMFD sp!, {R4}			;backing up registers using the stack
	MOV R4,R1				;Red, Green or Blue value
	CMP R4,#255				;if(value > 255)
	BLT negative			;{
	MOV R1,#255				;	value = 255
	LDMFD sp!, {R4}			;	taking registers off stack
	BX lr					;	branch to link address
negative					;else if(value < 0)
	MOV R1,#0				; 	value = 0
	LDMFD sp!, {R4}			;   taking registers off stack
	BX lr					;   branch to link address


;updatePixel
;Parameters:
;R1	: currentPixel
;R2	: brightness
;R3	: contrast
;Return : R0 : updatedPixel
updatePixel
	STMFD sp!,{R4-R7,lr};backing up registers using the stack
	MOV R5,R2			;	brightness		
	MOV R6,R3			;	contrast
	MOV R3,R1			;	currentPixel
	BL getRed			;	redValue = getRed(currentPixel)
	MOV R7,R0			;			
	MUL R7,R6,R7		;	redValue = redValue * contrast
	MOV R7,R7,LSR#4		;	redValue = redValue / 16
	ADD R7,R7,R5		;	redValue = redValue + brightness
	MOV R1,R7			;	passing redValue as parameter
	CMP R1, #255		;	if(redValue > 255)
	BLGT correctValue	;		correctValue(redValue)
	CMP R1,#0			;	else if (redValue < 0)		
	BLLT correctValue	;		correctValue(redValue)
	BL setRed			;	
	MOV R3,R0			;	currentPixel = setRed(redValue,currentPixel)

	BL  getGreen		;	greenValue = getGreen(currentPixel)
	MOV R7,R0			
	MUL R7,R6,R7		;	greenValue = greenValue * contrast
	MOV R7,R7,LSR#4		;	greenValue = greenValue / 16
	ADD R7,R7,R5		;	greenValue = greenValue + brightness
	MOV R1,R7			;	passing greenValue as parameter
	CMP R1, #255		;	if(greenValue > 255)
	BLGT correctValue	;		correctValue(greenValue)
	CMP R1,#0			;	else if (greenValue < 0)	
	BLLT correctValue	;		correctValue(greenValue)
	BL setGreen			;
	MOV R3,R0			;	currentPixel = setGreen(greenValue,currentPixel)

	BL getBlue			;   blueValue = getBlue(currentPixel)
	MOV R7,R0			;	
	MUL R7,R6,R7		;	blueValue = blueValue * contrast
	MOV R7,R7,LSR#4		;	blueValue = blueValue / 16
	ADD R7,R7,R5		;	blueValue = blueValue + brightness
	MOV R1,R7			;	passing blueValue as parameter
	CMP R1, #255		;	if(blueValue > 255)
	BLGT correctValue	;		correctValue(blueValue)
	CMP R1,#0			;	else if (greenValue < 0)	
	BLLT correctValue	;		correctValue(blueValue)
	BL setBlue			;
	LDMFD sp!, {R4-R7,lr};  taking registers off stack
	BX lr				;   branch to link address


finish					;
	BL	putPic			; re-display the updated image
stop	B	stop

	AREA	MyStrings, DATA, READONLY
brightnessMes		DCB	"Please enter the brightness and contrast seperated by a space: \n",0
	
	END	