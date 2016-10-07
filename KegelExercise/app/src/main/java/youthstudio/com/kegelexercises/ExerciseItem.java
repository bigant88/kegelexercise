package youthstudio.com.kegelexercises;

import com.google.gson.annotations.SerializedName;

/**
 * Created by thaodv on 8/21/16.
 */
public class ExerciseItem {
    @SerializedName("instruction_image")
    String imageName;
    @SerializedName("name")
    String instructionName;
    @SerializedName("instruction_file")
    String instructionFile;

    public String getImageName() {
        return imageName;
    }

    public String getInstructionName() {
        return instructionName;
    }

    public String getInstructionFile() {
        return instructionFile;
    }
}
