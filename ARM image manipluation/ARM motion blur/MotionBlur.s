	AREA	MotionBlur, CODE, READONLY
	IMPORT	main
	IMPORT	getPicAddr
	IMPORT	putPic
	IMPORT	getPicWidth
	IMPORT	getPicHeight
	EXPORT	start
	PRESERVE8 {true}
start

	BL	getPicAddr	; load the start address of the image in R4
	MOV	R4, R0
	BL	getPicHeight; load the height of the image (rows) in R5
	MOV	R5, R0
	BL	getPicWidth	; load the width of the image (columns) in R6
	MOV	R6, R0


	LDR R11,=10				;radius
	MOV R0,#0				;//clearing return register
	MOV R1,R11				;passing radius as parameter
	BL divide				;//finding half the radius
	MOV R3,R0				;halfRadius
	LDR R7,=-1				;rowCount
forRow						;for(int row = 0; row <= imageHeight; row++){
	ADD R7,R7,#1			;rowCount++
	MOV R8,#0				;columnCount =0
	CMP R7,R5				;
	BEQ finish				;
forColumn					;	for(int column = 0; column < imageWidth; column++)
	STMFD sp!, {R3}			;	//backing up registers on stack so they can be used
	CMP R8,R6				;	{
	BEQ forRow				;			
	MOV R1,R7				;		tempRow = row
	SUB R1,R1,R3			;		tempRow = tempRow - halfRadius
	MOV R2,R8				;		tempColumn = column
	SUB R2,R2,R3			;		parameter = parameter - halfRadius
	MOV R3,R6				;		width
	STMFD sp!,{R10,R7-R9}	;		//backing up registers on stack so they can be used
	MOV R10,#0				; 		pixelCount = 0
	MOV R9,#0				;		blueTotal = 0
	MOV R7,#0				;		redTotal = 0
	MOV R8,#0				;		greenTotal = 0
while						;    	while(pixelCount < radius)
	CMP R10,R11				;		{
	BEQ endWh				;			
	CMP R1,#0				;			if(tempRow < 0 || temRow > imageHeight)
	BLT skip				;			{
	CMP R1,R5				;				skipPixel
	BGE skip				;			}
	CMP R2,#0				;			else if(tempRow < 0 || tempRow > imageWidth)
	BLT skip				;			{
	CMP R2,R6				;				skipPixel
	BGE skip				;			}
	STMFD sp!, {R4}			;			passing ImageAddress as parameter
	MOV R3,R6				;			passing width as paramter
	BL getPixel				;			currentPixel = getPixel(row,column,width,ImageAddress)
	MOV R3,R0				;			
	b insideImage			;		
skip						;
	MOV R3,#0x00FFFFFF		;			//if pixel outside image assume its white
insideImage					;
	BL getRed				;			
	ADD R7,R7,R0			;			redTotal+= currentRed
	BL getGreen				;			
	ADD R8,R8,R0			;			greenTotal += currentGreen
	BL getBlue				;			
	ADD R9,R9,R0			;			blueTotal += currentBlue

	ADD R10,R10,#1			;			pixelCount++
	ADD R1,R1,#1			;			tempRow++
	ADD R2,R2,#1			;			tempColumn++
	b while					;	}

endWh						;
	MOV R2,R11				; 			passing radius as divisor
	MOV R1,R7				;			passing redTotal as parameter
	BL 	getAverage			;			getAverage(redTotal)
	MOV R1,R0				;			passing redAverage
	BL setRed				;			setRed()
	MOV R3,R0				;			updating currentPixel
							;			
	MOV R1,R8				;			passing greenTotal as parameter
	BL 	getAverage			;			getAverage(greenTotal)
	MOV R1,R0				;			passing	greenAverage
	BL setGreen				;			setGreen()
	MOV R3,R0				;			update currentPixel
							;
	MOV R1,R9				;			passing blueTotal as parameter	
	BL 	getAverage			;			getAverage(blueTotal)
	MOV R1,R0				;			passing blueAverage
	BL setBlue				;			setBlue()
							;
	LDMFD sp!, {R10,R7-R9}	;			//taking registers used off stack
	MOV R1,R7				;			passing row as parameter
	MOV R2,R8				;			passing column as parameter
	MOV R3,R6				;			passing width as parameter
	STMFD sp!, {R4}			;			passing imageAddress as parameter
	BL setPixel				;			setPixel(currentPixel,row,column,width,imageAddress)	
	ADD R8,R8,#1			;			column++	
	LDMFD sp!, {R3}			;			//taking registers used off stack
	b forColumn				;	}
	
;getPixel subroutine
;Returns a given pixel in an array
;Parameters:
;R1 : row of pixel
;R2 : column of pixel
;R3 : width of array
;R4: currentPixel
;Return R0 : any given pixel
getPixel		
	LDMFD sp!, {R4}			;current pixel parameter pushed off stack
	STMFD sp!, {R4-R8}		;backing up registers using the stack
	MOV R6,R1				;rowOfPixel
	MOV R5,R4				;imageAddress
	MOV R7,R2				;colummOfPixel
	MOV R8,R3				;widthOfImage
	MUL R6,R8,R6			;pixelPosition = row * arrayWidth
	ADD R6,R6,R7			;pixelPosition = pixelPosition + column
	LDR R0, [R5,R6,LSL#2]	;currentPixel = imageAdress[PixelPosition * 4]
	LDMFD sp!, {R4-R8}		;taking registers off stack
	BX lr					;branch to link address	
	
	
;getRed subroutine
;Returns the red component of a given pixel
;Parameters :
;R3 : current Pixel
;Returns : R0 : Value of red component
getRed						
	STMFD sp!, {R7}			;backing up registers using the stack
	MOV R7,R3				;currentPixel
	AND R7,R7,#0x00FF0000	;isolating red bits
	MOV R0,R7,LSR#16		;moving red into least significant bytes
	LDMFD sp!, {R7}			;taking registers off stack
	BX lr					;branch to link address	

;getGreen subroutine
;Returns the green component of a given pixel
;Parameters :
;R3 : currentPixel
;Returns : R0 : Value of green component
getGreen
	STMFD sp!, {R7}			;backing up registers using the stack
	MOV R7,R3				;currentPixel
	AND R7,R7,#0x0000FF00	;isolating green bits
	MOV R0,R7,LSR#8			;moving green into least significant bytes
	LDMFD sp!, {R7}			;taking registers off stack
	BX lr					;branch to link address
	
;getBlue subroutine
;Returns the blue component of a given pixel
;Parameters :
;R3: currentPixel
;Returns : R0 : Value of blue component
getBlue						
	STMFD sp!, {R7}			;backing up registers using the stack
	MOV R7,R3				;currentPixel
	AND R0,R7,#0x000000FF	;isolating blue bits
	LDMFD sp!, {R7}			;taking registers off stack
	BX lr					;branch to link address	
	
	
;getAverage subroutine
;Returns the average of a set of values
;Parameters:
;R1	: totalOfValues(remainder)
;R2 : divisor
;Return: R0 : averageResult
getAverage					
	STMFD sp!, {R5-R6}		;backing up registers using the stack
	MOV R5,R1				;remainder = totalOfValues
	MOV R6,R2				;divisor(b)	
	MOV R0,#0				;quotient
avWhile 					;
	CMP R5, R6				;	while( reamainder >= b)	
	BLT endwh				;	{
	SUB R5,R5,R6			;		remainder = remainder - b;
	ADD R0, R0, #1			;		quotient = quotient + 1;
	b avWhile				;	}
endwh						; 
	LDMFD sp!, {R5-R6}		;taking registers off stack
	BX lr					;branch to link address	
		
;setRed subroutine
;Sets the red value of a given pixel
;Parameters
;R1 : redValue
;R3 : currentPixel
;Returns : R0 : current pixel with updated red value
setRed
	STMFD sp!, {R4-R5,R7-R8,lr}	;backing up registers using the stack
	MOV R4,R1
	MOV R5,R3
	CMP R1,#0				;if(redValue < 0)
	BGE check				;{
	BL correctValue			;	redValue.correctValue();
check						;}
	CMP R4,#255				;if(redValue > 255
	BLE contRed				;{
	BL correctValue			;	redValue.correctValue();
contRed						;}
	MOV R7,R4   			;load red value into local variable
	MOV R8,R5				;load pixel into local variable
	MOV R7,R7,LSL#16		;Moving red value into correct position
	AND R8,R8,#0xFF00FFFF 	;clear red bits
	ORR R0,R8,R7			;setting red bits
	LDMFD sp!, {R4-R5,R7-R8,lr}	;taking registers off stack
	BX lr					;branch to link address
	
;setGreen subroutine
;Sets the green value of a given pixel
;Parameters
;R1 : greenValue
;R3 : currentPixel
;Returns : R0 : current pixel with updated green value
setGreen
	STMFD sp!, {R4-R5,R7-R8,lr}	;backing up registers using the stack
	MOV R4,R1
	MOV R5,R3
	CMP R4,#0				;if(greenValue < 0)
	BGE check2				;{
	BL correctValue			;	greenValue.correctValue();
check2						;}
	CMP R4,#255				;if(greenValue > 255)
	BLE contGreen			;if(greenValue < 0)
	BL correctValue			;	greenValue.correctValue();
contGreen					;}
	MOV R7,R4   			;load green value into local variable
	MOV R8,R5				;load pixel into local variable
	MOV R7,R7,LSL#8			;Moving green value into correct position
	AND R8,R8,#0xFFFF00FF 	;clear green bits
	ORR R0,R8,R7			;setting green bits
	LDMFD sp!, {R4-R5,R7-R8,lr}	;taking registers off stack
	BX lr					;branch to link address
	
;setBlue subroutine
;Sets the blue value of a given pixel
;Parameters
;R1 : blueValue
;R3 : currentPixel
;Returns : R0 : current pixel with updated blue value	
setBlue
	STMFD sp!, {R4-R5,R7-R8,lr}	;backing up registers using the stack
	MOV R4,R1
	MOV R5,R3
	CMP R4,#0				;if(blueValue < 0)
	BGE check3				;{
	BL correctValue			;	blueValue.correctValue();
check3						;}
	CMP R4,#255				;if(blueValue > 255)
	BLE contBlue			;{
	BL correctValue			;	blueValue.correctValue();
contBlue					;}
	MOV R7,R4   			;load blue value into local variable
	MOV R8,R5				;load pixel into loacal variable
	AND R8,R8,#0xFFFFFF00 	;clear blue bits
	ORR R0,R8,R7			;setting blue bits
	LDMFD sp!, {R4-R5,R7-R8,lr}	;taking registers off stack
	BX lr					;branch to link address	
	
;setPixel
;sets a particular pixel in memory to its new form
;Parameters:
;R0: currentPixel
;R1: row of pixel
;R2: column of pixel
;R3: width of array
;R4: imageAddress
;Returns : this subrotuine returns position of the address with the
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
	STR R9,[R5,R8,LSL#2]	;memory.word[address] = currentPixel
	LDMFD sp!, {R4-R9}		;taking registers off stack
	BX lr					;branch to link address
	
;correctValue subRoutine
;corrects red, green or blue value if its above the maximum value of 255 or below 0
;R1 : Red, Green or Blue value to be corrected
;Return R1 : corrected value
correctValue				
	STMFD sp!, {R4}			;backing up registers using the stack
	MOV R4,R1				;Red, Green or Blue value
	CMP R4,#255				;if(value > 255)
	BLO negative			;{
	MOV R1,#255				;	value = 255
	LDMFD sp!, {R4}			;	taking registers off stack
	BX lr					;	branch to link address
negative					;else if(value < 0)
	MOV R1,#0				; 	value = 0
	LDMFD sp!, {R4}			;taking registers off stack
	BX lr					;branch to link address
;divide subroutine
;divides raidus by 2 to find out how far to go back
;Paramters
;R1 : radius
;Returns : R0 : half of radius(rounded)
divide
	STMFD sp!, {R4-R5}		;backing up registers using the stack
	MOV R4,R1				;radius
	MOV R5,#2				;divisor = 2
divWhile 					;
	CMP R4,R5				;	while( reamainder >= b)	
	BLT endDiv				;	{
	SUB R4,R4,R5			;		remainder = remainder - b;
	ADD R0, R0, #1			;		quotient = quotient + 1;
	b divWhile				;	}
endDiv						;
	LDMFD sp!, {R4-R5}		;taking registers off stack
	BX lr					;branch to link address

finish	
	BL	putPic		; re-display the updated image

stop	B	stop


	END	