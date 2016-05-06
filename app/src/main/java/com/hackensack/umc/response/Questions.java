package com.hackensack.umc.response;

import android.os.Parcel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by prerana_katyarmal on 10/15/2015.
 */
public class Questions implements Serializable {

    private String prompt;
    private String type;
    private ArrayList<String> answer;
    private String Answer;


    protected Questions(Parcel in) {


        prompt = in.readString();
        type = in.readString();
        answer = in.createStringArrayList();
        Answer = in.readString();
    }

    /* public static final Creator<Questions> CREATOR = new Creator<Questions>() {
         @Override
         public Questions createFromParcel(Parcel in) {
             return new Questions(in);
         }

         @Override
         public Questions[] newArray(int size) {
             return new Questions[size];
         }
     };
 */
    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Questions(String type, String answer) {
        this.type = type;
        this.Answer = answer;
    }

    @Override
    public String toString() {
        return "Questions{" +
                //  "prompt='" + prompt + '\'' +
                ", type='" + type + '\'' +
                ", answer=" + answer +
                '}';
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getAnswer() {
        return answer;
    }

    public void setAnswer(ArrayList<String> answer) {
        this.answer = answer;
    }

   /* @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(prompt);
        dest.writeString(type);
        dest.writeStringList(answer);
        dest.writeString(Answer);
    }*/

   /*"prompt": "Which of the following people do you know?",
            "type": "person.known.fic",
            "answer": [
            "ELAINE RYAN",
            "MIKE MAVARIDIS",
            "MIKE FRONAL",
            "None of the above"*/


}
