package youthstudio.com.kegelexercises;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by thaodv on 12/29/15.
 */
public class RawFileReader {
    private static final String TAG = RawFileReader.class.getSimpleName();
    static RawFileReader instance = null;
    private Context mContext;
    private List<ExerciseItem> exerciseItemList;
    protected Resources mResources;

    public RawFileReader(Context ctx) {
        mContext = ctx;
        mResources = mContext.getResources();
    }

    public static RawFileReader getInstance(Context ctx) {
        if (instance == null) {
            instance = new RawFileReader(ctx);
        }
        return instance;
    }

    public List<ExerciseItem> readExerciseData() {
        try {
            InputStream is = mResources.openRawResource(R.raw.data);
            byte[] data = new byte[is.available()];
            is.read(data);
            String content = new String(data, "UTF-8");
            Gson gson = new Gson();
            Type listType = new TypeToken<List<ExerciseItem>>() {
            }.getType();
            exerciseItemList = gson.fromJson(content, listType);
            Log.i(TAG, "exerciseItemList " + exerciseItemList.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exerciseItemList;
    }
}
