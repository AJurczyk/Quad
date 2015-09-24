################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/AnalogInput.cpp \
../src/I2C.cpp \
../src/Mpu6050AccelGyro.cpp \
../src/PwmPin.cpp \
../src/SlotsBoneCapeMgr.cpp \
../src/Test.cpp \
../src/pin.cpp 

OBJS += \
./src/AnalogInput.o \
./src/I2C.o \
./src/Mpu6050AccelGyro.o \
./src/PwmPin.o \
./src/SlotsBoneCapeMgr.o \
./src/Test.o \
./src/pin.o 

CPP_DEPS += \
./src/AnalogInput.d \
./src/I2C.d \
./src/Mpu6050AccelGyro.d \
./src/PwmPin.d \
./src/SlotsBoneCapeMgr.d \
./src/Test.d \
./src/pin.d 


# Each subdirectory must supply rules for building sources it contributes
src/%.o: ../src/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	arm-linux-gnueabi-g++-4.7 -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


