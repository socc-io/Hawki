package becxer.indoorposition.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by becxer on 16. 5. 27.
 */
public class FileReadWriter {

    public static String BASE_PATH = Environment.getExternalStorageDirectory() + "/indoorposition";

    public static File checkExist(String path, String filename){
        String real_path = BASE_PATH;
        if(path != null) real_path += "/" + path;
        File base_dir = new File(real_path);
        if(!base_dir.exists()) base_dir.mkdirs();

        String real_file_path = real_path;
        if(filename != null) real_file_path += "/" + filename;
        File open_file = new File(real_file_path);
        if(!open_file.exists() && !open_file.isDirectory()){
            try {
                open_file.createNewFile();
            }catch(Exception e){
                Log.d("checkExist", e.toString());
            }
        }
        return open_file;
    }

    public static void writeContent(String path, String filename, String content){
        File open_file = checkExist(path,filename);
        try {
            FileOutputStream fOut = new FileOutputStream(open_file);
            OutputStreamWriter outWriter = new OutputStreamWriter(fOut);
            outWriter.write(content);
            outWriter.close();
            fOut.close();
        }catch (Exception e){
            Log.d("writeContent", e.toString());
        }
    }

    public static String readContent(String path, String filename){
        File open_file = checkExist(path, filename);
        String ret = "";
        try {
            FileReader fileReader = new FileReader(open_file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String receiveString = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ( (receiveString = bufferedReader.readLine()) != null ) {
                stringBuilder.append(receiveString);
            }
            ret = stringBuilder.toString();
        }catch (Exception e){
            Log.d("readContent", e.toString());
        }
        return ret;
    }
}
