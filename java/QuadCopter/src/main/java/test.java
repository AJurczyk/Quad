import com.pi4j.wiringpi.Gpio;
import hardware.exception.PwmValueOutOfRange;
import hardware.IPwm;
import hardware.impl.HardwarePwm;

/**
 * @author aleksander.jurczyk@gmail.com on 29.11.15.
 */
public class test {
    public static void main(String[] args) throws PwmValueOutOfRange {
        System.out.println("TEST quad");

        IPwm pwm = new HardwarePwm(1, 20);
        pwm.setDuty(1.5f);
        for (int i = 0; i < 3; i++) {
            pwm.setDuty(1.5f);
            pwm.setDuty(0.8f);
            pwm.setDuty(1.5f);
            pwm.setDuty(2.2f);
        }

//        Gpio.pinMode(1,Gpio.PWM_OUTPUT);
//        Gpio.pwmSetMode(Gpio.PWM_MODE_MS);
//        Gpio.pwmSetClock(1920);
//        Gpio.pwmSetRange(200);
//
//        //The Raspberry Pi PWM clock has a base frequency of 19.2 MHz.
//        //setClock 2 - 4095
//        //setRange up to 4096
//        //1,14Hz minimum freq
//        for (int i = 0; i < 3; i++) {
//            Gpio.pwmWrite(1, 15);//center
//            Gpio.pwmWrite(1, 8);//right
//            Gpio.pwmWrite(1, 15);
//            Gpio.pwmWrite(1, 22);//left
//        }
//        Gpio.pwmWrite(1,15);//center

//        GpioController gpioCtr = GpioFactory.getInstance();
////        Gpio.pwmSetClock();
//        GpioPinPwmOutput pwm = gpioCtr.provisionPwmOutputPin(RaspiPin.GPIO_01,0);
//        pwm.setPwm(512);
//        pwm.setPwm(1024);
//        pwm.setPwm(1025);
//        pwm.setPwm(3000);
//        PinMode mode = pwm.getMode();
//        Map<String,String> properties =  pwm.getProperties();
    }
}
