// Distributed with a free-will license.
// Use it any way you want, profit or free, provided it fits in the licenses of its associated works.
// TMP112
// This code is designed to work with the TMP112_I2CS I2C Mini Module available from ControlEverything.com.
// https://www.controleverything.com/content/Temperature?sku=TMP112_I2CS#tabs-0-product_tabset-2

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import java.io.IOException;

public class TMP112
{
	public static void main(String args[]) throws Exception
	{
		// Create I2C bus
		I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
		// Get I2C device, TMP112 I2C address is 0x48(72)
		I2CDevice device = bus.getDevice(0x48);
		
		byte[] config = new byte[2];
		// Continous Conversion mode, 12-Bit Resolution, Fault Queue is 1
		config[0] = (byte)0x60;
		// Polarity low, Thermostat in Comparator mode, Disables Shutdown mode
		config[1] = (byte)0xA0;
		// Write config to register 0x01(1)
		device.write(0x01, config, 0, 2);
		
		Thread.sleep(500);
		// Read 2 Bytes of data from address 0x00(0), msb first
		byte[] data = new byte[2];
		device.read(0x00, data, 0, 2);
		
		// Convert data 
		int temp = (((data[0] & 0xFF) * 256) + (data[1] & 0xFF))/16;
		if(temp > 2047)
		{
			temp -= 4096;
		}
		double cTemp = temp * 0.0625;
		double fTemp = cTemp * 1.8 + 32;
		
		// Output to screen
		System.out.printf("Temperature  in Celsius is : %.2f C %n", cTemp);
		System.out.printf("Temperature in Fahrenheit is : %.2f F %n", fTemp);
	}
}
