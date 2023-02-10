package com.hik.iothub.device.plugins.postek;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface CDFPSKDll extends Library {

  static String DLLName = "CDFPSK" + System.getProperty("sun.arch.data.model");
  public static final CDFPSKDll Instance = (CDFPSKDll)Native.loadLibrary(DLLName, CDFPSKDll.class);
  
  int PTK_GetLastError();
  
  int PTK_WSAGetLastError();
  
  int PTK_OpenLogMode(String paramString);
  
  int PTK_CloseLogMode();
  
  int PTK_UTF8toGBK(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt);
  
  int PTK_GBKtoUTF8(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt);
  
  int PTK_OpenUSBPort(int paramInt);
  
  int PTK_CloseUSBPort();
  
  int PTK_Connect_Timer(String paramString, int paramInt1, int paramInt2);
  
  int PTK_Connect(String paramString, int paramInt);
  
  int PTK_CloseConnect();
  
  int PTK_OpenSerialPort(int paramInt1, int paramInt2);
  
  int PTK_CloseSerialPort();
  
  int PTK_OpenPrinter(String paramString);
  
  int PTK_ClosePrinter();
  
  int PTK_OpenParallelPort(int paramInt);
  
  int PTK_CloseParallelPort();
  
  int PTK_OpenTextPort(String paramString);
  
  int PTK_CloseTextPort();
  
  int PTK_OpenUSBPort_Buff(int paramInt);
  
  int PTK_Connect_Timer_Buff(String paramString, int paramInt1, int paramInt2);
  
  int PTK_OpenPrinter_Buff(String paramString);
  
  int PTK_CloseBuffPort();
  
  int PTK_WriteBuffToPrinter();
  
  int PTK_SendFile(String paramString);
  
  int PTK_SendCmd(String paramString, int paramInt);
  
  int PTK_SendString(int paramInt, String paramString);
  
  int PTK_GetUSBID(byte[] paramArrayOfbyte);
  
  int PTK_GetErrorInfo(int paramInt1, byte[] paramArrayOfbyte, int paramInt2);
  
  int PTK_GetAllPrinterUSBInfo(byte[] paramArrayOfbyte, int paramInt);
  
  int PTK_GetInfo();
  
  int PTK_GetErrState();
  
  int PTK_PrintConfiguration();
  
  int PTK_MediaDetect();
  
  int PTK_UserFeed(int paramInt);
  
  int PTK_UserBackFeed(int paramInt);
  
  int PTK_EnableFLASH();
  
  int PTK_DisableFLASH();
  
  int PTK_FeedMedia();
  
  int PTK_CutPage(int paramInt);
  
  int PTK_CutPageEx(int paramInt);
  
  int PTK_SetCoordinateOrigin(int paramInt1, int paramInt2);
  
  int PTK_GetUtilityInfo(int paramInt1, byte[] paramArrayOfbyte, int paramInt2);
  
  int PTK_GetAllPrinterInfo(int paramInt1, int paramInt2, byte[] paramArrayOfbyte, int paramInt3);
  
  int PTK_ErrorReport_USBInterrupt(int[] paramArrayOfint);
  
  int PTK_GetPrinterName(byte[] paramArrayOfbyte);
  
  int PTK_GetPrinterDPI(int[] paramArrayOfint);
  
  int PTK_GetPrinterKey_USB(byte[] paramArrayOfbyte);
  
  int PTK_ClearBuffer();
  
  int PTK_SetPrintSpeed(int paramInt);
  
  int PTK_SetDirection(char paramChar);
  
  int PTK_SetDarkness(int paramInt);
  
  int PTK_SetLabelHeight(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean);
  
  int PTK_SetLabelWidth(int paramInt);
  
  int PTK_PrintLabel(int paramInt1, int paramInt2);
  
  int PTK_PrintLabelFeedback(byte[] paramArrayOfbyte, int paramInt);
  
  int PTK_DrawText(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, char paramChar, String paramString);
  
  int PTK_DrawTextEx(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, char paramChar, String paramString, int paramInt7);
  
  int PTK_DrawTextTrueTypeW(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString1, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, String paramString2, String paramString3);
  
  int PTK_DrawText_TrueType(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString1, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, String paramString2);
  
  int PTK_DrawText_TrueType_AutoFeedLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString1, int paramInt5, int paramInt6, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt7, int paramInt8, int paramInt9, char paramChar1, char paramChar2, String paramString2);
  
  int PTK_DrawText_TrueTypeEx(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString1, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13, String paramString2);
  
  int PTK_ChangeIMGtoPCX(String paramString);
  
  int PTK_PcxGraphicsList();
  
  int PTK_PcxGraphicsDel(String paramString);
  
  int PTK_AnyGraphicsDownload(String paramString1, String paramString2, float paramFloat, int paramInt1, int paramInt2, int paramInt3);
  
  int PTK_DrawPcxGraphics(int paramInt1, int paramInt2, String paramString);
  
  int PTK_AnyGraphicsPrint(int paramInt1, int paramInt2, String paramString1, String paramString2, float paramFloat, int paramInt3, int paramInt4, int paramInt5);
  
  int PTK_AnyGraphicsDownloadFromMemory(String paramString, int paramInt1, int paramInt2, float paramFloat, int paramInt3, int paramInt4, int paramInt5, Byte[] paramArrayOfByte);
  
  int PTK_AnyGraphicsPrintFromMemory(int paramInt1, int paramInt2, String paramString, int paramInt3, int paramInt4, float paramFloat, int paramInt5, int paramInt6, int paramInt7, Byte[] paramArrayOfByte);
  
  int PTK_PcxGraphicsDownload(String paramString1, String paramString2);
  
  int PTK_PrintPCX(int paramInt1, int paramInt2, String paramString);
  
  int PTK_BmpGraphicsDownload(String paramString1, String paramString2, int paramInt);
  
  int PTK_BinGraphicsList();
  
  int PTK_BinGraphicsDel(String paramString);
  
  int PTK_BinGraphicsDownload(String paramString, int paramInt1, int paramInt2, byte[] paramArrayOfbyte);
  
  int PTK_RecallBinGraphics(int paramInt1, int paramInt2, String paramString);
  
  int PTK_DrawBinGraphics(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfbyte);
  
  int PTK_DrawRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  int PTK_DrawLineXor(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  int PTK_DrawLineOr(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  int PTK_DrawDiagonal(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  int PTK_DrawWhiteLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  int PTK_DrawBar2D_QR(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, String paramString);
  
  int PTK_DrawBar2D_QREx(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, String paramString1, String paramString2);
  
  int PTK_DrawBar2D_HANXIN(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, String paramString);
  
  int PTK_DrawBar2D_Pdf417(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, String paramString);
  
  int PTK_DrawBar2D_Pdf417Ex(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, String paramString);
  
  int PTK_DrawBar2D_MaxiCode(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString);
  
  int PTK_DrawBar2D_DATAMATRIX(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, String paramString);
  
  int PTK_DrawBarcode(int paramInt1, int paramInt2, int paramInt3, String paramString1, int paramInt4, int paramInt5, int paramInt6, char paramChar, String paramString2);
  
  int PTK_DrawBarcodeEx(int paramInt1, int paramInt2, int paramInt3, String paramString1, int paramInt4, int paramInt5, int paramInt6, int paramInt7, char paramChar, String paramString2, int paramInt8);
  
  int PTK_GetStorageList(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  int PTK_FormList();
  
  int PTK_FormDel(String paramString);
  
  int PTK_FormDownload(String paramString);
  
  int PTK_FormEnd();
  
  int PTK_ExecForm(String paramString);
  
  int PTK_DefineVariable(int paramInt1, int paramInt2, char paramChar, String paramString);
  
  int PTK_DefineCounter(int paramInt1, int paramInt2, char paramChar, String paramString1, String paramString2);
  
  int PTK_Download();
  
  int PTK_DownloadInitVar(String paramString);
  
  int PTK_PrintLabelAuto(int paramInt1, int paramInt2);
  
  int PTK_RFIDCalibrate();
  
  int PTK_RWRFIDLabel(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString);
  
  int PTK_RWRFIDLabelEx(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString);
  
  int PTK_SetRFLabelPWAndLockRFLabel(int paramInt1, int paramInt2, String paramString);
  
  int PTK_EncodeRFIDPC(String paramString);
  
  int PTK_SetRFID(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  int PTK_ReadRFIDLabelData(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfbyte, int paramInt4);
  
  int PTK_ReadRFIDLabelDataEx(int paramInt1, int paramInt2, int paramInt3, String paramString, byte[] paramArrayOfbyte, int paramInt4);
  
  int PTK_RFIDEndPrintLabel(int paramInt1, byte[] paramArrayOfbyte, int paramInt2);
  
  int PTK_RFIDEndPrintLabelFeedBack(int paramInt1, byte[] paramArrayOfbyte1, int paramInt2, byte[] paramArrayOfbyte2, int paramInt3);
  
  int PTK_SetReadRFIDForwardSpeed(int paramInt);
  
  int PTK_SetReadRFIDBackSpeed(int paramInt);
  
  int PTK_ReadRFIDSetting(int paramInt1, int paramInt2, int paramInt3, String paramString);
  
  int PTK_PrintAndCallback(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  int PTK_RWHFLabel(char paramChar, int paramInt1, int paramInt2, String paramString, int paramInt3);
  
  int PTK_SetHFRFID(char paramChar, int paramInt1, int paramInt2);
  
  int PTK_ReadHFTagData(int paramInt1, int paramInt2, int paramInt3, Boolean paramBoolean, int paramInt4, int paramInt5, int paramInt6, char paramChar, Pointer paramPointer);
  
  int PTK_ReadHFLabelData(int paramInt1, int paramInt2, char paramChar, byte[] paramArrayOfbyte, int paramInt3);
  
  int PTK_ReadHFLabeUID(char paramChar, byte[] paramArrayOfbyte, int paramInt);
  
  int PTK_ReadHFTagUID(int paramInt1, int paramInt2, int paramInt3, Boolean paramBoolean, int paramInt4, char paramChar, Pointer paramPointer);
  
  int PTK_ReadHFTagDataPrintAuto(int paramInt1, int paramInt2);
  
  int PTK_ReadHFTagUIDPrintAuto();
  
  int PTK_SetHFAFI(int paramInt);
  
  int PTK_SetHFDSFID(int paramInt);
  
  int PTK_SetHFEAS(char paramChar);
  
  int PTK_HFDecrypt(int paramInt1, int paramInt2, int paramInt3, String paramString);
  
  int PTK_LockHFLabel(int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3);
  
  int PTK_LockHFIdentifier(char paramChar);
  
  int PTK_LockHFBlock(int paramInt1, int paramInt2);
  
  int PTK_SetHFKey(int paramInt, String paramString1, String paramString2, String paramString3);
  
  int PTK_SetHFCRCCommand(int paramInt, String paramString1, String paramString2);
  
  int PTK_SetHFPrivateCommand(int paramInt, String paramString1, String paramString2);
  
  int PTK_LockHFUser(int paramInt1, int paramInt2, int paramInt3);
  
  int PTK_SetHFCFG10(String paramString);
  
  int PTK_SetHFCFG80(String paramString);
  
  int PTK_ReadHFRFIDSetting(int paramInt1, int paramInt2, int paramInt3);
  
  int OpenPort(int paramInt);
  
  int ClosePort();
  
  int SetPCComPort(int paramInt, boolean paramBoolean);
  
  int PTK_Reset();
  
  int PTK_SoftFontList();
  
  int PTK_SoftFontDel(char paramChar);
  
  int PTK_DisableBackFeed();
  
  int PTK_EnableBackFeed(int paramInt);
  
  int PTK_SetPrinterState(String paramString);
  
  int PTK_DisableErrorReport();
  
  int PTK_EnableErrorReport();
  
  int PTK_SetRFIDLabelRetryCount(int paramInt);
  
  int PTK_FeedBack();
  
  int PTK_ReadData(int[] paramArrayOfint, int paramInt);
  
  int PTK_ErrorReport(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  int PTK_ErrorReportNet(String paramString, int paramInt);
  
  int PTK_ErrorReportUSB(int paramInt);
  
  int PTK_SetFontGap(int paramInt);
  
  int PTK_SetBarCodeFontName(char paramChar, int paramInt1, int paramInt2);
  
  int PTK_RenameDownloadFont(int paramInt, char paramChar, String paramString);
  
  int PTK_SetCharSets(int paramInt, char paramChar, String paramString);
  
  int PTK_ReadRFTagDataNet(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Pointer paramPointer);
  
  int PTK_ReadRFTagDataUSB(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfbyte);
  
  int PTK_ClearUIDBuffers();
  
  int PTK_ReadHFTagUIDUSB(int paramInt, char paramChar, byte[] paramArrayOfbyte);
  
  int PTK_SetTearAndTphOffset(float paramFloat1, float paramFloat2);
}
