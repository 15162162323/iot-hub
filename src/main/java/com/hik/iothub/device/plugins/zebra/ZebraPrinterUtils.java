package com.hik.iothub.device.plugins.zebra;

import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.TcpConnection;
import com.zebra.sdk.comm.UsbConnection;
import com.zebra.sdk.printer.*;
import com.zebra.sdk.printer.discovery.DiscoveredUsbPrinter;
import com.zebra.sdk.printer.discovery.UsbDiscoverer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * 斑马打印机工具类
 * 实现打印、状态查询
 */
public class ZebraPrinterUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ZebraPrinterUtils.class);
    public static final String UTF8="UTF-8";
    private static String symbolicName = null;

    public static boolean printNet(String ip,int port,String cmd,String charset) {
        Connection connection = new TcpConnection(ip, port);
        return print(connection,cmd,charset);
    }

    public static boolean print(Connection connection,String cmd,String charset) {
        try {
            connection.open();
            ZebraPrinter printer = ZebraPrinterFactory.getInstance(PrinterLanguage.ZPL,connection);
            String cs = charset == null ? UTF8 : charset;
            printer.sendCommand(cmd,cs);
            return true;
        } catch (ConnectionException e) {
            e.printStackTrace();
            LOG.error("打印机状态异常",e);
        } catch (UnsupportedEncodingException e) {
            LOG.error("不支持的编码格式",e);
        } finally {
            closeConnectionQuiet(connection);
        }
        return false;
    }

    /**
     * 获取usb连接的斑马设备信息，如果系统获取过则复用，避免重复获耗时
     * @return
     */
    private static String getUsbSymbolicName() throws ConnectionException {
//        if(symbolicName!=null){
//            return symbolicName;
//        }
        DiscoveredUsbPrinter[] zebraUsbPrinters = UsbDiscoverer.getZebraUsbPrinters();
        if(zebraUsbPrinters.length>0){
            symbolicName = zebraUsbPrinters[0].address;
        }
        return symbolicName;
    }

    public static boolean printUsb(String serialPort, String cmd,String charset){
        try {
            String usbSymbolicName = getUsbSymbolicName();
            if(usbSymbolicName!=null){
                Connection connection = new UsbConnection(usbSymbolicName);
                return print(connection,cmd,charset);
            } else {
                LOG.error("无法获取USB连接信息，请确认打印机是否连接");
            }
        } catch (ConnectionException e) {
            LOG.error("打印机状态异常",e);
        }
        return false;
    }

    /**
     * 获取打印机状态
     * @return
     */
    public static boolean getStatusUsb(String serialPort){
        boolean connected = false;
        Connection connection = null;
        try {
            String usbSymbolicName = getUsbSymbolicName();
            if(usbSymbolicName!=null){
                connection = new UsbConnection(usbSymbolicName);
                connection.open();
                connected = connection.isConnected();
                LOG.info("打印机状态{}",connected);
            }
        } catch (Exception e) {
            LOG.error("打印机状态获取异常",e.getMessage());
        } finally {
            closeConnectionQuiet(connection);
        }
        return connected;
    }

    private static void closeConnectionQuiet(Connection connection){
        try {
            if(connection!=null&&connection.isConnected()){
                connection.close();
            }
        } catch (ConnectionException e) {
            LOG.error("打印机关闭异常",e);
        }
    }

    /**
     * 获取打印机状态
     * @param ip
     * @param port
     * @return
     */
    public static boolean getStatusNet(String ip,int port){
        boolean connected = false;
        Connection connection = null;
        try {
            connection = new TcpConnection(ip, port);
            connection.open();
            connected = connection.isConnected();
            LOG.info("打印机状态{}",connected);
        } catch (Exception e) {
            LOG.error("打印机状态获取异常",e.getMessage());
        } finally {
            closeConnectionQuiet(connection);
        }
        LOG.info("打印机状态{}",connected);
        return connected;
    }

    public static void main(String[] args) {
        String zplData = "^XA^CW1,E:27466536.TTF^FT320,104^A1N,50,49^FH\\^CI28^FD资产编号:^FS^CI27^FT320,195^A1N,50,49^FH\\^CI28^FD资产名称:^FS^CI27^FT320,283^A1N,50,49^FH\\^CI28^FD贴标日期:^FS^CI27^FO23,21^GB1104,315,2^FS^FO564,116^GB464,0,1^FS^FO564,214^GB464,0,1^FS^FO564,297^GB464,0,1^FS^SL0,15^FT588,283^A1N,50,49^FC%,{,#^FH\\^CI28^FD%Y-%m-%d^FS^CI27^FT588,102^A1N,50,49^FH\\^CI28^FDV0^FS^CI27^FT588,189^A1N,50,49^FH\\^CI28^FDV1^FS^CI27^PQ1,,,Y^XZ";
        printUsb("0",zplData,"UTF-8");
//        printNet("127.0.0.1",9100,zplData,"UTF-8");
//        getStatusUsb("");
    }
}
