package tw.com.mygis.fireapp_taoyuan;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileService {
	/**聲明上下文*/
	private Context context;
	/**文件夾名字*/
    private static final String PEOPLE_NAME = "/FireAPP/People";
    private static final String HANWRITE_NAME = "/FireAPP/HandWrite";
    private static final String PRINT_NAME = "/FireAPP/PreviewPrint";
	
	private static final String TAG = "FileService";
    private String fileDir = null;
	
	// 構造含數
	public FileService(Context context) {
		this.context = context;
	}

	/**
	 * 保存bitmap到文件
	 * @param filename
	 * @param bmp
	 * @return
	 */
	public String saveBitmapToSDCard(String filename, Bitmap bmp, String type) {
		
		// 文件相對路徑
		String fileName = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			// 文件保存的路徑
            if(type.equals("HandWrite")) {
                fileDir = Environment.getExternalStorageDirectory() + HANWRITE_NAME;
            }
            else if (type.equals("Print")){
                fileDir = Environment.getExternalStorageDirectory() + PRINT_NAME;
            }
            else if (type.equals("People")){
                fileDir = Environment.getExternalStorageDirectory() + PEOPLE_NAME;
            }
			else {
                Log.e(TAG, "TYPE錯誤!");
            }
			// 如果文件夾不存在，創建文件夾
			if (!createDir(fileDir)) {
				Log.e(TAG, "創建文件夾失敗!");
			}
            Log.e(TAG, fileDir);
			// 定義文件對象
			File file = null;
			// 定義輸出流
			FileOutputStream outStream = null;
			
			try {
				//如果有目標文件，直接獲得文件對象，否則創建一個以filename為名稱的文件
				file = new File(fileDir, filename);
				
				// 取得文件相對路徑
				fileName = file.toString();
				// 取得输出流，如果文件中有内容，追加内容
				outStream = new FileOutputStream(fileName);
				if(outStream != null)
                {
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    outStream.close();
                }

			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}finally{
				// 關閉
				try {
					if (outStream != null) {
						outStream.close();
					}
				} catch (IOException e) {
					Log.e(TAG, e.toString());
				}
			}
		}
		return fileName;
	}
	
	/**
	 * 創建指定路徑的文件夾，並返回執行情況ture or false
	 * @param filePath
	 * @return
	 */
	public boolean createDir(String filePath) {
		File fileDir = new File(filePath); // 生成文件對象
		boolean bRet = true;
		// 如果文件不存在，創造文件
		if (!fileDir.exists()) {
			// 獲得文件或文件夾名稱
			String[] aDirs = filePath.split("/");
			StringBuffer strDir = new StringBuffer();
			for (int i = 0; i < aDirs.length; i++) {
				// 獲得文件上一層路徑
				fileDir = new File(strDir.append("/").append(aDirs[i]).toString());
				// 是否存在
				if (!fileDir.exists()) {
					// 不存在創建文件失敗返回False
					if (!fileDir.mkdir()) {
						bRet = false;
						break;
					}
				}
			}
		}

		return bRet;
	}

}
