	AREA	BonusEffect, CODE, READONLY
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
	BL	getPicHeight	; load the height of the image (rows) in R5
	MOV	R5, R0
	BL	getPicWidth	; load the width of the image (columns) in R6
	MOV	R6, R0
	
	;first convert to greyscale
	LDR R7,=-1				;rowCount
forRow						;
	ADD R7,R7,#1			;rowCount++
	MOV R8,#0				;columnCount = 0
	CMP R7,R5				;for(int index = 0;rowCount < picHeight; index++)
	BEQ outline				;{
forColumn					;	for(int index2; index < picWidth; index2++)
	CMP R8,R6				;	{	
	BEQ forRow				;		
	MOV R9,#0				;			greyScaleTotal = 0
	MOV R1,R7				;			row	
	MOV R2,R8				;			column
	MOV R3,R6				;			width
	STMFD sp!, {R4}			;			passing the image address as a parameter by pushing it onto a stack
	BL getPixel				;			currentPixel = getPixel(row,column,width,imageAddress)
	MOV R3,R0 				; 			initialising register with return value
	BL getRed				;			redValue = getRed(currentPixel)
	ADD R9,R9,R0			;			greyScaleTotal += red
	BL  getGreen			;			greenValue = getGreen(currentPixel)
	ADD R9,R9,R0			;			greyScaleTotal += green
	BL getBlue				;			blueValue = getBlue(currentPixel)
	ADD R9,R9,R0			;			greyScaleTotal += blue
	MOV R1,R9				;			passing greyScaleTotal as parameter
	BL getAverage			;			getAverage(greyScaleTotal)
	MOV R1,R0				;			//setting rbg to average of 3 colors
	BL setRed				;
	MOV R3,R0				;
	BL setGreen				;	
	MOV R3,R0				;
	BL setBlue				;	
	MOV R3,R0				;
	MOV R10,R3				;			
	STMFD sp!, {R4,R10}		;			passing the image address and currentPixel as a parameters by pushing it onto a stack
	MOV R1,R7				;			passing local varaibles : row	
	MOV R2,R8				;									  column
	MOV R3,R6				;									  width
	BL setPixel				;			//putting pixel back into image
	ADD R8,R8,#1			;			column++
	b forColumn				;	}
							;
outline						;//performing effect on grey scale image
	MOV R7,#-1				; imageRow = -1
	LDR R11,=kernel			; //loading address of image kernel 
forRow2						; 
	ADD R7,R7,#1			;rowCount++
	MOV R8,#0				;columnCount
	CMP R7,R5				;for(int index = 0;rowCount < picHeight; index++)
	BEQ copy				;{
forColumn2					;	for(int index2; index < picWidth; index2++)
	CMP R8,R6				;	{	
	BEQ forRow2				;			//passing local varaibles as parmeters	
	MOV R1,R7				;			row	
	MOV R2,R8				;			column	
	MOV R3,R6				;			width
	STMFD sp!, {R4}			;			passing the image address as a parameter by pushing it onto a stack
	BL getPixel				;			currentPixel = getPixel(row,column,width,imageAddress)
	MOV R3,R0 				; 			initialising register with return value
	STMFD sp!,{R6-R8}		;			//backing up registers on stack so they can be used
	SUB R7,R7,#1			;			tempRow = row - 1
	SUB R8,R8,#1			;			tempColumn = column - 1
	MOV R10,#0				;			pixelTotal
	MOV R1,#-1				;			kernelRow = -1
	MOV R2,#0				;			kernelColumn = 0
kernelFor1					;		for(kernelRow; kernelRow < kernelHeight; kernelRow++){
	ADD R1,R1,#1			;			kernelRow++
	CMP R1,#3				;			
	BEQ exit				;			kernelColumn = 0
	MOV R2,#0				;
kernelFor2					;				for(kernelColumn; kernelColumn < kernelWidth; kernelColumn++)
	CMP R2,#3				;				{
	BEQ kernelFor1			;
	STMFD sp!,{R7,R8}		;					//backing up registers on stack so they can be used
	MOV R3,#3				;					kernelWidth = 3
	STMFD sp!,{R11}			;					passing cuurentPixel as parameter
	BL getKernelValue		;					kernelValue = getKernelValue(row,column,width,currentPixel)
	MOV R9,R0				;					
	ADD R7,R7,R1			;					tempRow += kernelRow
	ADD R8,R8,R2			;					tempColumn += kernelColumn
	MOV R3,R6				;					
	CMP R7,#0				;					if(tempRow < 0 || temRow > image height)
	BLT skip				;					{
	CMP R7,R5				;						skipPixel
	BGT skip				;					}
	CMP R8,#0				;					else if(tempRow < 0 || tempRow > image width)
	BLT skip				;					{
	CMP R8,R6				;						skipPixel
	BGT skip				;					}
	STMFD sp!, {R1,R2}		;					//backing up registers on stack so they can be used
	MOV R1,R7				;					passing row and column
	MOV R2,R8				;						
	STMFD sp!,{R4}			;					passing imageAddress
	BL getPixel				;					getPixel(row,column,width,imageAddress)
	LDMFD sp!, {R1,R2}		;					//taking registers used off stack
	AND R0,R0,#0x000000FF	;					get rgb values
	MUL R9,R0,R9			;					kernelvalue = rbg * kernelValue
	ADD R10,R10,R9			;					totalPixelValue += kernelValue
skip						;
	ADD R2,R2,#1			;					tempColumn++
	LDMFD sp!,{R7,R8}		;					//taking registers used off stack
	b kernelFor2			;				}
exit						;
	LDMFD sp!,{R6-R8}		;	//taking registers used off stack
	CMP R10,#0				;	if(totalPixelValue < 0 || > 255){
	BGE greater				;		correctValue()
	MOV R10,#0				;	}
greater						;
	CMP R10,#255			;
	BLE set					;
	MOV R10,#255			;
set							;
	MOV R1,R10				;	//setting each rbg value to totalPixelvalue
	BL setRed				;
	MOV R3,R0				;
	BL setGreen				;
	MOV R3,R0				;
	BL setBlue				;
	MOV R3,R0				;
	STMFD sp!,{R4-R5}		;	//backing up registers on stack so they can be used
	MOV R5,R3				;	passing parameters to newImage : currentPixel
	MOV R1,R7				;									 row
	MOV R2,R8				;									 column
	MOV R3,R6				;									 width
	LDR R4,=0xA1016204		;									 tempImageAddress
	STMFD sp!,{R4-R5}		;				
	BL newImage				;
	LDMFD sp!, {R4-R5}		;	//taking registers used off stack
	ADD R8,R8,#1			;	column++
	B forColumn2			; 	}
							;}
copy						;// copy tempImage into orginal imageAddress
	MOV R1,R3				;passing parameters into copyimage : width
	MOV R2,R5				;									 height
	LDR R3,=0xA1016204		;									 tempimageAddress
	STMFD sp!, {R4}			;									 imageAddress(orginal)
	BL copyImage			;	copyImage()
	b finish				;//branch to end of program
						

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

;getPixel subroutine
;Returns a given pixel in an array
;Parameters:
;R1 : row of pixel
;R2 : column of pixel
;R3 : width of array
;R4 : imageAddress
;Return R0 : any given pixel
getPixel		
	LDMFD sp!, {R4}			;address parameter pushed off stack
	STMFD sp!, {R4-R7}		;backing up registers using the stack
	MOV R5,R1				;rowOfPixel
	MOV R6,R2				;columnOfPixel
	MOV R7,R3				;imageWidth
	MUL R5,R7,R5			;pixelPosition = row * arrayWidth
	ADD R5,R5,R2			;pixelPosition = pixelPosition + column
	LDR R0, [R4,R5,LSL#2]	;currentPixel = imageAdress[PixelPosition * 4]
	LDMFD sp!, {R4-R7}		;taking registers off stack
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
;R1 : row of pixel
;R2 : column of pixel
;R3 : width of array
;R4	: imageAddress
;Returns : this subrotuine returns position of the address with the
setPixel
	LDMFD sp!, {R4,R10}		;imageAddress and currentPixel parameter pushed off stack
	STMFD sp!, {R4-R10}     ;backing up registers using the stack
	MOV R6,R1				;rowOfPixel
	MOV R7,R2				;columnOfPixel
	MOV R8,R3				;widthOfArray
	MUL R8,R6,R8			;pixelPosition = rowOfPixel * arrayWidth
	ADD R8,R8,R7			;pixelPosition = pixelPosition + columnOfPixel
	STR R10,[R4,R8,LSL#2]	;memory.word[address] = currentPixel
	LDMFD sp!, {R4-R10}		;taking registers off stack
	BX lr					;branch to link address	
;getAverage subroutine
;Returns the average of a set of values
;Parameters:
;R1	: totalOfValues(remainder)
;Return: R0 : averageResult

getAverage					
	STMFD sp!, {R5-R6}		;backing up registers using the stack
	MOV R5,R1				;remainder = totalOfValues
	MOV R6,#3				;b = 3
	MOV R0,#0				;quotient
avWhile 					;
	CMP R5, R6				;	while( reamainder >= b)	
	BLT endWh				;	{
	SUB R5,R5,R6			;		remainder = remainder - b;
	ADD R0, R0, #1			;		quotient = quotient + 1;
	b avWhile				;	}
endWh						; 
	LDMFD sp!, {R5-R6}		;taking registers off stack
	BX lr					;branch to link address		
	
	
;geKernelValue subroutine
;Returns a given pixel in an array
;Parameters:
;R1 : row of kernel
;R2 : column of kernel
;R3 : width of kernel
;R11 : currentPixel
;Return R0 : any given pixel
getKernelValue
	LDMFD sp!, {R11}		;current pixel parameter pushed off stack
	STMFD sp!, {R4-R7,R11}	;backing up registers using the stack
	MOV R4,R1				;rowOfPixel
	MOV R5,R2				;columnOfPixel
	MOV R6,R3				;widthOfKernel
	MOV R7,R11				;kernelAddress
	MUL R4,R6,R4			;pixelPosition = row * arrayWidth
	ADD R4,R4,R5			;pixelPosition = pixelPosition + column
	LDR R0, [R7,R4,LSL#2]	;currentPixel = imageAdress[PixelPosition * 4]
	LDMFD sp!, {R4-R7,R11}	;taking registers off stack
	BX lr					;branch to link address	
	

;newImage subroutine
;creates a new outline image
;Parameters;
;R1	:rows
;R2	:column
;R3 :imageWidth
;R4 :imageAddress
;R5 :newPixel
;Returns : this subroutine doesnt return anything	
newImage
	LDMFD sp!, {R4,R5}		;imageAddress and newPixel parameter pushed off stack
	STMFD sp!, {R4-R8}      ;backing up registers using the stack
	MOV R6,R1				;rowOfPixel
	MOV R7,R2				;columnOfPixel
	MOV R8,R3				;widthOfArray
	MUL R8,R6,R8			;pixelPosition = rowOfPixel * arrayWidth
	ADD R8,R8,R7			;pixelPosition = pixelPosition + columnOfPixel
	STR R5,[R4,R8,LSL#2]	;memory.word[address] = currentPixel
	LDMFD sp!, {R4-R8}		;taking registers off stack
	BX lr					;branch to link address	


;copyImage
;this copies the temp image into the correct image address
;Parameters:
;R1: picWidth
;R2: picHeight
;R3: tempImageAddress
;R4: imageAddress
;Return: This subroutine doesnt return anything
copyImage
	LDMFD sp!,{R4}			;imageAddress parameter pushed off stack
	STMFD sp!, {R4-R11,lr}	;backing up registers using the stack
	MOV R7,R1				;picWidth
	MOV R8,R2				;picheight
	MOV R9,R3				;tempImageAddress
	LDR R10,=-1				;rowCount
copyRow						;
	ADD R10,R10,#1			;rowCount++
	MOV R11,#0				;columnCount = 0;
	CMP R10,R8				;while(rowCount < picHeight)
	BEQ endCopy				;{
copyColumn					;	while(columnCount < width)
	CMP R11,R7				;	{	
	BEQ copyRow				;		//passing parameters
	MOV R1,R10				;		row		
	MOV R2,R11				;		column		
	MOV R3,R7				;		width
	STMFD sp!, {R4}			;		backing up imageAddress
	MOV R4,R9				;		passing tempImageAddress
	STMFD sp!, {R4}			;		passing the tempImageAddress as a parameter by pushing it onto a stack
	BL getPixel				;		currentPixel = getPixel(row,column,width,tempImageAddress)
	LDMFD sp!, {R4}			;		//taking initial imageAddress off stack	
	STMFD sp!,{R10}			;		//backing up register that holds rowCOunt
	MOV R10,R0				;		
	STMFD sp!,{R4,R10}		;		passing currentPixel as parameter
	BL setPixel				;		setPixel(currentPixel,row,column,width,imageAddress)
	LDMFD sp!,{R10}			;		//restore rowCount
	ADD R11,R11,#1			;		column++
	b copyColumn			;	}
endCopy						; }	
	LDMFD sp!, {R4-R11,lr}	;//taking registers off stack
	BX lr					;branch to link address	
	
finish
	BL	putPic		; re-display the updated image

stop	B	stop

;edge detection kernel
kernel	
	DCD	-1,-1,-1
	DCD	-1,8,-1
	DCD	 -1,-1,-1
	

	END	