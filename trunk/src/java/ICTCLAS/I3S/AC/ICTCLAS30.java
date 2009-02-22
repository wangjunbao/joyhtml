package ICTCLAS.I3S.AC;
import java.io.*;

public class ICTCLAS30 {
/*
   	public static String ICTCLAS_ParagraphProcessUTF8(String sSrc,int bPOSTagged)
   	{
   		String sInput,sResult;
   		byte[] u =sSrc.getBytes("utf-8");   
      sInput=new  String(u,"gb2312");   
      byte [] nativeBytes = ICTCLAS_ParagraphProcess(sInput.getBytes("GB2312"),bPOSTagged);
    	return new String(nativeBytes,0,nativeBytes.length,"utf-8");
   	}
*/   
    public native   boolean ICTCLAS_Init(byte[] sPath);
		public native   boolean ICTCLAS_Exit();
		public native   int ICTCLAS_ImportUserDict(byte[] sPath);
		public native float ICTCLAS_GetUniProb(byte[] sWord);
		public native boolean ICTCLAS_IsWord(byte[] sWord);
		public native   byte[] ICTCLAS_ParagraphProcess(byte[] sSrc,int bPOSTagged);
		public native   boolean ICTCLAS_FileProcess(byte[] sSrcFilename,byte[] sDestFilename,int bPOSTagged);

		public native   byte[] nativeProcAPara(byte[] src);

	
/*********************************************************************
*
*  Func Name  : ICTCLAS_AddUserWord
*
*  Description: add a word to the user dictionary ,example:你好	
*													 i3s	n
*
*  Parameters : sFilename: file name
*               
*  Returns    : 1,true ; 0,false
*
*  Author     :   
*  History    : 
*              1.create 11:10:2008
*********************************************************************/
public native int ICTCLAS_AddUserWord(byte[] sWord);//add by qp 2008.11.10



/*********************************************************************
*
*  Func Name  : Save
*
*  Description: Save dictionary to file
*
*  Parameters :
*               
*  Returns    : 1,true; 2,false
*
*  Author     :   
*  History    : 
*              1.create 11:10:2008
*********************************************************************/
public native int ICTCLAS_SaveTheUsrDic();

/*********************************************************************
*
*  Func Name  : ICTCLAS_DelUsrWord
*
*  Description: delete a word from the  user dictionary
*
*  Parameters : 
*  Returns    : -1, the word not exist in the user dictionary; else, the handle of the word deleted
*
*  Author     :   
*  History    : 
*              1.create 11:10:2008
*********************************************************************/
public native int ICTCLAS_DelUsrWord(byte[] sWord);

/*********************************************************************
*
*  Func Name  : ICTCLAS_KeyWord
*
*  Description: Extract keyword from paragraph
*
*  Parameters : resultKey, the returned key word 
				nCountKey, the returned key num
*  Returns    : 0, failed; else, 1, successe
*
*  Author     :   
*  History    : 
*              1.create 11:10:2008
*********************************************************************/
public native int ICTCLAS_KeyWord(byte[] resultKey, int nCountKey);

/*********************************************************************
*
*  Func Name  : ICTCLAS_FingerPrint
*
*  Description: Extract a finger print from the paragraph
*
*  Parameters :
*  Returns    : 0, failed; else, the finger print of the content
*
*  Author     :   
*  History    : 
*              1.create 11:10:2008
*********************************************************************/
public native  long ICTCLAS_FingerPrint();

/*********************************************************************
*
*  Func Name  : ICTCLAS_SetPOSmap
*
*  Description: select which pos map will use
*
*  Parameters :nPOSmap, ICT_POS_MAP_FIRST  计算所一级标注集
						ICT_POS_MAP_SECOND  计算所二级标注集
						PKU_POS_MAP_SECOND   北大二级标注集
						PKU_POS_MAP_FIRST 	  北大一级标注集
*  Returns    : 0, failed; else, success
*
*  Author     :   
*  History    : 
*              1.create 11:10:2008
*********************************************************************/
public native int ICTCLAS_SetPOSmap(int nPOSmap);

    /* Use static intializer */
    static {
			System.loadLibrary("ICTCLAS30");
    }
}


