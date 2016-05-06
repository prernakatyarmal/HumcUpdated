package com.hackensack.umc.datastructure;

import java.util.ArrayList;

/**
 * Created by prerana_katyarmal on 10/27/2015.
 */
public class DocumentPojo {


    public DocumentPojo(ArrayList<com.hackensack.umc.datastructure.Documents> documents) {
        Documents = documents;
    }

    private ArrayList<Documents> Documents;

        public ArrayList<Documents> getDocuments ()
        {
            return Documents;
        }

        public void setDocuments (ArrayList<Documents> Documents)
        {
            this.Documents = Documents;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [Documents = "+Documents+"]";
        }


}
