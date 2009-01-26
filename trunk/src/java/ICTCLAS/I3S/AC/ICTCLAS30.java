package ICTCLAS.I3S.AC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;

public class ICTCLAS30 {
    public native   boolean ICTCLAS_Init(byte[] sPath);
		public native   boolean ICTCLAS_Exit();
		public native   int ICTCLAS_ImportUserDict(byte[] sPath);
		public native   byte[] ICTCLAS_ParagraphProcess(byte[] sSrc,int bPOSTagged);
		public native   boolean ICTCLAS_FileProcess(byte[] sSrcFilename,byte[] sDestFilename,int bPOSTagged);

		public native   float ICTCLAS_GetUniProb(byte[] sWord);
		public native   boolean ICTCLAS_IsWord(byte[] sWord);
		public native byte[] nativeProcAPara(byte[] src);
	  
    /* Use static intializer */
    static {
			System.loadLibrary("ICTCLAS30");
    }
   
    public static void main(String[] args) throws Exception{
        ICTCLAS30 i= new ICTCLAS30();
        i.ICTCLAS_Init("".getBytes("gb2312"));
        i.ICTCLAS_FileProcess("c:/output.txt".getBytes("gb2312"), "c:/input.txt".getBytes("gb2312"), 1);
        FileReader r = new FileReader("c:/output.txt");
        BufferedReader br = new BufferedReader(r);
       String line = br.readLine();
       StringBuffer sb = new StringBuffer();
       while(line!=null){
           sb.append(line);
           line = br.readLine();
       }
       br.close();
        System.out.println(new String(i.ICTCLAS_ParagraphProcess(sb.toString().getBytes("gb2312"), 1),"gb2312"));
        i.ICTCLAS_Exit();
    }
}


