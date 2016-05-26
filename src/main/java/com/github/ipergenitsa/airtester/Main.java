package com.github.ipergenitsa.airtester;

import javax.usb.*;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) throws UsbException, UnsupportedEncodingException {
        final UsbServices services = UsbHostManager.getUsbServices();
        final Optional<UsbDevice> device = findDevice(services.getRootUsbHub(), (short) 0x16c0, (short) 0x05df);
        System.out.println(device.toString());
    }

    private static Optional<UsbDevice> findDevice(final UsbHub hub, short vendorId, short productId)
    {
        for (UsbDevice device : (List<UsbDevice>) hub.getAttachedUsbDevices())
        {
            UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
            if (desc.idVendor() == vendorId && desc.idProduct() == productId)
                return Optional.of(device);
            if (device.isUsbHub())
            {
                Optional<UsbDevice> foundDevice = findDevice((UsbHub) device, vendorId, productId);
                if (foundDevice.isPresent()) return foundDevice;
            }
        }
        return Optional.empty();
    }
}
