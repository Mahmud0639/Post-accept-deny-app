package com.manuni.postacceptdeny.models;

public class PermissionPost {
        private String posterEmail,posterPassword, posterName, posterProfession, posterId;

        public PermissionPost() {

        }

        public PermissionPost(String posterEmail, String posterPassword, String posterName, String posterProfession, String posterId) {
            this.posterEmail = posterEmail;
            this.posterPassword = posterPassword;
            this.posterName = posterName;
            this.posterProfession = posterProfession;
            this.posterId = posterId;
        }

        public String getPosterEmail() {
            return posterEmail;
        }

        public void setPosterEmail(String posterEmail) {
            this.posterEmail = posterEmail;
        }
        public String getPosterPassword(){
            return posterPassword;
        }
        public void setPosterPassword(String posterPassword){
            this.posterPassword = posterPassword;
        }

        public String getPosterName() {
            return posterName;
        }

        public void setPosterName(String posterName) {
            this.posterName = posterName;
        }

        public String getPosterProfession() {
            return posterProfession;
        }

        public void setPosterProfession(String posterProfession) {
            this.posterProfession = posterProfession;
        }

        public String getPosterId() {
            return posterId;
        }

        public void setPosterId(String posterId) {
            this.posterId = posterId;
        }

    }
