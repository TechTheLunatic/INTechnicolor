SET(CMAKE_C_COMPILER avr-g++)
SET(CMAKE_CXX_COMPILER avr-g++)

SET(CWARN "-Wall")
SET(COPT "-O2 -lm -lprintf_flt -Wl,-u,vfprintf")
SET(CMCU "-mmcu=atmega328p")
IF(FOR_ARDUINO)
	ADD_DEFINITIONS(-DF_CPU=16e6) #Arduino
ELSE(FOR_ARDUINO)
	ADD_DEFINITIONS(-DF_CPU=20e6) #Avr
ENDIF(FOR_ARDUINO)
# SET(CDEFS "-DF_CPU=20e6") #sur les cartes

SET(CFLAGS "${CMCU} ${CDEFS} ${COPT} ${CWARN}")
SET(CXXFLAGS "${CMCU} ${CDEFS} ${COPT} ${CWARN}")
# SET(CMAKE_SHARED_LIBRARY_LINK_CXX_FLAGS "")

SET(CMAKE_C_FLAGS  ${CFLAGS})
SET(CMAKE_CXX_FLAGS ${CXXFLAGS})
set(CMAKE_SHARED_LIBRARY_LINK_CXX_FLAGS)
