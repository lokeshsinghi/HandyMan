package com.example.locale_lite;

public class UploadImage {

        private String mname;
        private String mImageUrl;

        public UploadImage(){
            //empty Constructor needed
        }

        public UploadImage(String name, String imageUrl){
            if(name.trim().equals("")){
                name = "No Name";
            }

            mname = name;
            mImageUrl = imageUrl;
        }

        public String getName(){
            return mname;
        }
        public void setName(String name){
            mname = name;
        }

        public String getmImageUrl(){
            return  mImageUrl;
        }

        public void setImageUrl(String imageUrl){
            mImageUrl = imageUrl;
        }
}
